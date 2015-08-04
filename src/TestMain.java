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
        GraphGenerator graphGenerator = new GraphGenerator();
<<<<<<< Updated upstream
        List<Node> nodeList = graphGenerator.graphGenerator(50, 5000, 350, 60);
        printGraph(1, nodeList, graphGenerator.getLinkNumber());

        Graph graph = new Graph();
//        Graph graph1;
//
        GraphInput reader = new GraphInput();
        graph = reader.readGraph(graph, "input/graph1.txt");
//        graph1 = graph.clone();
//
//        graph1.addResidual(0, 3);
//        graph1.addPower(0, 11);
//        graph.print();
//
//        System.out.println("\n\n");
//
//        graph1.print();
//        graph1.print();

=======
        graphGenerator.graphGenerator(584, 500, 350, 40);
        graphGenerator.generateGraph(graph);
//        graph.printEdge();
>>>>>>> Stashed changes

        StopWatch.start();
        for (int i = 0; i < 10; i++) {
            checkACOV2(graph);
        }
        StopWatch.elapsedTime();
    }

    private static void checkACOV2(Graph graph) {
<<<<<<< Updated upstream
        Run run = new algorithm.aco.multi.source.ACO(100, 10, graph);
=======
        Run run = new algorithm.aco.multi.source.ACO(1000, 10, graph);
        Run run1 = new algorithm.aco.multi.source.ACO(2000, 10, graph);
        Run run3 = new algorithm.aco.multi.source.ACO(3000, 10, graph);
        Run run4 = new algorithm.aco.multi.source.ACO(4000, 10, graph);
>>>>>>> Stashed changes
        run.run();
        run1.run();
        run3.run();
        run4.run();
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

    private static void printGraph(int testCase, List<Node> nodeList, int linkNumber) {
        try {
            String path = "input/";
            String str = "graph";

            PrintWriter out = new PrintWriter(path + str + String.valueOf(testCase) + ".txt");
            out.println(nodeList.size() + " " + linkNumber);
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
