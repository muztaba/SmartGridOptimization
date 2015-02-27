package algorithm;
import graph.*;

import java.util.List;

/**
 * Created by seal on 2/25/15.
 */
public class MonteCarlo {
    int[][] matrix;
    public void monteCarlo(List<Node> nodeList) {
        matrix = new int[nodeList.size()][nodeList.size()];

        // Initialize matrix with the capacity of the link.
        initMatrix(nodeList);

    }


    /**
     * Initialize the matrix with capacity of the link between two node for i to j
     * and j to i keep the negative value. Positive incoming link and negative means
     * outgoing connection.
     * @param list
     */
    private void initMatrix(List<Node> list) {
        for (int i = 0; i < list.size(); i++) {
            Node node = list.get(i);

            for (int j = 0; j < node.edges().size(); j++) {
                int connectedNode = node.edges().get(i).getConnectedNode();
                matrix[i][connectedNode] = node.edges().get(i).getCapacity();
                matrix[connectedNode][i] = node.edges().get(i).getCapacity();
            }
        }

    }
}
