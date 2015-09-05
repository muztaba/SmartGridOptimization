package graph.version2;

import IOUtils.InputReader;
import Utils.ArrayUtils;
import Utils.Pair;
import com.sun.istack.internal.NotNull;
import graph.vertex.Node;

import java.io.*;
import java.util.*;

/**
 * Created by seal on 7/13/15.
 *
 * @author Muztaba Hasanat
 */
public class Graph implements Serializable {
    private int totalVertex;
    private int totalEdge;

    private Map<Integer, Node> vertexes = new HashMap<>();
    private Map<Integer, Set<Pair<Integer, Double>>> edges = new HashMap<>();

    // Keep the list of the sources.
    private Set<Integer> sourceList = new HashSet<>();
    // Out degree and and the in degree of a node.
    Map<Integer, Degree> degreeMap = new HashMap<>();

    private double[][][] flowMatrix;

    public static final int CAPACITY = 1;
    public static final int FLOW = 0;

    private static class Degree {
        public int inDegree;
        public int outDegree;
        public List<Integer> inDegreeList = new ArrayList<>();
        public List<Integer> outDegreeList = new ArrayList<>();

        public int totalDegree() {
            return inDegree + outDegree;
        }

    }

    public void setDegreeMap(int out, int in) {
        if (degreeMap.get(out) == null) {
            degreeMap.put(out, new Degree());
        }
        if (degreeMap.get(in) == null) {
            degreeMap.put(in, new Degree());
        }
        degreeMap.get(out).outDegree++;
        degreeMap.get(out).outDegreeList.add(in);
        degreeMap.get(in).inDegree++;
        degreeMap.get(in).inDegreeList.add(out);
    }

    public int degreeUse(int node) {
        return degreeMap.get(node) == null ? 0 : degreeMap.get(node).totalDegree();
    }

    public List<Integer> getOutDegreeList(int node) {
        List<Integer> listOutDegree;
        if (degreeMap.get(node) == null || degreeMap.get(node).outDegreeList == null) {
            listOutDegree = new ArrayList<>();
        } else {
            listOutDegree = new ArrayList<>(degreeMap.get(node).outDegreeList);
        }
        return listOutDegree;
    }

    public double getFlow(int u, int v) {
        double flow = flowMatrix[u][v][FLOW];
        // ======== DEBUG =======//
        if (flow > 0) {
            throw new IllegalArgumentException("+ : This is incoming flow");
        }
        // =====================//
        return flow;
    }

    /**
     * This method return the how many degree or edges is used so far for the target vertex.
     *
     * @param node target vertex.
     * @return how may edges or degrees has used or occupied.
     */
    public int degreeUsed(int node) {
        int totalEdge = edges.get(node).size();
        // If the degree return null then there is no edge has visited so far. So it return 0
        int usedEdge = (degreeMap.get(node) == null) ? 0 : degreeMap.get(node).totalDegree();
        int unusedEdge = totalEdge - usedEdge;
        // ===== Debug ======//
        if (unusedEdge < 0) {
            throw new IllegalArgumentException("Unused edge should not be negative");
        }
        //===================//
        return unusedEdge;
    }

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
     * Set the capacity u -> v node. It is bidirectional connection.
     *
     * @param u    from where to leave flow.
     * @param v    to where to get the flow
     * @param flow that is between the node.
     * @return this object.
     */
    public Graph setFlow(int u, int v, final double flow) {
        flowMatrix[u][v][FLOW] = -flow;
        flowMatrix[v][u][FLOW] = flow;
        return this;
    }

    public Graph addFlow(int u, int v, final double flow) {
        flowMatrix[u][v][FLOW] += -flow;
        flowMatrix[v][u][FLOW] += flow;
        return this;
    }

    /**
     * This method add edge between two node. 'u' is one node and 'v' is another node
     * and the capacity of the node. Make a pair with 'v' and capacity and added to a
     * edges map. Because of the graph bidirectional therefore make two pair with
     * capacity with 'u' and capacity with 'v'. That is u->v and v->u.
     *
     * @param u        first node number.
     * @param v        second node number.
     * @param capacity capacity of the edge.
     */
    public void addEdge(int u, int v, double capacity) {
        if (flowMatrix == null) {
            initFlowMatrix();
        }
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
        flowMatrix[u][v][CAPACITY] = capacity;
        flowMatrix[v][u][CAPACITY] = capacity;
    }

    /**
     * It creates a new object of the flow matrix that is initialize with 0.
     */
    private void initFlowMatrix() {
        flowMatrix = new double[totalVertex][totalVertex][2];
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
     * Set the total vertex of the graph that will contain.
     *
     * @param nodeNumber how many vertex there will be.
     * @return this object
     */
    public Graph setVertexNumber(final int nodeNumber) {
        totalVertex = nodeNumber;
        return this;
    }

    /**
     * Set the total edge of the graph that will contain.
     *
     * @param edgeNumber how many edge there will be.
     * @return this object
     */
    public Graph setEdgeNumber(final int edgeNumber) {
        totalEdge = edgeNumber;
        return this;
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
     * This method calculate the load shedding of the target node
     * at that time, and return the calculate value.
     *
     * @param node target node.
     * @return load shedding at that time.
     */
    public double currentDemand(int node) {
        double actualDemand = Math.abs(vertexes.get(node).getUse());
        double currentLoadShedding = vertexes.get(node).getLoadShedding();
        double currentDemand = actualDemand - currentLoadShedding;
        // ===== Debug ======//
        if (currentDemand < 0) {
            throw new IllegalArgumentException("Load shedding is grater then demand");
        }
        //===================//
        return currentDemand;
    }

    /**
     * This method return the power that generate by the source node.
     * If the node that has passed to the method as parameter is node
     * source node the method throw IllegalArgumentException.
     *
     * @param node node that is working on.
     * @return double value of the power source.
     * Id node has no power then return 0.
     * @throws IllegalArgumentException if the given node is not source node.
     */
    public double getPower(final int node) {
        if (!isSourceNode(node)) {
            throw new IllegalArgumentException("Node should be source node");
        }

        return vertexes.get(node).getPower();
    }

    /**
     * This method return the demand of the node that containing maximum value.
     *
     * @return maxDemand. The maximum value of the demand of the graph.
     */
    public double getMaxDemand() {
        double maxDemand = Double.MIN_VALUE;
        for (Map.Entry<Integer, Node> itr : vertexes.entrySet()) {
            if (itr.getValue().getSupply_demand() > 1) {
                continue;
            }
            double demand = itr.getValue().getLoadShedding();
            if (demand > maxDemand) {
                maxDemand = demand;
            }
        }
        return maxDemand;
    }

    /**
     * This method return the residual electricity in the node.
     *
     * @param node target node.
     * @return the residual electricity.
     */
    public double getResidual(final int node) {
        return vertexes.get(node).getResidual();
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
     * @param node     particular node where the residual power will be kept.
     * @param residual residual power.
     * @see Node setLeftPower method.
     */
    public void addResidual(final int node, final double residual) {
        vertexes.get(node).addResidual(residual);
    }

    /**
     * Set specific value in a node residual value.
     *
     * @param node     target node.
     * @param residual specific value.
     * @return current graph object.
     */
    public Graph setResidual(final int node, final double residual) {
        vertexes.get(node).setResidual(residual);
        return this;
    }

    /**
     * Set the given electricity to the particular node. After give the power
     * to the node validation check.
     *
     * @param node  particular node where power is given.
     * @param power given power.
     * @see Node addElectricity method.
     */
    public void addPower(int node, double power) {
        vertexes.get(node).addElectricity(power);
        validationCheck(node);
    }

    /**
     * Calculate the total load shedding of this graph. Iterate over
     * every node and get the load shedding and residual power or whatever
     * we are considering as load shedding and then sum up every thing.
     * <p>
     * Noted that this method calculate the total load shedding of this graph.
     * Not the individual load shedding of node.
     *
     * @return total load shedding of this graph.
     */
    public double calculateTotalLoadShedding() {
        double loadShedding = 0.0;
        for (Map.Entry<Integer, Node> itr : vertexes.entrySet()) {
            loadShedding += itr.getValue().getLoadShedding();
        }
        return loadShedding;
    }

    public double calculateTotalResidual() {
        double residual = 0.0;
        for (Map.Entry<Integer, Node> itr : vertexes.entrySet()) {
            residual += itr.getValue().getResidual();
        }
        return residual;
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

        for (int i = 0; i < vertexInfo.length; i++) {
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

    // ========== Graph Constraint Check ========//

    private boolean validationCheck(int node) {
        double[][] row = flowMatrix[node];
        double totalFlow = 0.0;
        for (int i = 0; i < row[0].length; i++) {
            if (row[i][CAPACITY] >= row[i][FLOW]) {
                totalFlow += row[i][FLOW];
            } else {
                throw new IllegalArgumentException("Capacity constraint violation"
                        + "Capacity " + row[i][CAPACITY] + " Flow " + row[i][FLOW]);
            }
        }
        if (totalFlow >= 0) {
            double residual = vertexes.get(node).getResidual();
            double electricity = vertexes.get(node).getElectricity();
            if (totalFlow - (residual + electricity) >= .1) {
                System.out.println(node + "******");
                System.out.println(totalFlow);
                System.out.println(residual);
                System.out.println(electricity);
                System.out.println();
//                throw new IllegalArgumentException("Flow constraint violation, Flow : " + totalFlow
//                        + " residual + electricity " + (residual + electricity));
            }
        } else {
            throw new IllegalArgumentException("Flow constraint violation, Flow : " + totalFlow);
        }
        return true;
    }

    //===========================================//

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

    Map<Integer, Integer> visitedNumber = new HashMap<>();

    public void setVisitedNumber(int node) {
        if (visitedNumber.get(node) == null) {
            visitedNumber.put(node, 1);
        } else {
            visitedNumber.put(node, visitedNumber.get(node) + 1);
        }
    }

    public void printDegree(Set<Integer> visited) {
        System.out.println(vertexes.size() == visited.size() ? "All Visited" : visited.size());
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        for (int i = 0; i < vertexes.size(); i++) {
            if (isSourceNode(i)) {
                System.out.println("Node " + i + "*");
                System.out.println("Visited       " + (visited.contains(i) ? "true" : "false"));
                System.out.println("Visited Time  " + (visitedNumber.get(i) == null ? "0" : visitedNumber.get(i)));
                System.out.println("Use           " + vertexes.get(i).getUse());
                System.out.println("Total Degree  " + edges.get(i).size());
                System.out.println("Degree Use    " + degreeUse(i));
                if (degreeMap.get(i) == null) {
                    System.out.println(0);
                } else {
                    System.out.print("In Degree     " + (degreeMap.get(i).inDegree) + " [ ");
                    for (int d : degreeMap.get(i).inDegreeList) {
                        System.out.print(d + " ");
                    }
                    System.out.println(" ]");
                    System.out.print("Out Degree    " + (degreeMap.get(i).outDegree) + " [ ");
                    for (int d : degreeMap.get(i).outDegreeList) {
                        System.out.print(d + " ");
                    }
                }
                System.out.println(" ]");
                System.out.println("Residual      " + getResidual(i));
            } else {
                System.out.println("Node " + i);
                System.out.println("Visited       " + (visited.contains(i) ? "true" : "false"));
                System.out.println("Visited Time  " + (visitedNumber.get(i) == null ? "0" : visitedNumber.get(i)));
                System.out.println("Use           " + vertexes.get(i).getUse());
                System.out.println("Total Degree  " + edges.get(i).size());
                System.out.println("Degree Use    " + degreeUse(i));
                if (degreeMap.get(i) == null) {
                    System.out.println(0);
                } else {
                    System.out.print("In Degree     " + (degreeMap.get(i).inDegree) + " [ ");
                    for (int d : degreeMap.get(i).inDegreeList) {
                        System.out.print(d + " ");
                    }
                    System.out.println(" ]");
                    System.out.print("Out Degree    " + (degreeMap.get(i).outDegree) + " [ ");
                    for (int d : degreeMap.get(i).outDegreeList) {
                        System.out.print(d + " ");
                    }
                }
                System.out.println(" ]");
                System.out.println("Residual      " + getResidual(i));
                System.out.println("Load Shedding " + getLoadShedding(i));
                System.out.println("Total         " + (getLoadShedding(i) + getResidual(i)));
            }
            System.out.println();
        }
    }

    public void printEdge() {
        for (Map.Entry<Integer, Set<Pair<Integer, Double>>> itr : edges.entrySet()) {
            for (Pair<Integer, Double> link : itr.getValue()) {
                System.out.println(itr.getKey() + " " + link.first + " " + link.second);
            }
            System.out.println();
        }
    }
}
