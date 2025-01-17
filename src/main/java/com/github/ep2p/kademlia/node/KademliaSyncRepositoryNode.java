package com.github.ep2p.kademlia.node;

import com.github.ep2p.kademlia.connection.ConnectionInfo;
import com.github.ep2p.kademlia.connection.NodeConnectionApi;
import com.github.ep2p.kademlia.exception.GetException;
import com.github.ep2p.kademlia.exception.StoreException;
import com.github.ep2p.kademlia.model.GetAnswer;
import com.github.ep2p.kademlia.model.StoreAnswer;
import com.github.ep2p.kademlia.table.Bucket;
import com.github.ep2p.kademlia.table.RoutingTable;
import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.github.ep2p.kademlia.model.StoreAnswer.Result.TIMEOUT;

public class KademliaSyncRepositoryNode<ID extends Number, C extends ConnectionInfo, K, V> extends KademliaRepositoryNode<ID, C,K,V> {
    private volatile Map<K, StoreAnswer<ID, K>> storeMap = new ConcurrentHashMap<>();
    private volatile Map<K, Lock> storeLockMap = new HashMap<>();
    private volatile Map<K, GetAnswer<ID, K, V>> getMap = new HashMap<>();
    private volatile Map<K, Lock> getLockMap = new HashMap<>();

    public KademliaSyncRepositoryNode(ID nodeId, RoutingTable<ID, C, Bucket<ID, C>> routingTable, NodeConnectionApi<ID, C> nodeConnectionApi, C connectionInfo, KademliaRepository<K, V> kademliaRepository) {
        super(nodeId, routingTable, nodeConnectionApi, connectionInfo, kademliaRepository);
    }

    public StoreAnswer<ID, K> store(K key, V value, long timeout, TimeUnit timeUnit) throws StoreException, InterruptedException {
        Lock keyLock = new ReentrantLock();
        synchronized (this){
            Lock oldLock = storeLockMap.putIfAbsent(key, keyLock);
            if(oldLock != null){
                keyLock = oldLock;
            }
        }
        if (keyLock.tryLock()) {
            try {
                StoreAnswer<ID, K> storeAnswer = super.store(key, value);
                if(storeAnswer.getResult().equals(StoreAnswer.Result.STORED)){
                    return storeAnswer;
                }else {
                    StoreAnswer<ID, K> watchableStoreAnswer = new StoreAnswer<>();
                    watchableStoreAnswer.setResult(TIMEOUT);
                    storeMap.putIfAbsent(key, watchableStoreAnswer);
                    if(timeUnit == null)
                        watchableStoreAnswer.watch();
                    else
                        watchableStoreAnswer.watch(timeout, timeUnit);
                    return watchableStoreAnswer;
                }
            }finally {
                keyLock.unlock();
                storeLockMap.remove(key);
                storeMap.remove(key);
            }
        }else {
            StoreAnswer<ID, K> kWatchableStoreAnswer = storeMap.get(key);
            if(kWatchableStoreAnswer != null){
                if(timeUnit == null)
                    kWatchableStoreAnswer.watch();
                else
                    kWatchableStoreAnswer.watch(timeout, timeUnit);
                return kWatchableStoreAnswer;
            }else {
                throw new StoreException("Key is already under process!");
            }
        }
    }

    @Override
    @SneakyThrows
    public StoreAnswer<ID, K> store(K key, V value) throws StoreException {
        return this.store(key, value, 0, null);
    }

    @SneakyThrows
    public GetAnswer<ID, K, V> get(K key, long timeout, TimeUnit timeUnit) throws GetException {
        Lock keyLock = new ReentrantLock();
        synchronized (this){
            Lock oldLock = getLockMap.putIfAbsent(key, keyLock);
            if(oldLock != null){
                keyLock = oldLock;
            }
        }
        if (keyLock.tryLock()) {
            try {
                GetAnswer<ID, K, V> getAnswer = super.get(key);
                if(getAnswer.getResult().equals(GetAnswer.Result.FOUND)){
                    return getAnswer;
                }else {
                    GetAnswer<ID, K, V> answer = new GetAnswer<>();
                    answer.setResult(GetAnswer.Result.TIMEOUT);
                    getMap.putIfAbsent(key, answer);
                    if(timeUnit == null)
                        answer.watch();
                    else
                        answer.watch(timeout, timeUnit);
                    return answer;
                }
            }finally {
                keyLock.unlock();
                getLockMap.remove(key);
                getMap.remove(key);
            }
        }else {
            GetAnswer<ID, K,V> getAnswer = getMap.get(key);
            if(getAnswer != null){
                if(timeUnit == null)
                    getAnswer.watch();
                else
                    getAnswer.watch(timeout, timeUnit);
                return getAnswer;
            }else {
                throw new GetException("Key is already under process!");
            }
        }
    }

    @SneakyThrows
    @Override
    public GetAnswer<ID, K, V> get(K key) throws GetException {
        return this.get(key, 0, null);
    }

    @Override
    public void onGetResult(Node<ID, C> node, K key, V value) {
        super.onGetResult(node, key, value);
        GetAnswer<ID, K, V> getAnswer = getMap.get(key);
        getAnswer.setNodeId(node.getId());
        getAnswer.setAlive(true);
        getAnswer.setKey(key);
        getAnswer.setValue(value);
        getAnswer.setResult(value == null ? GetAnswer.Result.FAILED : GetAnswer.Result.FOUND);
        getAnswer.release();
    }

    @SneakyThrows
    @Override
    public void onStoreResult(Node<ID, C> node, K key, boolean successful) {
        super.onStoreResult(node, key, successful);
        StoreAnswer<ID, K> kStoreAnswer = storeMap.get(key);
        kStoreAnswer.setResult(successful ? StoreAnswer.Result.STORED : StoreAnswer.Result.FAILED);
        kStoreAnswer.setKey(key);
        kStoreAnswer.setNodeId(node.getId());
        kStoreAnswer.setAlive(true);
        kStoreAnswer.release();
    }
}
