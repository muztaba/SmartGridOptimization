package algorithm;
import graph.*;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by seal on 2/25/15.
 */
public class MonteCarlo extends Algorithm {


    public void monteCarlo(List<Node> graph, int iteration) {
        initVariable(graph.size());
        // Initialize connectionMatrix with the connection between node.
        initConnectionMatrix(graph);

        for (int i = 0; i < iteration; i++) {


            // Fill the flowMatrix with random value between 0 < x < capacity. [x = flow node i to node j]
            randomFlowGeneration(graph);
            
            // Check validity the flow of the matrix. graph parameter provide the supply or demand of the node.
            if (checkValidity(graph)) {
                // If the graph stable with the generated flow then calculate the total load shedding of the demand
                // node.
                int graphTotalLoadShedding = totalLoadShedding(graph);
                System.out.println(graphTotalLoadShedding);
            }

        }

    }
}
