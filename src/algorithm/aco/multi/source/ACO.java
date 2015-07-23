package algorithm.aco.multi.source;

import Utils.Pair;
import algorithm.Run;
import algorithm.aco.pheromone.IPheromone;
import algorithm.aco.pheromone.Pheromone;
import graph.version2.Graph;

import java.util.Set;

/**
 * Created by seal on 7/18/15.
 */
public class ACO implements Run {
    private final int iteration;
    private final int antNumber;
    private final Graph graph;

    private Ant[] ants;

    private IPheromone pheromone;

    public ACO(int iteration, int antNumber, Graph graph) {
        this.iteration = iteration;
        this.antNumber = antNumber;
        this.graph = graph;
        this.ants = new Ant[antNumber];
        this.pheromone = new Pheromone(this.graph.vertexesNumber());
        for (int i = 0; i < ants.length; i++) {
            // Put code here !!
        }

        ants[0] = new Ant(graph);
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
//            System.out.println(minLoadSheddingAntIndex);
            pheromoneUpdate(minLoadSheddingAntIndex);
        }
    }

    public static final double EVAPORATION = .25;

    private void pheromoneUpdate(int antIndex) {
//        System.out.println(ants[antIndex].visitedLink.size());
        for (Pair<Integer, Integer> itr : ants[antIndex].visitedLink) {
            int u = itr.first;
            int v = itr.second;
//            System.out.println(u + " " + v);
            double updatedPheromone = (1 - EVAPORATION) * pheromone.get(u, v);
//            System.out.println(updatedPheromone);
//            System.out.println("HI");
            pheromone.set(u, v, updatedPheromone);
        }

    }
}
