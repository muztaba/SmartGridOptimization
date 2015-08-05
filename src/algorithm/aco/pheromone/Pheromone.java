package algorithm.aco.pheromone;

import Utils.ArrayUtils;

/**
 * Created by seal on 7/19/15.
 */
public class Pheromone implements IPheromone{
    private double[][] pheromoneTrail;

    public Pheromone(int vertexes) {
        this.pheromoneTrail = new double[vertexes][vertexes];
        init(1.0);
    }

    public Pheromone(int vertexes, double fillValue) {
        this.pheromoneTrail = new double[vertexes][vertexes];
        init(fillValue);
    }

    @Override
    public void evaporation(IEvaporation evaporationMethod) {
        for (int i = 0; i < pheromoneTrail.length; i++) {
            for (int j = 0; j < pheromoneTrail[i].length; j++) {
                double oldPheromone = get(i, j);
                double newPheromone = evaporationMethod.evaporation(oldPheromone);
                set(i, j, newPheromone);
            }
        }
    }

    private void init(double fillValue) {
        ArrayUtils.fill(pheromoneTrail, fillValue);
    }

    @Override
    public void set(final int u, final int v, final double pheromoneValue) {
        pheromoneTrail[u][v] = pheromoneValue;
    }

    @Override
    public double get(final int u, final int v) {
        return pheromoneTrail[u][v];
    }

    @Override
    public void reset(double resetValue) {
        init(resetValue);
    }
}
