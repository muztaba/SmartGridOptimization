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
        init();
    }

    /**
     * Randomly given electricity to a node is subtract from the previous load shedding.
     * If the load shedding value negative then the node has residual electricity. Otherwise
     * there is still load shedding.
     * @param electricity
     */
    public void addElectricity(double electricity) {
        loadShedding = loadShedding - electricity;
    }

    public void reset() {
        init();
    }

    private void init() {
        this.loadShedding = (supply_demand < -1) ? Math.abs(use) : 0;
    }

    public double getLoadShedding() {
        return Math.abs(loadShedding);
    }
}
