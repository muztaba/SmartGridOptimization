package graph.version2;

import Utils.Pair;
import graph.vertex.Node;

import java.util.*;

/**
 * Created by seal on 7/13/15.
 */
public class Graph {
    private Map<Integer, Node> vertexes = new HashMap<>();
    private Map<Integer, Set<Pair<Integer, Double>>> edges = new HashMap<>();

    // Keep the list of the sources.
    private List<Integer> sourceList = new ArrayList<>();

    /**
     * Add the vertex(node) to the graph. Using a map structure to keep the node.
     * map key is the vertex number and the value is the vertex itself.
     *
     * @param node node class that represent the vertex.
     */
    public void addVertex(final Node node) {
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

    /**
     * Return the list of sources with shuffle every time the method call.
     * This can ensure that ant chose source every time randomly.
     *
     * @return list of the sources of the graph
     */
    public List<Integer> getSourceList() {
        Collections.shuffle(sourceList);
        return sourceList;
    }

    /**
     * This method return that how many number of vertex this graph object contain.
     * The vertexes map size is the number of total number of vertexes.
     *
     * @return an integer number that represent the total vertexes of the graph.
     */
    public int vertexesNumber() {
        return vertexes.size();
    }

    /**
     * This method return the number of the vertexes that the particular node is connected with.
     *
     * @param node node number.
     * @return the size of the set value from the edges map.
     */
    public int edgeCount(final int node) {
        return edges.get(node).size();
    }

    /**
     * Return the current load shedding of the particular node.
     *
     * @param nodeNumber which node's load shedding will return.
     * @return double value of that node load shedding.
     */
    public double getLoadShedding(final int nodeNumber) {
        return vertexes.get(nodeNumber).getLoadShedding();
    }

    /**
     * Get the power of the source node. Throw an exception if the node is not source node.
     * Otherwise return the power.
     *
     * @throws IllegalArgumentException
     * @param node node that is working on.
     * @return double value of the power source.
     */
    public double getPower(final int node) {
        if (vertexes.get(node).getSupply_demand() < 0) {
            throw new IllegalArgumentException(node + " is not a power source");
        }
        return vertexes.get(node).getPower();
    }

    public static class VertexInfo {
        public final int nodeNumber;
        public final double loadShedding;
        public final double capacity;

        public VertexInfo(int nodeNumber, double loadShedding, double capacity) {
            this.nodeNumber = nodeNumber;
            this.loadShedding = loadShedding;
            this.capacity = capacity;
        }
    }

    private VertexInfo[] vertexInfo;

    private void makeVertexInfoArray(final int node) {
        int edges = edgeCount(node);
        vertexInfo = new VertexInfo[edges];
        Pair[] pairList = this.edges.get(node).toArray(new Pair[edges]);

        for(int i = 0; i < vertexInfo.length; i++) {
            int _nodeNumber = (Integer) pairList[i].first;
            double _loadShedding = getLoadShedding(_nodeNumber);
            double _capacity = (Double) pairList[i].second;
            vertexInfo[i] = new VertexInfo(_nodeNumber, _loadShedding, _capacity);
        }
    }

    public Iterable<VertexInfo> extractVertexInfo(final int node) {
        makeVertexInfoArray(node);
        return new Iterable<VertexInfo>() {
            @Override
            public Iterator<VertexInfo> iterator() {
                return new Iterator<VertexInfo>() {
                    int index = 0;

                    @Override
                    public boolean hasNext() {
                        return index < vertexInfo.length;
                    }

                    @Override
                    public VertexInfo next() {
                        return vertexInfo[index++];
                    }
                };
            }
        };
    }
}
