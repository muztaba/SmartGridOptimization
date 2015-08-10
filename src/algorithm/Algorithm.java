package algorithm;

import graph.Edge;
import graph.Node;

import java.util.List;
import java.util.Random;

/**
 * Created by seal on 3/11/15.
 */
public class Algorithm {
    protected double[][] flowMatrix;
    protected boolean[][] connectionMatrix;
    protected int[] markMatrix;     //marking where a node is demand or supply; 0 for supply and 1 for demand.
    protected Random random;

    /**
     * Initialize all member variable;
     */
    protected void initVariable(int size) {
        flowMatrix = new double[size][size];
        connectionMatrix = new boolean[size][size];
        markMatrix = new int[size];
        random = new Random();

    }

    /**
     * connectionMatrix is boolean 2-D matrix where store the connection between
     * two node. To initialize the boolean matrix we get the connection from the
     * graph where each node has the containers to hold the connection to others
     * node. It also fill the markMatrix for determine where the node is supply or
     * demand. 0 for supply and 1 for demand.
     */
    protected void initConnectionMatrix(List<Node> graph) {
        for (int i = 0; i < graph.size(); i++) {
            Node node = graph.get(i);
            markMatrix[i] = (isDemandOrSupply(node.getSupplyOrDemand()) == 0) ? 0 : 1;
            for (int j = 0; j < node.edges().size(); j++) {
                int connectedWith = node.edges().get(j).getConnectedNode();
                connectionMatrix[i][connectedWith] = true;
            }
        }
    }

    /**
     *  isDemandOrSupply method determine the node is demand node or supply node. To do this
     *  method check the return value of the node's getSupplyOrDemand. If positive then it is
     *  supply node otherwise demand node. This method return 0 for the supply and 1 for the demand.
     */
    protected int isDemandOrSupply(double val) {
        return (val > 0) ? 0 : 1;
    }


    /**
     * randomFlowGeneration method fill the flowMatrix with flow. The flow of
     * between two will randomly generate.
     */
    protected void randomFlowGeneration(List<Node> graph) {
        for (int i = 0; i < flowMatrix.length; i++) {
            List<Edge> listOfEdge = graph.get(i).edges();
            for (int j = 0; j < flowMatrix[i].length; j++) {
                if (connectionMatrix[i][j]) {
                    int index = indexOf(listOfEdge, j);
                    if (listOfEdge.get(index).getCapacity() <= 0) continue;
                    double randomValue = (double)random.nextInt(listOfEdge.get(index).getCapacity());
                    flowMatrix[i][j] = -randomValue;
                    flowMatrix[j][i] = randomValue;
                }
            }
        }
    }

    /**
     * Find the index of the j-th value, that is connected with the 'i' in the flowMatrix.
     * This method return the position of the given value 'i' otherwise return -1.
     */
    protected int indexOf(List<Edge> list, int val) {
        int i;
        for (i = 0; i < list.size(); i++) {
            if (list.get(i).getConnectedNode() == val) return i;
        }
        return -1;
    }

    /**
     * checkValidity method check where all the flow is possible. This method return boolean.
     */
    protected boolean checkValidity(List<Node> graph) {
        int i = 0;
        for(double[] row : flowMatrix) {
            if (markMatrix[i] == 0) {
                double supply = graph.get(i).getSupply();
                if (!checkValiditySupply(row, supply)) return false;
            } else {
                double demand = graph.get(i).getDemand();
                if (!checkValidityDemand(row, demand)) return false;
            }
            i++;
        }
        return true;
    }

    /**
     * This method check for the supply condition. That is total incoming and it self generated
     * supply should be greater then is outgoing.
     */
    protected boolean checkValiditySupply(double[] row, double selfGeneration) {
        double total = 0.0;

        for (double i : row) total += i;

        return (total + selfGeneration) >= 0.0;
    }

    /**
     *  This method check for the demand condition. That is the total outgoing flow must be
     *  smaller than the total incoming. Therefor the total incoming and outgoing summation
     *  is greater than 0.
     */
    protected boolean checkValidityDemand(double[] row, double demand) {
        double total = 0.0;

        for (double i : row) total += i;

        return total >= 0.0;
    }

    /**
     * Calculate the total load shedding of the demand node and return the value in negative form because the demand
     * in the negative from. We assume that there is no load shedding in the supply node.
     */
    protected double totalLoadShedding(List<Node> graph) {
        double totalLoadShedding = 0.0;
        for (int i = 0; i < flowMatrix.length; i++) {
            if (markMatrix[i] == 1) {
                double total = 0.0;

                for (double j : flowMatrix[i]) total += j;

                totalLoadShedding += (total + graph.get(i).getDemand());
            }
        }

        return totalLoadShedding;
    }


}
