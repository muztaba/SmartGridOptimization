package graph.version2;

import Utils.Pair;
import graph.vertex.Node;

import java.util.*;

/**
 * Created by seal on 7/13/15.
 */
public class Graph <V extends Node, E> {
    Map<Integer, V> vertexes = new HashMap<>();
    Map<Integer, Set<Pair<Integer, Double>>> edges = new HashMap<>();

    // Keep the list of the sources.
    Set<Integer> sourceList = new HashSet<>();

    public void addVertex(final V node) {
        int nodeNumber = node.getNodeNumber();
        vertexes.put(nodeNumber, node);
        if (node.getSupply_demand() > 0) {
            sourceList.add(nodeNumber);
        }
    }

    public void addEdge(int u, int v, double capacity) {
        Pair<Integer, Double> U = Pair.makePair(v, capacity);
        Pair<Integer, Double> V = Pair.makePair(u, capacity);

        if (!edges.containsKey(u)) {
            edges.put(u, new HashSet<>());
        }
        if (!edges.containsKey(v)) {
            edges.put(v, new HashSet<>());
        }

        edges.get(u).add(U);
        edges.get(v).add(V);
    }

}
