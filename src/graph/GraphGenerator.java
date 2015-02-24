package graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by seal on 2/23/15.
 */
public class GraphGenerator {


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

        int edge = node + (random.nextInt(node / 2) + 1);

        for (int i = 0; i < edge; i++) {
            int u = random.nextInt(node);
            int v = random.nextInt(node);
            if (u == v) {
                i--;
                continue;
            }
            if (u > v) {
                int swap = u;
                u = v;
                v = swap;
            }
            double mean = mean(demandNode, demand);
            int capacity = generateSupplyOrDemand(mean, mean, random);
            nodeList.get(u).setConnectedWith(v);
            nodeList.get(u).setCapacity(capacity);
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
