package algorithm.aco.single_power_source;

import algorithm.Run;
import graph.Edge;
import graph.GraphInput;
import graph.Node;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by seal on 5/28/15.
 */
public class ACO implements Run {
    private Set<Integer> visited;
    private List<Node> graph;
    private int source;
    public ACO(String filePath, int source) {
        GraphInput readGraph = new GraphInput();
        try {
            this.graph = readGraph.readGraph(filePath);
        } catch (IOException e) {
            System.err.println("File Not Found");
            e.printStackTrace();
        }
        this.source = source;
        // initialize the visited List with number of the total node in graph.
        visited = new HashSet<>(graph.size());
        for (int i = 1; i <= graph.size(); i++) visited.add(i);
    }

    @Override
    public void run() {
        // For the primary ants.

    }
}
