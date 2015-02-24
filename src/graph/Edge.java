package graph;

/**
 * Created by seal on 2/24/15.
 */
public class Edge {
        private int connectedNode = -1;
        private int capacity = 0;

        Edge (int node, int capacity) {
            this.connectedNode = node;
            this.capacity = capacity;
        }

        public int getCapacity() {
            return capacity;
        }

        public int getConnectedNode() {
            return connectedNode;
        }
}
