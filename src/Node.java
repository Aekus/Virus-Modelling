import java.util.HashSet;

public class Node {
    /* initializes node to state healthy and day 0 */
    public Node() {
        state = "healthy";
        day = 0;
    }

    /* adds an edge from this to n */
    public boolean addEdge(Node n) {
        if (connections.contains(n)) {
            return false;
        }
        connections.add(n);
        return true;
    }

    /* changes the state of m from healthy to incubating and adds m to list of infected nodes */
    public void infect(Node m) {
        if (m.state.equals("healthy")) {
            infectedNodes.add(m);
            m.state = "incubation";
        }
    }

    /* returns boolean if this has infected m */
    public boolean infected(Node m) {
        return infectedNodes.contains(m);
    }

    /* for all connections, removes edge from both nodes */
    public void quarantine() {
        for (Node n : connections) {
            n.connections.remove(this);
        }
        connections.clear();
    }

    HashSet<Node> connections = new HashSet<Node>();
    HashSet<Node> infectedNodes = new HashSet<Node>();
    int day;
    String state;
}
