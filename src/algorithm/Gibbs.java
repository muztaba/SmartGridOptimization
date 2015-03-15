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

    Undo undo;

    class Undo {
        int i, j;
        int froward, backward;

        Undo(int i, int j, int froward, int backward) {
            this.i = i;
            this.j = j;
            this.froward = froward;
            this.backward = backward;
        }
    }

    public void gibbs(List<Node> graph, int iteration) {
        // Initializing the variable.
        initVariable(graph.size());

        initConnectionMatrix(graph);

        // For the starting up Gibbs algorithm this method provide a valid graph
        // that satisfy the flow constraints.
        validGraph(graph);

        // Here begin the Gibbs algorithm.
        for (int mainLoop = 0; mainLoop < iteration; mainLoop++) {
            // Make a single change in the flowMatrix.
            makeChange(graph);
            if (!checkValidity(graph)) {
                undo();
            }
            int totalLoadShedding = totalLoadShedding(graph);
            System.out.println(totalLoadShedding);
        }
    }

    /**
     * Give the Gibbs algorithm a valid graph. This method randomly generate flow then check the constraints.
     * If the graph stable with the generated flow then it return from the method to further proceed.
     * This give the Gibbs algorithm a valid graph for the start up.
     */
    protected void validGraph(List<Node> graph) {
        while (true) {

            // Generating flow between two node randomly within their edge capacity
            randomFlowGeneration(graph);

            // If there is a valid graph with flow constraint then return from this method to further proceed.
            if (checkValidity(graph)) return;
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

                undo = new Undo(i, j, flowMatrix[i][j], flowMatrix[j][i]);

                break;

            }
        }
    }

    /**
     * If the graph is not valid with the generated flow then this method undo the all change have been made
     * in the flowMatrix.
     */
    protected void undo() {
        flowMatrix[undo.i][undo.j] = undo.froward;
        flowMatrix[undo.j][undo.i] = undo.backward;
    }
}
