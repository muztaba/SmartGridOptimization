package algorithm.aco.multi.source;

import Utils.Pair;
import algorithm.Run;
import algorithm.aco.pheromone.IPheromone;
import algorithm.aco.pheromone.Pheromone;
import graph.version2.Graph;

/**
 * Created by seal on 7/18/15.
 *
 * @author Muztaba Hasanat
 */
public class ACO implements Run {
    private final int iteration;
    private final Graph graph;

    private Ant[] ants;

    private IPheromone pheromone;

    public ACO(int iteration, int antNumber, Graph graph) {
        this.iteration = iteration;
        this.graph = graph;
        this.ants = new Ant[antNumber];
        this.pheromone = new Pheromone(this.graph.vertexesNumber());
        for (int i = 0; i < ants.length; i++) {
            Graph cloneGraph = this.graph.clone();
            ants[i] = new Ant(cloneGraph);
        }
    }

    @Override
    public void run() {
        for (int _iteration = 0; _iteration < iteration; _iteration++) {

            for (int antIndex = 0; antIndex < ants.length; antIndex++) {
                ants[antIndex].initiate(pheromone);
            }

            double minLoadShedding = Double.MAX_VALUE;
            int minLoadSheddingAntIndex = 0;
            for (int antIndex = 0; antIndex < ants.length; antIndex++) {
                double loadShedding = ants[antIndex].getLoadShedding();
                if (loadShedding < minLoadShedding) {
                    minLoadShedding = loadShedding;
                    minLoadSheddingAntIndex = antIndex;
                }
            }
            System.out.println(minLoadShedding);
            pheromoneUpdate(minLoadSheddingAntIndex);
        }
    }

    public static final double EVAPORATION = .25;

    private void pheromoneUpdate(int antIndex) {
        for (Pair<Integer, Integer> itr : ants[antIndex].visitedLink) {
            int u = itr.first;
            int v = itr.second;
            double updatedPheromone = (1 - EVAPORATION) * pheromone.get(u, v);
            pheromone.set(u, v, updatedPheromone);
        }

    }
}
