package graph.vertex;

/**
 * Created by seal on 7/13/15.
 */
public class Node {
    private final int node;
    private final int supply_demand;
    // keep the use
    private double use;
    // If this variable > 0 then there is load shedding and < 0 then residual.
    private double loadShedding;

    public Node(int node, double use) {
        this.node = node;
        this.use = use;
        this.supply_demand = (this.use < 0) ? -1 : 1;
    }
    
    public void addElectricity(double electricity) {

    }

    public double getLoadShedding() {
        return loadShedding;
    }
}
