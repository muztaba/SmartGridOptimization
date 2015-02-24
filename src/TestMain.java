import graph.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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
            generator.graphGenerator(i, 50, 11000, 6000, 40);
        }

    }

    public static void print(int v) throws IOException{
        GraphGenerator generator = new GraphGenerator();
        String path = "/home/seal/IdeaProjects/SmartGridOptimization/output/";
        String str = "g50-";

        PrintWriter out = new PrintWriter(path + str + String.valueOf(v) + ".txt", "UTF-8");
        List<Node> list = generator.graphGenerator(1 ,10, 11000, 6000, 40);

        out.println(list.size());
        for (int i = 0; i < list.size(); i++) {
            out.print(list.get(i).getSupplyOrDemand() + " ");
        }

        out.println();

        for (int i = 0; i < list.size(); i++) {
            List<Edge> edges = list.get(i).edges();
            for (int j = 0; j < edges.size() - 1; j++) {
                out.println(i + "  " + edges.get(j).getConnectedNode() + "  " + edges.get(j).getCapacity());
            }
        }

        out.close();
    }
}
