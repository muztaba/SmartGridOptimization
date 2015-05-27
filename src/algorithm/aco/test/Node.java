package algorithm.aco.test;

/**
 * Created by seal on 5/25/15.
 */
public class Node {
    private int nodeNumber;
    private int supplyOrDemand;
    private int priority;
    private int capacity;


    public Node(int u, int supplyOrDemand, int priority, int capacity) {
        this.nodeNumber = u;
        this.supplyOrDemand = supplyOrDemand;
        this.priority = priority;
        this.capacity = capacity;
    }
}
