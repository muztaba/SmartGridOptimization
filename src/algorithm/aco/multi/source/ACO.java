package algorithm.aco.multi.source;

import Utils.Pair;
import algorithm.OurLocalSearch;
import algorithm.Run;
import algorithm.aco.pheromone.IEvaporation;
import algorithm.aco.pheromone.IPheromone;
import algorithm.aco.pheromone.Pheromone;
import com.sun.org.apache.bcel.internal.generic.DLOAD;
import graph.version2.Graph;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        this.pheromone = new Pheromone(this.graph.vertexesNumber(), 1);
        for (int i = 0; i < ants.length; i++) {
            Graph cloneGraph = this.graph.clone();
            ants[i] = new Ant(cloneGraph);
        }
    }

    @Override
    public void run() {
        double prevLoadShedding = 0.0;
        int threshold = 20;
        int count = 0;

        for (int _iteration = 0; _iteration < iteration; _iteration++) {
            for (int antIndex = 0; antIndex < ants.length; antIndex++) {
                ants[antIndex].initiate(pheromone);
            }
            double minQuality = Double.MAX_VALUE;
            int minQualityAntIndex = 0;

            for (int antIndex = 0; antIndex < ants.length; antIndex++) {
                double quality = ants[antIndex].quality();
                if (quality < minQuality) {
                    minQuality = quality;
                    minQualityAntIndex = antIndex;
                }
            }
            pheromoneUpdate(minQualityAntIndex);
            double minLoadShedding = ants[minQualityAntIndex].getTotalLoadShedding();

            if (prevLoadShedding == minLoadShedding) {
                count++;
            } else {
                prevLoadShedding = minLoadShedding;
            }
            if (count == threshold) {
                pheromone.reset(1);
                count = 0;
            }
        }
    }


    public static final double EVAPORATION = .75;

    private void pheromoneUpdate(int antIndex) {
        // Evaporation
        pheromone.evaporation(new IEvaporation() {
            @Override
            public double evaporation(double oldPheromone) {
                return (1 - EVAPORATION) * oldPheromone;
            }
        });

        // Update pheromone
        for (Pair<Integer, Integer> itr : ants[antIndex].visitedLink) {
            int u = itr.first;
            int v = itr.second;
            double updatePheromone = pheromone.get(u, v) + pheromone.get(u, v);
            pheromone.set(u, v, updatePheromone);
        }
    }
}
