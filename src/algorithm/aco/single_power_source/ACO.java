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
    // Probability fluctuation factor
    public static final double AlPHA = 1.0;
    public static final double BETA = 1.0;

    private int source;
    private double power;
    private int powersIndex = Integer.MAX_VALUE;
    private int sourceEdgeIndex;

    private Set<Integer> visited;
    private List<Node> graph;
    private List<Double> powers;
    private Ant ant;

    public ACO(List<Node> graph, int source) {
        this.graph = graph;
        this.source = source;
        this.power = graph.get(source).getSupply();
        // initialize the visited List with number of the total node in graph.
        this.visited = new HashSet<>(graph.size());
        // powers is arrayList that store the distribution of power within the source's edges.
        this.powers = new ArrayList<>(graph.get(source).degree());
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
        // Distribute the power of the source node within the source edge.
        if (powersIndex < powers.size()) {
            ant.setPower(powers.get(powersIndex++));
        } else {
            generatePowers();
            ant.setPower(powers.get(powersIndex = 0));
        }
    }

    private void moveAnt() {
        // Ant place at the source node. Force to chose edge sequentially.
        // Else ant place any node other than source. Next node chose probabilistically.
        if (ant.getCurrentNode() == source) {
            if (sourceEdgeIndex < graph.get(source).degree()) {
                ant.nextNode(graph.get(source).edge(sourceEdgeIndex++));
            } else {
                ant.nextNode(graph.get(source).edge(sourceEdgeIndex = 0));
            }
        } else {

        }
    }

    /**
     * This method chose probabilistically next node from where the ant is currently
     * standing.
     *
     * @param currentNode currently where the ant now.
     * @return nextNode where the ant next to move.
     */
    private int nextNodeSelection(int currentNode) {
        int nextNode = -1;
        double n = 0.0;
        double maxProbability = 0.0;
        int indexArray = 0;
        List<Edge> edgesList = graph.get(currentNode).edges();
        for (Edge i : edgesList) {
            n += i.getCapacity() * graph.get(i.getConnectedNode()).getDemand();
        }
        for (Edge i : edgesList) {
            double temp = pow(i.getCapacity(), AlPHA) * pow(graph.get(i.getConnectedNode()).getDemand(), BETA) / n;
            if (temp > maxProbability) {
                nextNode = i.getConnectedNode();
                maxProbability = temp;
            }
        }
        return nextNode;
    }

    /**
     * Faster pow method. Default pow method is slow.
     * This method approximately 25 times faster that java's pow method.
     */
    public static double pow(final double a, final double b) {
        final int x = (int) (Double.doubleToLongBits(a) >> 32);
        final int y = (int) (b * (x - 1072632447) + 1072632447);
        return Double.longBitsToDouble(((long) y) << 32);
    }

    private void generatePowers() {
        powers.clear();
        double temp = 0.0;
        double _power = this.power;
        for (int i = 0; i < powers.size(); i++) {
            temp = _power * Math.random();
            _power -= temp;
            powers.add(temp);
        }
    }
}