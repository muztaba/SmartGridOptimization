package graph;

import IOUtils.InputReader;
import graph.version2.Graph;
import graph.vertex.Node;

import java.io.File;
import java.io.IOException;


/**
 * Created by seal on 3/7/15.
 */

/**
 *  This class is responsible for input graph from a file.
 *  First line declare the number of Vertex. and the following line declare the supply or demand.
 *  Positive value mean supply and negative value for demand.
 */
public class GraphInput {
    Graph graph;

    public Graph readGraph(String path) throws IOException {
        InputReader reader = new InputReader(new File("input/graph.txt"));
        this.graph = new Graph();
        int nodeNumber = reader.nextInt();
        int edgeNumber = reader.nextInt();

        for (int i = 0; i < nodeNumber; i++) {
            int use = reader.nextInt();
            graph.vertex.Node node = new Node(i, use);
            graph.addVertex(node);
        }

        for (int i = 0; i < edgeNumber; i++) {
            int u = reader.nextInt();
            int v = reader.nextInt();
            double capacity = reader.nextInt();
            graph.addEdge(u, v, capacity);
        }

        return this.graph;
    }
}
