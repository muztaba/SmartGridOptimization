import algorithm.Gibbs;
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
//        printGraph(1, new GraphGenerator().graphGenerator(5, 500, 300, 40));
        GraphInput in = new GraphInput();
        List<Node> graph = in.readGraph("/home/seal/IdeaProjects/SmartGridOptimization/output/graphOutput.txt1.txt");
        checkGibbs(1, graph);
    }

    private static void checkGibbs(int testCase, List<Node> graph) {
        Gibbs solver = new Gibbs();
        solver.gibbs(graph, 100);
    }

    private static void printGraph(int testCase, List<Node> nodeList) {
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
