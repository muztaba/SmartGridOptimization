import Utils.StopWatch;
import algorithm.Gibbs;
import algorithm.MonteCarlo;
import algorithm.Run;
import algorithm.aco.single_power_source.ACO;
import graph.Edge;
import graph.GraphGenerator;
import graph.GraphInput;
import graph.Node;
import graph.version2.Graph;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Created by seal on 2/23/15.
 */
public class TestMain {
    public static void main(String[] args) throws IOException {
//        printGraph(1, new GraphGenerator().graphGenerator(50, 500, 350, 40));
        Graph graph = new Graph();
        GraphGenerator graphGenerator = new GraphGenerator();
        graphGenerator.graphGenerator(500, 500, 350, 40);
        graphGenerator.generateGraph(graph);
//        graph.printEdge();

        StopWatch.start();
        checkACOV2(graph);
        StopWatch.elapsedTime();
    }

    private static void checkACOV2(Graph graph) {
        Run run = new algorithm.aco.multi.source.ACO(1000, 50, graph);
        run.run();
    }

    private static void checkNewGraph(Graph graph) {
        graph.print();
    }

    private static void checkACO(int testCase, List<Node> graph) {
        Run solver = new ACO(graph, 0);
        solver.run();
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
