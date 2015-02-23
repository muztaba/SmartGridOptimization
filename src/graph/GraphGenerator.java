package graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by seal on 2/23/15.
 */
public class GraphGenerator {
    class Node {
        private int nodeNumber = -1;
        private int connectedWith = -1;
        private int supply = 0;
        private int demand = 0;
        private int capacity = 0;

        Node(int nodeNumber, int val) {
            this.nodeNumber = nodeNumber;
            setSupplyOrDemand(val);
        }

        public void setCapacity(int capacity) {
            this.capacity = capacity;
        }

        public void setConnectedWith(int connectedWith) {
            this.connectedWith = connectedWith;
        }

        public void setSupplyOrDemand(int val) {
            if (val == 0) throw new IllegalArgumentException("Must have some value");
            else if (val < 0) setDemand(val);
            else setSupply(val);
        }

        private void setSupply(int supply) {
            this.supply = supply;
        }

        private void setDemand(int demand) {
            this.demand = demand;
        }

    }

    public List<Node> graphGenerator(int node, int supply, int demand, int percentageOf) {
        Random random = new Random();
        List<Node> nodeList = new ArrayList<>();

        int supplyNode = (node * percentageOf) / 100;
        int demandNode = node - supplyNode;

        for(int i = 0; i < node; i++) {
            if (random.nextInt() % 2 == 0) {
                double mean = mean(supplyNode, supply);
                nodeList.add(new Node(i, generateSupplyOrDemand(mean, mean, random)));
            } else {
                double mean = mean(demandNode, demand);
                nodeList.add(new Node(i, generateSupplyOrDemand(mean, mean, random)));
            }
        }

        return nodeList;
    }

    private int generateSupplyOrDemand(double mean, double uc, Random rand) {
        if (rand.nextInt() % 2 == 0) return (int) (mean + (Math.random() * uc));
        else return (int) (mean - (Math.random() * uc));
    }

    private double mean(int n, int numerator) {
        return numerator / n;
    }










}
