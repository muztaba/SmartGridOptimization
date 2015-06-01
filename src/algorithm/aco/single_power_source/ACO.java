package algorithm.aco.single_power_source;

import algorithm.Run;
import graph.Edge;
import graph.GraphInput;
import graph.Node;

import java.io.IOException;
import java.util.*;

/**
 * Created by seal on 5/28/15.
 */
public class ACO implements Run {
    public static final int primaryIteration = 1000;
    public static final int secondaryIteration = 1000;
    // Probability fluctuation factor
    public static final double AlPHA = 1.0;
    public static final double BETA = 1.0;
    public static final double EVAPORATION = 0.5;
    private double[][] pheromoneTrail;

    private final int source;
    private final int size;
    private double power;
    private int powersIndex = Integer.MAX_VALUE;
    private int sourceEdgeIndex;

    final private List<Node> graph;
    private Set<Integer> visited;
    private List<Double> powers;
    // Store the value of all connected node demand and capacity.
    // This will calculate when the next node chose with probability. It can reduce some calculation.
    private Map<Integer, Double> denominator;
    private Ant ant;

    public ACO(final List<Node> graph, final int source) {
        this.graph = graph;
        this.source = source;
        this.size = graph.size();
        this.power = graph.get(source).getSupply();
        this.pheromoneTrail = new double[size][size]; // Create pheromone trail 2-D array
        Arrays.fill(pheromoneTrail, 1.0); // Initialize the array with 1.0 value.
        // initialize the visited List with number of the total node in graph.
        this.visited = new HashSet<>(size);
        // powers is arrayList that store the distribution of power within the source's edges.
        this.powers = new ArrayList<>(graph.get(source).degree());
        this.denominator = new HashMap<>(size);
    }

    @Override
    public void run() {
        // For the primary ants.
        for (int iteration = 0; iteration < primaryIteration; iteration++) {
            setupAnts();
            moveAnt();
        }
    }

    /**
     * Create an new ant and place at the primary source for the first time.
     */
    private void setupAnts() {
        ant = new Ant();
        // Place the ant at the source node.
        ant.nextNode(source);
        // Distribute the power of the source node within the source edge. Initially powerIndex = Integer.MAX_VALUE
        if (powersIndex < powers.size()) {
            ant.setPower(powers.get(powersIndex++));
        } else {
            generatePowers();
            ant.setPower(powers.get(powersIndex = 0));
        }
    }

    private void moveAnt() {
        while (ant.isMoveStop()) {
            // Ant place at the source node. Force to chose edge sequentially.
            // Else ant place any node other than source. Next node chose probabilistically.
            if (ant.getCurrentNode() == source) {
                if (sourceEdgeIndex < graph.get(source).degree()) {
                    ant.nextNode(graph.get(source).edge(sourceEdgeIndex++));
                } else {
                    ant.nextNode(graph.get(source).edge(sourceEdgeIndex = 0));
                }
            } else {
                int nextNode = nextNodeSelection(ant.getCurrentNode());
                ant.nextNode(nextNode);
            }
        }
    }

    /**
     * This method chose probabilistically next node from where the ant is currently
     * standing.
     *
     * @param currentNode currently where the ant now.
     * @return nextNode where the ant next to move.
     */
    private int nextNodeSelection(final int currentNode) {
        int nextNode = -1;
        double n = 0.0;
        double maxProbability = 0.0;
        List<Edge> edgesList = graph.get(currentNode).edges();
        // Check if there is pre-calculate denominator. If not then calculate and store them.
        if (!denominator.containsKey(currentNode)) {
            if (edgesList.isEmpty()) {
                denominator.put(currentNode, Double.MAX_VALUE);
            } else {
                for (Edge i : edgesList) {
                    n += i.getCapacity() * graph.get(i.getConnectedNode()).getDemand();
                }
                denominator.put(currentNode, n);
            }
        }
        for (Edge i : edgesList) {
            // Multiply pheromone and the demand.
            double temp = pow(getPheromone(currentNode, i.getConnectedNode()), AlPHA) *
                    pow(graph.get(i.getConnectedNode()).getDemand(), BETA) / denominator.get(currentNode);
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

    /**
     * @param i node u
     * @param j node v
     * @return the pheromone from node u to node v;
     */
    private double getPheromone(final int i, final int j) {
        return pheromoneTrail[i][j];
    }

}