package algorithm.aco.multi.source;

import Utils.CollectionUtils;
import Utils.Pair;
import algorithm.aco.pheromone.IPheromone;
import graph.version2.Graph;

import java.util.*;

/**
 * Created by seal on 7/18/15.
 *
 * @author Muztaba Hasanat
 */
public class Ant {
    private Graph graph;
    private IPheromone pheromone;

    Deque<Integer> queue;

    Random random = new Random();
    private List<Integer> sourceList;
    private Set<Integer> visited = new HashSet<>();
    private Set<Pair<Integer, Integer>> occupiedLink = new HashSet<>();
    public Set<Pair<Integer, Integer>> visitedLink = new HashSet<>();
    private int currentNode;
    private double power;
    private double loadShedding;


    public static final double ALPHA = 5.0;
    public static final double BETA = 2.0;

    public Ant(final Graph graph) {
        this.graph = graph;
        Set<Integer> sourceSet = graph.getSourceList();
        // source set converted to List.
        this.sourceList = CollectionUtils.toList(sourceSet);
    }

    public void initiate(IPheromone pheromone) {
        graph.reset();
        occupiedLink.clear();
        visitedLink.clear();
        visited.clear();
        this.pheromone = pheromone;
        shuffleSourceList();

        for (int sourceNode : sourceList) {
            queue = new ArrayDeque<>();
            queue.add(sourceNode);

            while (!queue.isEmpty()) {
                int _currentNode = queue.poll();
                setupAnt(_currentNode);
                moveAnt();
            }
        }
        this.loadShedding = graph.calculateLoadShedding();
    }

    /**
     * At the first time when a ant place in a source node or in a left power node then this
     * method place the ant in that node and get the power of the node and set it for the current ant.
     *
     * @param node first node where some power has.
     */
    private void setupAnt(final int node) {
        this.currentNode = node;
        this.power = graph.getPower(currentNode);
    }

    /**
     * Every time initiate the ant the source list will be shuffled.
     * Because ant will not place in source node in any order.
     */
    private void shuffleSourceList() {
        Collections.shuffle(this.sourceList);
    }


    /**
     * This inner class use for finding out the maximum probability node.
     */
    private static class MaxProbNode implements Comparable<MaxProbNode> {
        private int nodeNumber;

        private double probability;

        public MaxProbNode(int nodeNumber, double probability) {
            this.nodeNumber = nodeNumber;
            this.probability = probability;
        }

        @Override
        public int compareTo(MaxProbNode node) {
            return Double.compare(node.probability, this.probability);
        }

    }

    private boolean moveAnt() {

        while (this.power > 0) {
            visited.add(this.currentNode); // Keep track which node has been visited.
            // If the current node demand node then give some electricity.
            // And if the node supply node then the ant simply move on.
            if (!graph.isSourceNode(this.currentNode)) {
                operationOnDemandNode();
            }

            // After giving power to a node if the ant's power will zero then the ant stop moving.
            if (this.power <= 0) {
                return false;
            }

            int nextNode = nextNodeSelection();

            //=========DEBUG=========//
//            printNextNode(nextNode);

            // If the ant won't move to next node then put the ant power to the node's residual power.
            if (nextNode < 0) {
                graph.addResidual(this.currentNode, this.power);
                return false;
            }

            occupiedLink.add(Pair.makePair(this.currentNode, nextNode));
            occupiedLink.add(Pair.makePair(nextNode, this.currentNode));

            double linkCapacity = graph.getCapacity(this.currentNode, nextNode);

            if (power > linkCapacity) {
                double residual = power - linkCapacity;
                graph.addResidual(this.currentNode, residual);
                power = linkCapacity;
                queue.add(this.currentNode);
            }

            visitedLink.add(Pair.makePair(currentNode, nextNode));
            this.currentNode = nextNode;
        }

        return false;
    }

    /**
     * This method give the power to the demand node. Get the load shedding of the current node.
     * Take the minimum between current ant power and the current node load shedding. Generate power
     * power and set to the power to the current node. Then deduct the generated power from the ant's power.
     * Ant's power will not negative.
     */
    private void operationOnDemandNode() {
        double loadShedding = graph.getLoadShedding(this.currentNode);
        double generatePower = random.nextInt((int) Math.min(this.power, loadShedding));
        graph.addPower(this.currentNode, generatePower);
        this.power -= generatePower;
    }

    private int nextNodeSelection() {
        double denominator = calculateDenominator();
        if (denominator <= 0) {
            return -1;
        }
        PriorityQueue<MaxProbNode> probNodes = new PriorityQueue<>();

        for (Graph.VertexInfo itr : graph.extractVertexInfo(this.currentNode)) {
            int V = itr.nodeNumber;

            if (visited.contains(V)) {
                continue;
            }

            if (occupiedLink.contains(Pair.makePair(this.currentNode, V))) {
                continue;
            }

            double pheromone = this.pheromone.get(currentNode, V);
            double nn = itr.loadShedding;
            double p = probTo(pheromone, nn, denominator);
            MaxProbNode maxProbNode = new MaxProbNode(V, p);
            probNodes.add(maxProbNode);
        }

        int nextNode = -1;

        if (!probNodes.isEmpty()) {
            nextNode = probNodes.peek().nodeNumber;
        }

        return nextNode;
    }

    private double calculateDenominator() {
        double sum = 0.0;
        for (Graph.VertexInfo itr : graph.extractVertexInfo(this.currentNode)) {
            int V = itr.nodeNumber;
            if (visited.contains(V)) {
                continue;
            }
            sum += Math.pow(itr.loadShedding, BETA) * Math.pow(pheromone.get(currentNode, V), ALPHA);
        }
        return sum;
    }

    private double probTo(double pheromone, double nn, double denominator) {
        double nominator = Math.pow(pheromone, ALPHA) * Math.pow(nn, BETA);
        return nominator / denominator;
    }

    /**
     * Return the total load shedding of the graph that this ant
     * has visited. In another way load shedding means quality of the
     * graph.
     *
     * @return total load shedding of the graph that this ant visited.
     */
    public double getLoadShedding() {
        return this.loadShedding;
    }


    //===========================================//
    //==================DEBUG====================//
    //===========================================//
    public void printVisitedNode() {
        for (int i : visited) {
            System.out.println(i);
        }
    }

    public void printNextNode(int node) {
        System.out.println(node);
    }

    public int getVisitedNodeNumber() {
        return this.visited.size();
    }
}
