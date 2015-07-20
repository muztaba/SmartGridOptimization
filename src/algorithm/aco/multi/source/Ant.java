package algorithm.aco.multi.source;

import Utils.ArrayUtils;
import Utils.CollectionUtils;
import algorithm.aco.pheromone.IPheromone;
import graph.version2.Graph;

import java.util.*;

/**
 * Created by seal on 7/18/15.
 */
public class Ant {
    private Graph graph;
    private IPheromone pheromone;

    private List<Integer> sourceList;
    private Set<Integer> visited = new HashSet<>();

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
     * Every time initiate the ant the source list will be shuffled.
     * Because ant will not place in source node in any order.
     */
    private void shuffleSourceList() {
        Collections.shuffle(this.sourceList);
    }

    private static class Node implements Comparable<Node> {
        private int nodeNumber;
        private double probability;

        public Node(int nodeNumber, double probability) {
            this.nodeNumber = nodeNumber;
            this.probability = probability;
        }

        @Override
        public int compareTo(Node node) {
            return Double.compare(node.probability, this.probability);
        }

    }

    private boolean moveAnt() {
        double denominator = calDenominator();
        if (denominator <= 0) {
            return false;
        }



        return false;
    }

    private double calDenominator() {
        double sum = 0.0;
        for (Graph.VertexInfo itr : graph.extractVertexInfo(currentNode)) {
            int V = itr.nodeNumber;
            if (visited.contains(V)) {
                continue;
            }
            sum += itr.loadShedding * pheromone.get(currentNode, V);
        }
        return sum;
    }

    private void setupAnt(final int nextNode) {
        setCurrentNode(nextNode);
        this.power = graph.getPower(currentNode);
    }

    private void setCurrentNode(final int nextNode) {
        this.currentNode = nextNode;
    }



}
