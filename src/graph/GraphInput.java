package graph;

/**
 * Created by seal on 3/7/15.
 */

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 *  This class is responsible for input graph from a file.
 *  First line declare the number of Vertex. and the following line declare the supply or demand.
 *  Positive value mean supply and negative value for demand.
 */
public class GraphInput {
    List<Node> graph;

    public List<Node> readGraph(String path) throws IOException {
        InputReader in = new InputReader(new File(path));
        System.out.println("Starting graph input");
        int nodeNumber = in.nextInt();
        int edges = in.nextInt();
        this.graph = new ArrayList<>(nodeNumber);

        for (int i = 0; i < nodeNumber; i++) {
            double use = in.nextDouble();
            graph.add(new Node(i, use));
        }

        for (int i = 0; i < edges; i++) {
            int u = in.nextInt();
            int v = in.nextInt();
            int capacity = in.nextInt();
            graph.get(u).setConnectedWith(v, capacity);
        }
        System.out.println("Finish input");
        return graph;
    }

    class InputReader {
        private BufferedReader reader;
        public StringTokenizer tokenizer;

        InputReader(File file) {
            try {
                reader = new BufferedReader(new FileReader(file));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            tokenizer = null;
        }

        InputReader(InputStream stream) {
            reader = new BufferedReader(new InputStreamReader(stream));
            tokenizer = null;
        }

        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(reader.readLine());
                } catch (Exception e) {

                }
            }
            return tokenizer.nextToken();
        }

        public String nextLine() {
            String str = "";
            try {
                str = reader.readLine();
            } catch (IOException e) {
                throw new RuntimeException();
            }
            return str;
        }

        public boolean hasMoreTokens() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                String str = null;
                try {
                    str = reader.readLine();
                } catch (IOException e) {
                    throw new RuntimeException();
                }
                if (str == null) return false;
                tokenizer = new StringTokenizer(str);
            }
            return true;
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }

        public double nextDouble() {
            return Double.parseDouble(next());
        }

        public long nextLong() {
            return Long.parseLong(next());
        }
    }

}
