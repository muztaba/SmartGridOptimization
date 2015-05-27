package algorithm.aco.test;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by seal on 5/25/15.
 */
public class Graph {
    private Map<Integer, List<Node>> graph;
    private int source;

    public Graph() {
        graph = new HashMap<>();
    }

    public void setSource(int source) {
        this.source = source;
    }
    public void add(int u, int v, int use, int capacity, int priority) {
        if (graph.get(u) == null) {
            graph.put(u, new ArrayList<>());
            graph.get(u).add(new Node(v, use, priority, capacity));
        } else {
            graph.get(u).add(new Node(v, use, priority, capacity));
        }
    }
    public List<Node> getListOfNode(int u) {
        return graph.get(u);
    }
}
