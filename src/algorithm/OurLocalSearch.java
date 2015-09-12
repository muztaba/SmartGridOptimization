package algorithm;

import graph.version2.Graph;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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
            /*Assertion*/
            assert (graph.getResidual(currentNode) < 0) : "Residual must be positive";

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
            double remainFlow = graph.getCapacity(currentNode, v);
            if (remainFlow > 0.0) {
                double residual = graph.getResidual(currentNode);
                double flow = Math.min(remainFlow, residual) * random.nextDouble();
                double validFlow = graph.checkFlowConstraint(currentNode, v, flow);
                if (validFlow < 0) {
                    flow -= Math.abs(validFlow);
                    graph.addResidual(this.currentNode, Math.abs(validFlow));
                }
                graph.addPower(v, flow);
                graph.addFlow(currentNode, v, flow);
                graph.addResidual(currentNode, -flow);
//                graph.validationCheck();
            }
        }
    }

}
