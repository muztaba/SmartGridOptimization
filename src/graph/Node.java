package graph;

import graph.Edge;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by seal on 2/24/15.
 */
public class Node {
    private List<Edge> connectedWith;

    public int nodeNumber = -1;
    private double supply = 0;
    private double demand = 0;
    private int capacity = 0;

    public List<Edge> edges() {
        return connectedWith;
    }
    public int edge(int index) {return connectedWith.get(index).getConnectedNode();}

    Node(int nodeNumber, double use) {
        this.nodeNumber = nodeNumber;
        connectedWith = new ArrayList<>();
        setSupplyOrDemand(use);
    }

    public void setConnectedWith(int connectedWith, int capacity) {
        this.connectedWith.add(new Edge(connectedWith, capacity));
    }

    public void setSupplyOrDemand(double val) {
//        if (val == 0) throw new IllegalArgumentException("Must have some value");
        if (val < 0) setDemand(val);
        else setSupply(val);
    }

    public double getSupplyOrDemand() {
        if (supply != 0) return supply;
        else return demand;
    }

    public double getSupply() {
        return supply;
    }

    public double getDemand() {
        return demand;
    }

    public int degree() {
        return connectedWith.size();
    }

    private void setSupply(double supply) {
        this.supply = supply;
    }

    private void setDemand(double demand) {
        this.demand = demand;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public boolean isConnected(int index) {
        return connectedWith.contains(index);
    }
}
