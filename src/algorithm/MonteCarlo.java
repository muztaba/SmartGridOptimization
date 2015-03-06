package algorithm;
import graph.*;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by seal on 2/25/15.
 */
public class MonteCarlo {
    int[][] flowMatrix;
    boolean[][] connectionMatrix;
    int[] markMatrix;     //marking where a node is demand or supply; 0 for supply and 1 for demand.
    Random random;

    public void monteCarlo(List<Node> graph, int iteration) {
        initVariable(graph.size());

        for (int i = 0; i < iteration; i++) {

            // Initialize connectionMatrix with the connection between node.
            initConnectionMatrix(graph);
            // Fill the flowMatrix with random value between 0 < x < capacity. [x = flow node i to node j]
            randomFlowGeneration(graph);
            
            // Check validity the flow of the matrix. graph parameter provide the supply or demand of the node.
            if (checkValidity(graph)) {
                int graphTotalLoadShedding = totalLoadShedding(graph);
                System.out.println(graphTotalLoadShedding);
            }

        }

    }

    private int totalLoadShedding(List<Node> graph) {
        int totalLoadShedding = 0;
        for (int i = 0; i < flowMatrix.length; i++) {
            if (markMatrix[i] == 1) {
                int[] row = flowMatrix[i];
                int incoming, outgoing; incoming = outgoing = 0;

                for (int j : row) {
                    if (j < 0) outgoing += j;
                    else incoming += j;
                }
                totalLoadShedding += (incoming + outgoing + graph.get(i).getDemand());
            }
        }

        return totalLoadShedding;
    }

    /**
     * checkValidity method check where all the flow is possible. This method return boolean.
     */
    private boolean checkValidity(List<Node> graph) {
        int i = 0;
        for(int[] row : flowMatrix) {
            if (markMatrix[i] == 0) {
                int supply = graph.get(i).getSupply();
                if (!checkValiditySupply(row, supply)) return false;
            } else {
                int demand = graph.get(i).getDemand();
                if (!checkValidityDemand(row, demand)) return false;
            }
            i++;
        }
        return true;
    }

    /**
     *  This method check for the demand condition. That is the total outgoing flow must be
     *  smaller than the total incoming. Therefor the total incoming and outgoing summation
     *  is greater than 0.
     */
    private boolean checkValidityDemand(int[] row, int demand) {
        int incoming, outgoing; incoming = outgoing = 0;

        for (int i : row) {
            if (i > 0) incoming += i;
            else outgoing += i;
        }

        return ((incoming + outgoing) > 0) ? true : false;
    }

    /**
     * This method check for the supply condition. That is total incoming and it self generated
     * supply should be greater then is outgoing.
     */
    private boolean checkValiditySupply(int[] row, int selfGeneration) {
        int incoming, outgoing; incoming = outgoing = 0;

        for (int i : row) {
            if (i > 0) incoming += i;
            else outgoing += i;
        }

        return (incoming + selfGeneration >= Math.abs(outgoing)) ? true : false;
    }

    /**
     * randomFlowGeneration method fill the flowMatrix with flow. The flow of
     * between two will randomly generate.
     */
    private void randomFlowGeneration(List<Node> graph) {
        for (int i = 0; i < flowMatrix.length; i++) {
            List<Edge> listOfEdge = graph.get(i).edges();
            for (int j = 0; j < flowMatrix[i].length; j++) {
                if (connectionMatrix[i][j]) {
                    int index = indexOf(listOfEdge, j);
                    if (listOfEdge.get(index).getCapacity() <= 0) continue;
                    int randomValue = random.nextInt(listOfEdge.get(index).getCapacity());
                    flowMatrix[i][j] = randomValue;
                    flowMatrix[j][i] = -randomValue;
                }
            }
        }
    }

    private int indexOf(List<Edge> list, int val) {
        int i;
        for (i = 0; i < list.size(); i++) {
            if (list.get(i).getConnectedNode() == val) return i;
        }
        return -1;
    }


    /**
     * connectionMatrix is boolean 2-D matrix where store the connection between
     * two node. To initialize the boolean matrix we get the connection from the
     * graph where each node has the containers to hold the connection to others
     * node. It also fill the markMatrix for determine where the node is supply or
     * demand. 0 for supply and 1 for demand.
     */
    private void initConnectionMatrix(List<Node> graph) {
        for (int i = 0; i < graph.size(); i++) {
            Node node = graph.get(i);
            markMatrix[i] = (isDemandOrSupply(node.getSupplyOrDemand()) == 0) ? 0 : 1;
            for (int j = 0; j < node.edges().size(); j++) {
                int connectedWith = node.edges().get(j).getConnectedNode();
                connectionMatrix[i][connectedWith] = true;
//                connectionMatrix[connectedWith][i] = true;
            }
        }
    }

    /**
     * Initialize all member variable;
     */
    private void initVariable(int size) {
        flowMatrix = new int[size][size];
        connectionMatrix = new boolean[size][size];
        markMatrix = new int[size];
        random = new Random();

    }
    
    /**
     *  isDemandOrSupply method determine the node is demand node or supply node. To do this
     *  method check the return value of the node's getSupplyOrDemand. If positive then it is
     *  supply node otherwise demand node. This method return 0 for the supply and 1 for the demand.
     */
    private int isDemandOrSupply(int val) {
        return (val > 0) ? 0 : 1;
    }
}
