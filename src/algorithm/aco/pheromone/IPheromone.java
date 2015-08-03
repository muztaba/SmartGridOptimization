package algorithm.aco.pheromone;

/**
 * Created by seal on 7/19/15.
 */
public interface IPheromone {
    double get(final int u, final int v);

    void set(final int u, final int v, final double pheromoneValue);

    void reset(double resetValue);
}
