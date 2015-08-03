package graph.vertex;

import java.io.Serializable;

/**
 * Created by seal on 7/13/15.
 */
public class Node implements Serializable {
    private final int node;
    private final int supply_demand;
    // keep the use
    private final double use;
    // How much electric power this node has that is providing to the other node.
    private double power;
    // If this variable > 0 then there is load shedding and < 0 then residual.
    private double residual;
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
     * @param electricity given electricity will add to the node.
     */
    public void addElectricity(double electricity) {
        loadShedding = loadShedding - electricity;
    }

    public void reset() {
        init();
    }

    private void init() {
        this.power = (supply_demand > 0) ? Math.abs(use) : 0;
        this.loadShedding = (supply_demand < 0) ? Math.abs(use) : 0;
        this.residual = 0;
    }

    /**
     * Return the current load shedding of this node. This method return the absolute value of the load shedding.
     * If the load shedding value is positive that means there is still a load shedding. Otherwise if negative
     * then there is a residual in this node. In this model both is considered as a load shedding.
     *
     * @return an absolute double value of the load shedding.
     */
    public double getLoadShedding() {
        return Math.abs(loadShedding);
    }
    public double getPower() {
        return this.power;
    }
    public void setPower(final double power) {
        this.power = power;
    }
    public int getNodeNumber() {
        return node;
    }
    public int getSupply_demand() {
        return supply_demand;
    }
    public double getUse() {
        return this.use;
    }

    public double getResidual() {
        return residual;
    }

    public void setResidual(double residual) {
        this.residual = residual;
    }

    @Override
    public String toString() {
        return "Node{" +
                "node=" + node +
                ", supply_demand=" + supply_demand +
                ", use=" + use +
                ", power=" + power +
                ", residual=" + residual +
                ", loadShedding=" + loadShedding +
                '}';
    }
}
