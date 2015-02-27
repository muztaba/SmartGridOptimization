import graph.Edge;
import graph.GraphGenerator;
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

        String path = "/home/seal/IdeaProjects/SmartGridOptimization/output/";
        String str = "g50-";
//        PrintWriter out = new PrintWriter(path + str + String.valueOf(v) + ".txt", "UTF-8");
        for (int i = 0; i < 50; i++) {
            GraphGenerator generator = new GraphGenerator();
            List<Node> nodeList = generator.graphGenerator(50, 11000, 6000, 40);
            print(i, nodeList);
        }

    }

    private static void print(int testCase, List<Node> nodeList) {
        try {
            String path = "/home/seal/IdeaProjects/SmartGridOptimization/output/";
            String str = "g50-";

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
