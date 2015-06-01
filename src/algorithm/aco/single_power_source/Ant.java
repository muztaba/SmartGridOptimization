package algorithm.aco.single_power_source;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Stack;

/**
 * Created by seal on 5/28/15.
 */
public class Ant {
    private double power;
    private int currentNode;
    private boolean powerEmpty;
    private Deque<Integer> tour;

    public Ant() {
        tour = new ArrayDeque<>();
    }

    public void setPower(final double power) {
        this.power = power;
    }

    public int getPower(int demand) {
        if (power - demand <= 0) {
            powerEmpty = true;
            return 0;
        } else {
            // return full power as the nodes demanded.
            // it also update the ant current power.
            power -= demand;
            return demand;
        }
    }

    public void nextNode(final int nextNode) {
        this.currentNode = nextNode;
        tour.addFirst(nextNode);
    }

    /**
     * Negate the powerEmpty boolean variable to notify
     * is the ant moving stop or not.
     * @return negation of the powerEmpty.
     */
    public boolean isMoveStop() {return !this.powerEmpty;}
    public int getCurrentNode() {return this.currentNode;}
}
