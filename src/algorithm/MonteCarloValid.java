package algorithm;

import graph.Node;
import graph.Edge;

import java.util.List;

/**
 * Created by seal on 3/11/15.
 */
public class MonteCarloValid extends Algorithm {

    public void monteCarloValid(List<Node> graph, int iteration) {
        initVariable(graph.size());

        initConnectionMatrix(graph);


    }

    @Override
    protected void randomFlowGeneration(List<Node> graph) {
        for (int i = 0; i < flowMatrix.length; i++) {
            List<Edge> listOfEdge = graph.get(i).edges();
            if (markMatrix[i] == 0) {
                for (int j = 0; j < flowMatrix[i].length; j++) {

                }
            }
        }
    }
}
