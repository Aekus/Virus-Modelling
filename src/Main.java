import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;

import java.util.HashSet;

public class Main {
    /* Create graph of size 10000 */
    static RandomGraph graph = new RandomGraph(10000);
    /* Probability of quarantining infected node. If implementing linear quarantine function, this is probability on day 0 */
    static double RESPONSE_RATE = 0.05;
    /* Growth constant if implementing linear quarantine function */
    static final double RESPONSE_RATE_GROWTH = 0.00188889;
    /* probability of infecting connected nodes */
    static final double INFECTION_RATE = 0.20;
    /* contact tracing probability */
    static final double TRACING_RATE = 0.80;

    /* Running main will simply run a simulation of size 10000 for 200 days and print the distribution on every day */
    public static void main(String[] args) {
        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        setHost();
        for (int i = 0; i < 200; i++) {
            simulate(i+1);
            report();
            RESPONSE_RATE += RESPONSE_RATE_GROWTH;
        }
    }

    /* Produces Visualization */
    private static void display() {
        Graph vizgraph = new SingleGraph("");
        for (Node n : graph.nodeList) {
            vizgraph.addNode(n.toString());
            vizgraph.getNode(n.toString()).addAttribute("ui.class", n.state);
        }
        for (Node n : graph.nodeList) {
            for (Node m : n.connections) {
                String edgeID = n.toString() + m.toString();
                String firstID = n.toString();
                String secondID = m.toString();
                try {
                    vizgraph.addEdge(edgeID, firstID, secondID);
                    if (n.infected(m) || m.infected(n)) {
                        vizgraph.getEdge(edgeID).addAttribute("ui.class", "infected");
                    }
                } catch (Exception exception) {
                }
            }
        }
        vizgraph.addAttribute("ui.stylesheet",
                "edge.infected { fill-color: red; }" +
                "node { size: 5px; }" +
                "node.healthy { fill-color: black; }" +
                "node.incubation { fill-color: purple; }" +
                "node.infected { fill-color: red; }" +
                "node.recovered { fill-color: green; }");
        vizgraph.display();
    }

    /* Outputs current distribution of nodes for all states.
     */
    private static void report() {
        int healthyCount = 0, incubationCount = 0, infectedCount = 0, recoveredCount = 0;
        for (Node n : graph.nodeList) {
            if (n.state.equals("healthy")) {
                healthyCount++;
            } else if (n.state.equals("incubation")) {
                incubationCount++;
            } else if (n.state.equals("infected")) {
                infectedCount++;
            } else if (n.state.equals("recovered")) {
                recoveredCount++;
            }
        }
        System.out.format("%d%10d%10d%10d", healthyCount, incubationCount, infectedCount, recoveredCount);
        System.out.println();
    }

    /* Randomly assigns a node as patient zero */
    private static void setHost() {
        int index = (int) (Math.random() * graph.nodeList.length);
        graph.nodeList[index].state = "incubation";
    }

    /* Simulates one day of virus */
    private static void simulate(int totalDay) {
        for (Node n : graph.nodeList) {
            if (n.state.equals("infected") || n.state.equals("incubation")) {
                /* changing states for infected and incubation nodes if they exceed their timeline */
                if (n.day >= 5 && n.state.equals("incubation")) {
                    n.state = "infected";
                }
                if (n.day >= 19 && n.state.equals("infected")) {
                    n.state = "recovered";
                }
                /* infects every connection with probability INFECTION_RATE independently */
                for (Node c : n.connections) {
                    if (Math.random() < INFECTION_RATE) {
                        n.infect(c);
                    }
                }
                /* isolates infected nodes with probability RESPONSE_RATE */
                if (n.state.equals("infected")) {
                    if (Math.random() < RESPONSE_RATE) {
                        /* Contact tracing protocol */
                        if (totalDay > 40) {
                            /* must create a copy of since Node.quarantine() is a destructive method */
                            HashSet<Node> connectionsCopy = (HashSet<Node>) n.connections.clone();
                            for (Node m : connectionsCopy) {
                                if (Math.random() < TRACING_RATE) {
                                    m.quarantine();
                                }
                            }
                        }
                        n.quarantine();
                    }
                }
                n.day++;
            }
        }
    }

    /* prints number of edges for each node */
    private static void reportCount() {
        for (Node n : graph.nodeList) {
            System.out.println(n.connections.size());
        }
    }
}