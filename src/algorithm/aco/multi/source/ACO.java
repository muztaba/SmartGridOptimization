package algorithm.aco.multi.source;

import algorithm.Run;
import algorithm.aco.pheromone.IPheromone;
import algorithm.aco.pheromone.Pheromone;
import graph.version2.Graph;

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
        this.pheromone = new Pheromone(graph.vertexesNumber());
        for (int i = 0; i < ants.length; i++) {
            // Put code here !!
        }
    }

    @Override
    public void run() {
        for (int _iteration = 0; _iteration < iteration; _iteration++) {
            for (int antIndex = 0; antIndex < antNumber; antIndex++) {

            }
        }
    }
}
