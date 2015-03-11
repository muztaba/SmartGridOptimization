package algorithm;
import graph.*;

/**
 * Created by seal on 3/11/15.
 */

import java.util.List;

/**
 * Gibbs algorithm is like the MonteCarlo but every time it does not start the whole algorithm
 * again. Rather it make a small change in the sample.
 */
public class Gibbs extends Algorithm {
    public void gibbs(List<Node> graph, int iteration) {
        // Initializing the variable.
        initVariable(graph.size());

        initConnectionMatrix(graph);
        for (int mainLoop = 0; mainLoop < iteration; mainLoop++) {
            randomFlowGeneration(graph);

            // This make sure that the given graph is valid.
            if (!checkValidity(graph)) continue;
            for (int i = 0; i < iteration; i++) {
                makeChange(graph);
                if (!checkValidity(graph)) break;
                int totalLoadShedding = totalLoadShedding(graph);
                System.out.println(totalLoadShedding);
            }
            System.out.println();
        }
    }

    /**
     * Make a single change in the flowMatrix.
     */
    protected void makeChange(List<Node> graph) {
        int breakVariable = 100;
        while (breakVariable-- > 0) {
            int i = random.nextInt(connectionMatrix.length);
            int j = random.nextInt(connectionMatrix.length);
            if (connectionMatrix[i][j]) {
                int index = indexOf(graph.get(i).edges(), j);
                int randomCapacity = random.nextInt(graph.get(i).edges().get(index).getCapacity());
                flowMatrix[i][j] = -randomCapacity;
                flowMatrix[j][i] = randomCapacity;
                break;
            }
        }
    }
}
