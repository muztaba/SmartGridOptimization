package algorithm;
import graph.*;

import java.util.List;

/**
 * Created by seal on 2/25/15.
 */
public class MonteCarlo {
    int[][] flowMatrix;
    boolean[][] connectionMatrix;

    public void monteCarlo(List<Node> nodeList) {
        flowMatrix = new int[nodeList.size()][nodeList.size()];
        connectionMatrix = new boolean[nodeList.size()][nodeList.size()];

        // Initialize connectionMatrix with the connection between node.
        initConnectionMatrix(nodeList);

        //


    }


    /**
     * connectionMatrix is boolean 2-D matrix where store the connection between
     * two node. To initialize the boolean matrix we get the connection from the
     * nodeList where each node has the containers to hold the connection to others
     * node.
     */
    private void initConnectionMatrix(List<Node> list) {
        for (int i = 0; i < list.size(); i++) {
            Node node = list.get(i);
            for (int j = 0; j < node.edges().size(); j++) {
                int connectedWith = node.edges().get(j).getConnectedNode();
                connectionMatrix[i][connectedWith] = true;
                connectionMatrix[connectedWith][i] = true;
            }
        }
    }
}
