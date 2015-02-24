package graph;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by seal on 2/24/15.
 */
public class Node {
    private int nodeNumber = -1;
    private List<Edge> connectedWith;
    private int supply = 0;
    private int demand = 0;
    private int capacity = 0;

    public List<Edge> edges() {
        return connectedWith;
    }

    public int degree() {
        return connectedWith.size();
    }

    public boolean isConnected(int index) {
        return connectedWith.contains(index);
    }

    Node(int nodeNumber, int val) {
        this.nodeNumber = nodeNumber;
        connectedWith = new ArrayList<>();
        setSupplyOrDemand(val);
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setConnectedWith(int connectedWith, int capacity) {
        this.connectedWith.add(new Edge(connectedWith, capacity));
    }

    public void setSupplyOrDemand(int val) {
//        if (val == 0) throw new IllegalArgumentException("Must have some value");
         if (val < 0) setDemand(val);
        else setSupply(val);
    }

    public int getSupplyOrDemand() {
        if (supply != 0) return supply;
        else return demand;
    }

    private void setSupply(int supply) {
        this.supply = supply;
    }

    private void setDemand(int demand) {
        this.demand = demand;
    }

}
