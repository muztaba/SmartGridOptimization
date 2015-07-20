package algorithm.aco.multi.source;

import Utils.ArrayUtils;
import Utils.CollectionUtils;
import Utils.Pair;
import algorithm.aco.pheromone.IPheromone;
import graph.version2.Graph;

import java.util.*;

/**
 * Created by seal on 7/18/15.
 */
public class Ant {
    private Graph graph;
    private IPheromone pheromone;

    Random random = new Random();

    private List<Integer> sourceList;
    private Set<Integer> visited = new HashSet<>();
    private Set<Pair<Integer, Integer>> occupiedLink = new HashSet<>();
    private int currentNode;
    private double power;

    public static final double ALPHA = 1.0;
    public static final double BETA = 1.0;
    public static final double EVAPORATION = .25;

    public Ant(final Graph graph) {
        this.graph = graph;
        this.sourceList = graph.getSourceList();
    }

    public void initiate(IPheromone pheromone) {
        this.pheromone = pheromone;
        shuffleSourceList();

        for (int sourceNode : sourceList) {
            Deque<Integer> queue = new ArrayDeque<>();
            queue.add(sourceNode);

            while (!queue.isEmpty()) {
                int _currentNode = queue.poll();
                setupAnt(_currentNode);
                moveAnt();
            }

        }

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
        boolean isAntMoving = true;

        while (isAntMoving) {
            if (!graph.isSourceNode(this.currentNode)) {
                operationOnDemandNode();
            }

            if (this.power <= 0) {
                return false;
            }

            int nextNode = nextNodeSelection();

            if (nextNode < 0) {
                return false;
            }

            visited.add(nextNode);
            occupiedLink.add(Pair.makePair(this.currentNode, nextNode));
            occupiedLink.add(Pair.makePair(nextNode, this.currentNode));
            this.currentNode = nextNode;

            //....................................//
            // left power work
            //.....................................//
        }

        return false;
    }

    private void operationOnDemandNode() {
        double loadShedding = graph.getLoadShedding(this.currentNode);
        double generatePower = random.nextInt((int)loadShedding);
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
            double p = 0.0;
            int V = itr.nodeNumber;

            if (visited.contains(V)) {
                continue;
            }

            if (occupiedLink.contains(Pair.makePair(this.currentNode, V))) {
                continue;
            }

            double pheromone = this.pheromone.get(currentNode, V);
            double nn = itr.loadShedding;
            p = probTo(pheromone, nn, denominator);
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
            sum += itr.loadShedding * pheromone.get(currentNode, V);
        }
        return sum;
    }

    private double probTo(double pheromone, double nn, double denominator) {
        double p = 0.0;
        double nominator = Math.pow(pheromone, ALPHA) * Math.pow(nn, BETA);
        p = nominator / denominator;
        return p;
    }

    private void setCurrentNode(final int nextNode) {
        this.currentNode = nextNode;
    }


}
