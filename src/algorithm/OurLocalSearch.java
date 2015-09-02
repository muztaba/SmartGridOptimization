package algorithm;

import graph.version2.Graph;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by seal on 9/2/15.
 */
public class OurLocalSearch {
    Graph graph;
    Random random = new Random();

    private int currentNode;

    public void initiate(Graph graph, int iteration) {
        this.graph = graph;
        for (int _iteration = 0; _iteration < iteration; _iteration++) {
            currentNode = random.nextInt(graph.vertexesNumber());
            if (graph.getResidual(currentNode) > 0) {
                operationOne();
            }
        }

    }

    private void operationOne() {
        List<Integer> listOutDegree = graph.getOutDegreeList(currentNode);
        if (listOutDegree.size() == 0) {
            return;
        }
        Collections.shuffle(listOutDegree);
        for (int v : listOutDegree) {
            double capacity = graph.getCapacity(currentNode, v);
            double flow = Math.abs(graph.getFlow(currentNode, v)); // outgoing flow represent as negative value
            // ============  DEBUG ===============//
            if (flow > capacity) {
                throw new IllegalArgumentException("violate flow constraint");
            }
            // ====================================//
            double remainFlow = capacity - flow;
            if (remainFlow > 2.0) {
                double residual = graph.getResidual(currentNode);
                double electricity = Math.min(remainFlow, residual) * random.nextDouble();
                graph.addPower(v, electricity);
                graph.addFlow(currentNode, v, electricity);
            }
        }
    }

}
