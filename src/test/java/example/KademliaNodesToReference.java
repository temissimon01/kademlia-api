package example;

import java.util.ArrayList;
import java.util.Arrays;

public class KademliaNodesToReference {
    public static void main(String[] args) {
        // Node identifier size
        int identifierSize = 128;

        // Valid distances according to identifier size
        ArrayList<Integer> distances = new ArrayList<>();
        for(int i = 0; i < identifierSize; i++){
            distances.add((int) Math.pow(2, i));
        }

        // Your node id here. Must be in range of 0 -> (2 power identifierSize)
        int nodeId = 1;

        // Extracting nodes with specified distance
        ArrayList<Integer> validNodes = new ArrayList<>();
        distances.forEach(distance -> {
            validNodes.add(nodeId ^ distance);
        });

        System.out.println("Nodes to reference for "+ nodeId +" are: " + Arrays.toString(validNodes.toArray()));
    }
}
