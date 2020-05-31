import java.util.ArrayList;
import java.util.HashSet;

public class RandomGraph {
    public RandomGraph(int n) {
        nodeList = new Node[n];
        createNodes(n);
        initialize(n);
    }

    /* Create n nodes */
    private void createNodes(int n) {
        for(int i = 0; i < nodeList.length; i++) {
            nodeList[i] = new Node();
        }
    }

    /* create edges between nodes */
    private void initialize(int n) {
        for(int i = 0; i < nodeList.length; i++) {
            int j = (int) (Math.ceil(Math.sqrt(draw(n))));
            j++;
            for (int k = 0; k < j; k++) {
                stubList.add(nodeList[i]);
            }
        }
        randomPairing();
    }

    /* randomly pairs elements of stubList, ensuring that they are not from the same node and that no multiedges are formed */
    private void randomPairing() {
        for (int i = 0; i < stubList.size(); i++) {
            int index = (int) (Math.random() * stubList.size());
            Node firstNode = stubList.get(i);
            Node secondNode = stubList.get(index);
            if (!firstNode.equals(secondNode) &&
                    !firstNode.connections.contains(secondNode)) {
                firstNode.addEdge(secondNode);
                secondNode.addEdge(firstNode);
                stubList.remove(firstNode);
                stubList.remove(secondNode);
                i--;
                if (index < i) {
                    i--;
                }
            }
        }
    }

    /* draws a value k from power-law distribution pk */
    private int draw(int n) {
        double p  = Math.random();
        int x = 1;
        double CDF = 0;
        while(true) {
            /* Complexity can be decreased by incrementing by 2^n, but time wasn't a bottleneck so I didn't bother */
            CDF += (Math.pow(x,-A))/RIEMANN_VALUE;
            if (p < Math.sqrt(CDF)) {
                return x;
            }
            x++;
        }
    }
    /* Value for riemann-zeta function with argument A */
    private final double RIEMANN_VALUE = 1.644934;
    /* constant A */
    private final double A = 2.0;
    ArrayList<Node>  stubList = new ArrayList<>();
    HashSet<Node[]> edgeSet = new HashSet<Node[]>();
    Node[] nodeList;
}
