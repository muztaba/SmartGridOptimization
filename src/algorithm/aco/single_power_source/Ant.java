package algorithm.aco.single_power_source;

import java.util.*;

/**
 * Created by seal on 5/28/15.
 */
public class Ant {
    private double power;
    private int currentNode;
    private boolean powerEmpty;

    private List<Integer> tour;
    private Set<Integer> visited;

    public Ant() {
        tour = new ArrayList<>();
        visited = new HashSet<>();
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

    /**
     * Set the next Node as current node. This way ant move from one node to next node.
     * @param nextNode node that the ant next to move.
     */
    public void nextNode(final int nextNode) {
        this.currentNode = nextNode;
        tour.add(nextNode);
        visited.add(nextNode);
    }

    public boolean isVisitedBefore(final int node) {
        return visited.contains(node);
    }

    /**
     * This method remove the node where the ant currently standing. Then the currentNode has been
     * updated to the last node of it's path, that is the last node of the list array. It behave like back track.
     */
    public void removeFromEnd() {
        tour.remove(tour.size() - 1);
        currentNode = tour.get(tour.size() - 1);
    }

    /**
     * This method return the tour array that is visited by the this ant.
     * @return an integer ArrayDeque that is store the tour.
     */
    public List<Integer> getTour() {return this.tour;}

    /**
     * Negate the powerEmpty boolean variable to notify
     * is the ant moving stop or not.
     * @return negation of the powerEmpty.
     */
    public boolean isMoveStop() {return !this.powerEmpty;}

    public int getCurrentNode() {return this.currentNode;}
    public void setPower(final double power) {
        this.power = power;
    }
}
