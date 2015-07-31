package graph;

import Utils.Pair;
import graph.version2.Graph;

import java.io.Serializable;
import java.util.*;

/**
 * Created by seal on 2/23/15.
 */
public class GraphGenerator {
    List<Node> nodeList = new ArrayList<>();

    public List<Node> graphGenerator(int node, int supply, int demand, int percentageOf) {
        Random random = new Random();

        int supplyNode = (node * percentageOf) / 100;
        int demandNode = node - supplyNode;

        double meanSupply = mean(supplyNode, supply);
        double meanDemand = mean(demandNode, demand);

        for (int i = 0; i < node; i++) {
            if (random.nextInt() % 2 == 0) {
                nodeList.add(new Node(i, generateSupplyOrDemand(meanSupply, meanSupply, random)));
            } else {
                nodeList.add(new Node(i, (-1) * generateSupplyOrDemand(meanDemand, meanDemand, random)));
            }
        }

        int edge = (node / 3) + random.nextInt(node / 2) + 1;
        int avgEdge = (edge / node) + 1;

        // Making a spanning tree therefor there will be no chance that any of those node has no connection
        // with others node

        List<Integer> list = getList(nodeList.size());

        for (int i = 0; !list.isEmpty(); i++) {
            if (i >= nodeList.size()) {
                i = 0;
            }

            int randIndex = random.nextInt(list.size());
            Collections.swap(list, randIndex, list.size() - 1);
            int lastIndexValue = list.get(list.size() - 1);

            if (lastIndexValue == i) continue;

            if (nodeList.get(i).isConnected(lastIndexValue) || nodeList.get(lastIndexValue).isConnected(i)) {
                continue;
            }

            int capacity = generateSupplyOrDemand(meanDemand, meanDemand / 3, random);
            nodeList.get(i).setConnectedWith(lastIndexValue, capacity);
            list.remove(list.size() - 1);
        }

//        for (int i = 0; i < nodeList.size(); i++) {
//            List<Edge> l = nodeList.get(i).edges();
//            for (int j = 0; j < l.size(); j++) {
//                System.out.println(i + " " +   l.get(j).getConnectedNode());
//            }
//            System.out.println();
//        }
//
//        System.out.println("**********************");
//        System.out.println(edge);
        for (int i = 0, t = edge * 3; i < edge && t > 0; i++, t--) {
            int prev = i;
            int u = random.nextInt(node);
            int v = random.nextInt(node);

            if (u == v) {
                i = prev;
                continue;
            }

            if (nodeList.get(u).isConnected(v) || nodeList.get(v).isConnected(u)) {
                i = prev;
                continue;
            }

            if (nodeList.get(u).degree() > avgEdge && nodeList.get(v).degree() > avgEdge) {
                i = prev;
                continue;
            }

            int capacity = generateSupplyOrDemand(meanDemand, meanDemand, random);
            nodeList.get(u).setConnectedWith(v, capacity);
        }

//        for (int i = 0; i < nodeList.size(); i++) {
//            List<Edge> l = nodeList.get(i).edges();
//            for (int j = 0; j < l.size(); j++) {
//                System.out.println(i + " " + l.get(j).getConnectedNode());
//            }
//            System.out.println();
//        }
        return nodeList;
    }

    private int generateSupplyOrDemand(double mean, double uc, Random rand) {
        if (rand.nextInt() % 2 == 0) return (int) (mean + (Math.random() * uc));
        else return (int) (mean - (Math.random() * uc));
    }

    private double mean(int n, int numerator) {
        return numerator / n;
    }


    private List<Integer> getList(int size) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(i);
        }
        return list;
    }

    public void generateGraph(Graph graph) {
        // Sometime there is duplicate link appear due to bug. Therefor this
        // is here to check whether this a duplicate link or not. If duplicate
        // then this link will not add to the graph.
        Set<Pair<Integer, Integer>> duplicateCheck = new HashSet<>();
        // Create vertex and add to the graph.
        for (int i = 0; i < nodeList.size(); i++) {
            double supply_demand = nodeList.get(i).getSupplyOrDemand();
            graph.addVertex(new graph.vertex.Node(i, supply_demand));
        }
        // Add edge between two node along with their link capacity.
        for (int i = 0; i < nodeList.size(); i++) {
            List<Edge> l = nodeList.get(i).edges();
            for (int j = 0; j < l.size(); j++) {
                int v = l.get(j).getConnectedNode();
                Pair<Integer, Integer> pair = Pair.makePair(i, v);
                double capacity = l.get(j).getCapacity();
                // If the capacity will zero then get the connected node's use
                // and assign to the capacity.
                if (capacity  == 0) {
                    capacity = nodeList.get(v).getSupplyOrDemand();
                }
                if (!duplicateCheck.contains(pair)) {
                    graph.addEdge(i, v, capacity);
                    duplicateCheck.add(pair);
                }
            }
        }
    }
}
