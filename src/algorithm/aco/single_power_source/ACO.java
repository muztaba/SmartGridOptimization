package algorithm.aco.single_power_source;

import algorithm.Run;
import graph.Edge;
import graph.GraphInput;
import graph.Node;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by seal on 5/28/15.
 */
public class ACO implements Run {
    public static final int primaryIteration = 1000;
    public static final int secondaryIteration = 1000;

    private int source;
    private double power;

    private Set<Integer> visited;
    private List<Node> graph;
    private List<Double> powers;
    private Ant ant;

    public ACO(String filePath, int source) {
        readGraph(filePath);
        this.source = source;
        this.power = graph.get(source).getSupply();
        // initialize the visited List with number of the total node in graph.
        this.visited = new HashSet<>(graph.size());
        // powers is arrayList that store the distribution of power within the source's edges.
        this.powers = new ArrayList<>(graph.get(source).degree());
    }
    private void readGraph(String filePath) {
        GraphInput in = new GraphInput();
        try {
            this.graph = in.readGraph(filePath);
        } catch (IOException e) {
            System.err.println("File Not Found");
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        // For the primary ants.
        for (int iteration = 0; iteration < primaryIteration; iteration++) {
            setupAnts();
        }
    }

    /**
     * Create an new ant and place at the primary source for the first time.
     */
    private void setupAnts() {
        ant = new Ant();
        // Place the ant at the source node.
        ant.nextNode(source);
    }
}
