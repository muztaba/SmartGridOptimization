import graph.*;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by seal on 2/23/15.
 */
public class TestMain {
    public static void main(String[] args) {
        GraphGenerator solve = new GraphGenerator();
        PrintWriter out = new PrintWriter(System.out);
        List<Node> list = solve.graphGenerator(30, 11000, 6000, 40);

        out.println(list.size());
        for (int i = 0; i < list.size(); i++) {
            out.print(list.get(i).getSupplyOrDemand() + " ");
        }

        for (int i = 0; i < list.size(); i++) {
            List<Integer> edges = list.get(i).edges();
            for (int j = 0; j < edges.size() - 1; j++) {
                out.println(i + edges.get(i));
            }
        }


    }
}
