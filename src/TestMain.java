import algorithm.MonteCarlo;
import com.sun.org.apache.bcel.internal.generic.LSTORE;
import graph.Edge;
import graph.GraphGenerator;
import graph.GraphInput;
import graph.Node;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Created by seal on 2/23/15.
 */
public class TestMain {
    public static void main(String[] args) throws IOException{
//        PrintWriter[] out = new PrintWriter[50];
//        for (int i = 0; i < out.length; i++) {
//            out[i] = new PrintWriter(path + str + String.valueOf(i), "UTF-8");
//        }

//        for (int p = 0; p < 50; p++) {

//        }
//        for (int i = 0; i < 50; i++) {
//            print(1);
//        }

        String path = "/home/seal/IdeaProjects/SmartGridOptimization/input/graph.txt";
        String str = "";
//        PrintWriter out = new PrintWriter(path + str + String.valueOf(v) + ".txt", "UTF-8");
        GraphGenerator generator = new GraphGenerator();
        GraphInput input = new GraphInput();
        List<Node> nodeList = input.readGraph(path);
        print(1, nodeList);
        MonteCarlo solve = new MonteCarlo();
        solve.monteCarlo(nodeList, 1000);

    }

    private static void printGraph(List<Node> graph) {
        System.out.println(graph.size());
        for (int i = 0; i < graph.size(); i++) {
            System.out.print(graph.get(i).getSupplyOrDemand());
        }
        System.out.println();
        for (int i = 0; i < graph.size(); i++) {
            for (int j = 0; j < graph.get(i).edges().size(); j++) {
                List<Edge> list = graph.get(i).edges();
                System.out.println(graph.get(i).nodeNumber + " " + list.get(j).getConnectedNode() + " " +
                list.get(j).getCapacity());
            }
        }
    }
    private static void print(int testCase, List<Node> nodeList) {
        try {
            String path = "/home/seal/IdeaProjects/SmartGridOptimization/output/";
            String str = "graphOutput.txt";

            PrintWriter out = new PrintWriter(path + str + String.valueOf(testCase) + ".txt");
            out.println(nodeList.size());
            for (int i = 0; i < nodeList.size(); i++) {
                out.print(nodeList.get(i).getSupplyOrDemand() + " ");
            }
            out.println('\n');
            for (int i = 0; i < nodeList.size(); i++) {
                List<Edge> l = nodeList.get(i).edges();
                for (int j = 0; j < l.size(); j++) {
                    out.println(i + " " + l.get(j).getConnectedNode() + " " + l.get(j).getCapacity());
                }
                out.println();
            }
            out.close();
        } catch (IOException e) {

        }
    }
}
