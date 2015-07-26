package graph.version2;

import IOUtils.InputReader;
import Utils.Pair;
import graph.vertex.Node;

import java.io.*;
import java.util.*;

/**
 * Created by seal on 7/13/15.
 * @author Muztaba Hasanat
 */
public class Graph implements Serializable{
    private Map<Integer, Node> vertexes = new HashMap<>();
    private Map<Integer, Set<Pair<Integer, Double>>> edges = new HashMap<>();

    // Keep the list of the sources.
    private Set<Integer> sourceList = new HashSet<>();

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

    /**
     * This method add edge between two node. 'u' is one node and 'v' is another node
     * and the capacity of the node. Make a pair with 'v' and capacity and added to a
     * edges map. Because of the graph bidirectional therefore make two pair with
     * capacity with 'u' and capacity with 'v'. That is u->v and v->u.
     *
     * @param u first node number.
     * @param v second node number.
     * @param capacity capacity of the edge.
     */
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
     * This method return the all sources as Set data structure. Ensure that there is no duplicate.
     *
     * @return Set of integer of the sources of this graph containing.
     */
    public Set<Integer> getSourceList() {
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
     * Get the power of the source node or demand node that has been left.
     * If the node has no power or left power then the method return 0.
     *
     * @param node node that is working on.
     * @return double value of the power source.
     *          Id node has no power then return 0.
     */
    public double getPower(final int node) {
        double power = 0.0;
        if (vertexes.get(node).getSupply_demand() < 0) {
            power = vertexes.get(node).getLeftPower();
        } else {
            power = vertexes.get(node).getPower();
        }
        return power;
    }

    /**
     * This method return the link capacity between tow node 'u' and 'v'. The direction of the node is u -> v.
     * Iterate over the edges and if u and v have the link then it will return the capacity between link.
     * And if the there is  no link that is, u == null of v not match with the integer value from the set
     * then the method will return -1.
     *
     * @param u from node.
     * @param v to node.
     * @return capacity if there is a link otherwise -1.
     */
    public double getCapacity(final int u, final int v) {
        Set<Pair<Integer, Double>> set = edges.get(u);
        if (set == null) {
            return -1;
        }
        for (Pair<Integer, Double> pair : set) {
            if (pair.first == v) {
                return pair.second;
            }
        }
        return -1;
    }

    /**
     * Check if the given node is supply node. If supply node then return true, otherwise false.
     *
     * @param node given node that will be checked.
     * @return if supply node then True, otherwise False.
     */
    public boolean isSourceNode(final int node) {
        return sourceList.contains(node);
    }

    /**
     * Reset node's temporary variable that keeps changes in the graph for one state.
     * Iterate over the vertexes map and call the node's internal reset method.
     *
     * @see Node reset method.
     */
    public void reset() {
        for (Map.Entry<Integer, Node> itr : vertexes.entrySet()) {
            itr.getValue().reset();
        }
    }

    /**
     * Set the residual electricity to the particular node that is given to the method.
     * This residual electricity will keep in the Node's value.
     *
     * @see Node setLeftPower method.
     * @param node particular node where the residual power will be kept.
     * @param residual residual power.
     */
    public void addResidual(final int node, final double residual) {
        vertexes.get(node).setLeftPower(residual);
    }

    /**
     * Set the given electricity to the particular node.
     *
     * @see Node addElectricity method.
     * @param node particular node where power is given.
     * @param power given power.
     */
    public void addPower(int node, double power) {
        vertexes.get(node).addElectricity(power);
    }

    /**
     * Calculate the total load shedding of this graph. Iterate over
     * every node and get the load shedding and residual power or whatever
     * we are considering as load shedding and then sum up every thing.
     *
     * Noted that this method calculate the total load shedding of this graph.
     * Not the individual load shedding of node.
     *
     * @return total load shedding of this graph.
     */
    public double calculateLoadShedding() {
        double loadShedding = 0.0;
        for (Map.Entry<Integer, Node> itr : vertexes.entrySet()) {
            loadShedding += itr.getValue().getLoadShedding();
            loadShedding += itr.getValue().getLeftPower();
        }
        return loadShedding;
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
    //================CLONING====================//
    //===========================================//

    public Graph clone() {
        serializeObject();
        return deSerializeObject();
    }

    private void serializeObject() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("src/graph/objSerialize.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOutputStream);
            out.writeObject(this);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Graph deSerializeObject() {
        Graph cloneObject = null;
        try {
            FileInputStream fileInputStream = new FileInputStream("src/graph/objSerialize.ser");
            ObjectInputStream in = new ObjectInputStream(fileInputStream);
            cloneObject = (Graph) in.readObject();
            in.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cloneObject;
    }

    //===========================================//
    //==================DEBUG====================//
    //===========================================//

    public void print() {
        for (Map.Entry<Integer, Node> itr : vertexes.entrySet()) {
            System.out.println(itr.getValue());
        }
    }
}
