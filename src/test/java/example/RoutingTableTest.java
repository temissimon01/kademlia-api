package example;

import com.github.ep2p.kademlia.Common;
import com.github.ep2p.kademlia.connection.EmptyConnectionInfo;
import com.github.ep2p.kademlia.connection.LocalNodeConnectionApi;
import com.github.ep2p.kademlia.node.IncrementalNodeIdFactory;
import com.github.ep2p.kademlia.node.KademliaSyncRepositoryNode;
import com.github.ep2p.kademlia.node.NodeIdFactory;
import com.github.ep2p.kademlia.node.SampleRepository;
import com.github.ep2p.kademlia.table.Bucket;
import com.github.ep2p.kademlia.table.RoutingTable;
import com.github.ep2p.kademlia.table.SimpleRoutingTableFactory;

public class RoutingTableTest {
    public static void main(String[] args) {
        LocalNodeConnectionApi nodeApi = new LocalNodeConnectionApi();
        NodeIdFactory nodeIdFactory = new IncrementalNodeIdFactory();
        SimpleRoutingTableFactory routingTableFactory = new SimpleRoutingTableFactory();
        Common.IDENTIFIER_SIZE = 4;
        Common.REFERENCED_NODES_UPDATE_PERIOD_SEC = 2;
        RoutingTable<Integer, EmptyConnectionInfo, Bucket<Integer, EmptyConnectionInfo>> routingTable = routingTableFactory.getRoutingTable(0);

        KademliaSyncRepositoryNode<Integer, EmptyConnectionInfo, Integer, String> node0 = new KademliaSyncRepositoryNode<>(nodeIdFactory.getNodeId(), routingTableFactory.getRoutingTable(0), nodeApi, new EmptyConnectionInfo(), new SampleRepository());
        KademliaSyncRepositoryNode<Integer, EmptyConnectionInfo, Integer, String> node1 = new KademliaSyncRepositoryNode<>(nodeIdFactory.getNodeId(), routingTableFactory.getRoutingTable(1), nodeApi, new EmptyConnectionInfo(), new SampleRepository());
        KademliaSyncRepositoryNode<Integer, EmptyConnectionInfo, Integer, String> node2 = new KademliaSyncRepositoryNode<>(nodeIdFactory.getNodeId(), routingTableFactory.getRoutingTable(2), nodeApi, new EmptyConnectionInfo(), new SampleRepository());
        KademliaSyncRepositoryNode<Integer, EmptyConnectionInfo, Integer, String> node3 = new KademliaSyncRepositoryNode<>(nodeIdFactory.getNodeId(), routingTableFactory.getRoutingTable(3), nodeApi, new EmptyConnectionInfo(), new SampleRepository());
        KademliaSyncRepositoryNode<Integer, EmptyConnectionInfo, Integer, String> node4 = new KademliaSyncRepositoryNode<>(nodeIdFactory.getNodeId(), routingTableFactory.getRoutingTable(4), nodeApi, new EmptyConnectionInfo(), new SampleRepository());
        KademliaSyncRepositoryNode<Integer, EmptyConnectionInfo, Integer, String> node5 = new KademliaSyncRepositoryNode<>(nodeIdFactory.getNodeId(), routingTableFactory.getRoutingTable(5), nodeApi, new EmptyConnectionInfo(), new SampleRepository());
        KademliaSyncRepositoryNode<Integer, EmptyConnectionInfo, Integer, String> node6 = new KademliaSyncRepositoryNode<>(nodeIdFactory.getNodeId(), routingTableFactory.getRoutingTable(6), nodeApi, new EmptyConnectionInfo(), new SampleRepository());
        KademliaSyncRepositoryNode<Integer, EmptyConnectionInfo, Integer, String> node7 = new KademliaSyncRepositoryNode<>(nodeIdFactory.getNodeId(), routingTableFactory.getRoutingTable(7), nodeApi, new EmptyConnectionInfo(), new SampleRepository());
        KademliaSyncRepositoryNode<Integer, EmptyConnectionInfo, Integer, String> node8 = new KademliaSyncRepositoryNode<>(nodeIdFactory.getNodeId(), routingTableFactory.getRoutingTable(8), nodeApi, new EmptyConnectionInfo(), new SampleRepository());
        KademliaSyncRepositoryNode<Integer, EmptyConnectionInfo, Integer, String> node9 = new KademliaSyncRepositoryNode<>(nodeIdFactory.getNodeId(), routingTableFactory.getRoutingTable(9), nodeApi, new EmptyConnectionInfo(), new SampleRepository());



        routingTable.update(node0);
        routingTable.update(node1);
        routingTable.update(node2);
        routingTable.update(node3);
        routingTable.update(node4);
        routingTable.update(node5);
        routingTable.update(node6);
        routingTable.update(node7);
        routingTable.update(node8);
        routingTable.update(node9);

        routingTable.getBuckets().forEach(bucket -> {
            System.out.println(bucket.getNodeIds());
        });
    }
}
