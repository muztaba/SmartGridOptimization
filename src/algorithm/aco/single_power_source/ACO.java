package algorithm.aco.single_power_source;

import Utils.ArrayUtils;
import algorithm.Run;
import graph.Edge;
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
    public static final double ALPHA = 1.0;
    public static final double BETA = 1.0;
    public static final double EVAPORATION = 0.5;

    private double[][] pheromoneTrail;

    private final int source; // Considering one power source
    private final int size; // Graph size
    private double power; // Power of the source
    private int powersIndex = Integer.MAX_VALUE;
    private int sourceEdgeIndex = Integer.MAX_VALUE;

    final private List<Node> graph;
    private List<Double> powers;
    private Map<Integer, Double> denominator; // calculate the denominator for probability.
    private Ant ant;
    private BestTour bestTourSoFar;

    public ACO(final List<Node> graph, final int source) {
        this.graph = graph;
        this.source = source;
        this.size = graph.size();
        this.power = graph.get(source).getSupply();
        this.pheromoneTrail = new double[size][size]; // Create pheromone trail 2-D array
        ArrayUtils.fill(pheromoneTrail, 1.0); // Initialize the array with 1.0 value.
        // powers is arrayList that store the distribution of power within the source's edges.
        this.powers = new ArrayList<>(graph.get(source).degree());
        this.denominator = new HashMap<>(size);
    }

    /**
     *  From this method algorithm begin to start. This is a interface implementation.
     *  run() method override from Run interface.
     */
    @Override
    public void run() {
        // For the primary ants.
        System.out.println("Algorithm start");
        for (int iteration = 0; iteration < primaryIteration; iteration++) {
            setupAnts();
            moveAnt();
            bestTourSoFar();
            // After every 10 iteration update the pheromone trail along with the best tour path.
            if (iteration % 10 == 0) updateTrail();
            // After successfully one iteration print the iteration number in the console.
            System.out.println(iteration);
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
            powersIndex = 0;
            ant.setPower(powers.get(powersIndex));
        }
    }

    /**
     *  This method move the ant in the graph until it's power will empty or if the there is no why to
     *  move(when the nextNodeSelection return -1).
     *  If the ant currently standing at the source then force to chose ege sequentially or the
     *  ant chose probabilistically to move next node.
     */
    private void moveAnt() {
        while (ant.isMoveStop()) {
            int nextNode = -1;
            if (ant.getCurrentNode() == source) {
                if (sourceEdgeIndex < graph.get(source).degree()) {
                    nextNode = graph.get(source).edge(sourceEdgeIndex++);
                    ant.nextNode(nextNode);
                } else {
                    sourceEdgeIndex = 0;
                    nextNode = graph.get(source).edge(sourceEdgeIndex);
                    ant.nextNode(nextNode);
                }
            } else {
                nextNode = nextNodeSelection(ant.getCurrentNode());
                if (nextNode < 0) break;
                ant.nextNode(nextNode);
            }
        }
    }


    /**
     * After certain iteration pheromone trail will be updated. Ants made tour that has
     * saved into the bestTourSoFar object. Get the tours from that this object and
     * update the pheromone trail.
     * Update happen with this equation, (1 - p) * t (u -> v).
     * where p = EVAPORATION rate and t = previous pheromone to node u -> v.
     */
    private void updateTrail() {
        List<List<Integer>> tours = bestTourSoFar.getBestTour();
        for (List<Integer> tour : tours) {
            for (int i = 0; i < tour.size() - 1; i++) {
                int u = tour.get(i);
                int v = tour.get(i + 1);
                pheromoneTrail[u][v] *= (1 - EVAPORATION); // (1 - p) * t(u, v)
            }
        }
    }

    private void bestTourSoFar() {
        bestTourSoFar.addTour(sourceEdgeIndex, ant.getTour());
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
        double maxProbability = Double.MIN_VALUE;
        List<Edge> edgesList = graph.get(currentNode).edges();// Get the edges of the current node where the ant standing.

        if (edgesList == null || edgesList.size() == 0) {
            return backTrack();
        }

        // Check if there is pre-calculate denominator.
        if (!denominator.containsKey(currentNode)) {
            calculateDenominator(currentNode, edgesList);
        }

        // Calculating the probability the ant's next move.
        for (Edge i : edgesList) {
            if (!ant.isVisitedBefore(i.getConnectedNode())) {
                // Multiply pheromone and the demand.
                double temp = pow(getPheromone(currentNode, i.getConnectedNode()), ALPHA) *
                        pow(graph.get(i.getConnectedNode()).getDemand(), BETA) / denominator.get(currentNode);
                if (temp > maxProbability) {
                    nextNode = i.getConnectedNode();
                    maxProbability = temp;
                }
            }
        }
        return nextNode;
    }

    /**
     * When an ant standing at the leaf node that is the node has no out degree then
     * this method place the ant at node immediately before the current node. From this
     * node ant again chose next node probabilistically through the nextNodeSelection
     * method except the visited node.
     *
     * @return nextNode from where the ant begins to tour again.
     * @see nextNodeSelection
     */
    public int backTrack() {
        ant.removeFromEnd(); // Remove the current node that is dead end.
        int currentNode = ant.getCurrentNode();
        return nextNodeSelection(currentNode);
    }

    /**
     * This method calculate the denominator if it is not already calculated.
     *
     * @param currentNode where the ant currently standing
     * @param edgeList    currentNode's edges where the ant possible to go
     * @see nextNodeSelection
     */
    public void calculateDenominator(final int currentNode, final List<Edge> edgeList) {
        double n = 0.0;
        if (edgeList.isEmpty()) {
            denominator.put(currentNode, Double.MAX_VALUE);
        } else {
            for (Edge i : edgeList) {
                n += getPheromone(currentNode, i.getConnectedNode()) * graph.get(i.getConnectedNode()).getDemand();
            }
            denominator.put(currentNode, n);
        }
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
        this.powers.clear();
        double temp = 0.0;
        double _power = this.power;
        for (int i = 0; i < graph.get(source).degree(); i++) {
            temp = _power * Math.random();
            _power -= temp;
            this.powers.add(temp);
        }
        // Set the current power distribution within edges of source to the BestTour object.
        // Lazy initialization apply.
        if (bestTourSoFar != null) {
            bestTourSoFar.setPowers(powers);
        } else {
            bestTourSoFar = new BestTour();
            bestTourSoFar.setPowers(powers);
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