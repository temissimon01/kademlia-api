package com.github.ep2p.kademlia.node;

import com.github.ep2p.kademlia.connection.ConnectionInfo;
import com.github.ep2p.kademlia.exception.NodeIsOfflineException;

import java.util.List;

public abstract class KademliaNodeListenerDecorator<ID extends Number, C extends ConnectionInfo, K, V> implements KademliaNodeListener<ID, C, K, V> {
    private final KademliaNodeListener<ID, C, K, V> kademliaNodeListener;

    public KademliaNodeListenerDecorator(KademliaNodeListener<ID, C, K, V> kademliaNodeListener) {
        this.kademliaNodeListener = kademliaNodeListener;
    }

    @Override
    public void onReferencedNodesUpdate(KademliaNode<ID, C> kademliaNode, List<Node<ID, C>> referencedNodes) {
        kademliaNodeListener.onReferencedNodesUpdate(kademliaNode, referencedNodes);
    }

    @Override
    public void onBootstrapDone(KademliaNode<ID, C> kademliaNode) {
        kademliaNodeListener.onBootstrapDone(kademliaNode);
    }

    @Override
    public void onNewNodeAvailable(KademliaNode<ID, C> kademliaNode, Node<ID, C> node) {
        kademliaNodeListener.onNewNodeAvailable(kademliaNode, node);
    }

    @Override
    public void onShutdownComplete(KademliaNode<ID, C> kademliaNode) {
        kademliaNodeListener.onShutdownComplete(kademliaNode);
    }

    @Override
    public void onBeforeShutdown(KademliaNode<ID, C> kademliaNode) {
        kademliaNodeListener.onBeforeShutdown(kademliaNode);
    }

    @Override
    public void onStartupComplete(KademliaNode<ID, C> kademliaNode) {
        kademliaNodeListener.onStartupComplete(kademliaNode);
    }

    @Override
    public void onPing(KademliaNode<ID, C> kademliaNode, Node<ID, C> node) throws NodeIsOfflineException {
        kademliaNodeListener.onPing(kademliaNode, node);
    }

    @Override
    public void onKeyLookupResult(KademliaNode<ID, C> kademliaNode, Node<ID, C> node, K key, V value) {
        kademliaNodeListener.onKeyLookupResult(kademliaNode, node, key, value);
    }

    @Override
    public void onKeyStoredResult(KademliaNode<ID, C> kademliaNode, Node<ID, C> node, K key, boolean success) {
        kademliaNodeListener.onKeyStoredResult(kademliaNode, node, key, success);
    }
}
