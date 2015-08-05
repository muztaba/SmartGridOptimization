package algorithm.aco.multi.source;

import Utils.Pair;
import algorithm.Run;
import algorithm.aco.pheromone.IEvaporation;
import algorithm.aco.pheromone.IPheromone;
import algorithm.aco.pheromone.Pheromone;
import graph.version2.Graph;

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
        List<Double> ll = new ArrayList<>();
        //====== DEBUG ======//
        double prevLoadShedding = 0.0;
        int minAntIndex = 0;
        //===================//
        for (int _iteration = 0; _iteration < iteration; _iteration++) {

            for (int antIndex = 0; antIndex < ants.length; antIndex++) {
                ants[antIndex].initiate(pheromone);
                //=====DEBUG======//
//                System.out.println(ants[antIndex].getVisitedNodeNumber());
//                System.out.println(ants[antIndex].getLoadShedding());
//                System.out.println();
                //================//
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
            pheromoneUpdate(minLoadSheddingAntIndex);

            //===========DEBUG==========//
//            System.out.println(minLoadShedding);
            ll.add(minLoadShedding);
//            minAntIndex = minLoadSheddingAntIndex;
//            if (_iteration % 10 == 0) {
//                System.out.println("\n");
//            }
//            System.out.print("Iteration : " + _iteration + " Min Load Shedding :" + minLoadShedding);
//            if (minLoadShedding < prevLoadShedding) {
//                System.out.print(" * ");
//            }
//            System.out.println();
//            prevLoadShedding = minLoadShedding;
//            System.out.println("Visited Node Number : " + ants[minLoadSheddingAntIndex].getVisitedNodeNumber());
            //========================//
        }

        //===========DEBUG==========//
        Collections.sort(ll);
//        System.out.println();
//        ants[minAntIndex].printVisitedLink();
//        ants[minAntIndex].printGraph();
//        ants[minAntIndex].printVisitedNode();
//        System.out.println();
//        System.out.println(ants[minAntIndex].getVisitedNodeNumber());
//        System.out.println();
        System.out.println("Min : " + ll.get(0) + " Max : " + ll.get(ll.size()- 1));
        //========================//

    }

    public static final double EVAPORATION = .5;

    private void pheromoneUpdate(int antIndex) {

        // Evaporation

        pheromone.evaporation(new IEvaporation() {
            @Override
            public double evaporation(double oldPheromone) {
                return (1 - .5) * oldPheromone;
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
