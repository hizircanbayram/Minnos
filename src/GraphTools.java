import java.util.*;

public class GraphTools {


    public class Edge {
        // Data Fields
        private int source;

        private int dest;

        private double weight;

        public Edge(int source, int dest) {
            this.source = source;
            this.dest = dest;
            weight = 1.0;
        }

        public Edge(int source, int dest, double w) {
            this.source = source;
            this.dest = dest;
            weight = w;
        }

        public int getSource() {
            return source;
        }

        public int getDest() {
            return dest;
        }

        public double getWeight() {
            return weight;
        }

        public String toString() {
            StringBuffer sb = new StringBuffer("[(");
            sb.append(Integer.toString(source));
            sb.append(", ");
            sb.append(Integer.toString(dest));
            sb.append("): ");
            sb.append(Double.toString(weight));
            sb.append("]");
            return sb.toString();
        }

        public boolean equals(Object obj) {
            if (obj instanceof Edge) {
                Edge edge = (Edge) obj;
                return (source == edge.source
                        && dest == edge.dest);
            } else {
                return false;
            }
        }

        public int hashCode() {
            return (source << 16) ^ dest;
        }

    }

    public interface Graph {


        int getNumV();


        boolean isDirected();


        void insert(Edge edge);


        boolean isEdge(int source, int dest);


        Edge getEdge(int source, int dest);


        Iterator<Edge> edgeIterator(int source);

    }

    public abstract class AbstractGraph implements Graph {

        private int numV;

        private boolean directed;


        public AbstractGraph(int numV, boolean directed) {
            this.numV = numV;
            this.directed = directed;
        }


        public int getNumV() {
            return numV;
        }


        public boolean isDirected() {
            return directed;
        }


    }

    public class ListGraph extends AbstractGraph {

        private List<Edge>[] edges;


        public ListGraph(int numV, boolean directed) {
            super(numV, directed);
            edges = new List[numV];
            for (int i = 0; i < numV; i++) {
                edges[i] = new LinkedList<>();
            }
        }


        public boolean isEdge(int source, int dest) {
            return edges[source].contains(new Edge(source, dest));
        }

        public void insert(Edge edge) {
            edges[edge.getSource()].add(edge);
            if (!isDirected()) {
                edges[edge.getDest()].add(new Edge(edge.getDest(),
                        edge.getSource(),
                        edge.getWeight()));
            }
        }

        public Iterator<Edge> edgeIterator(int source) {
            return edges[source].iterator();
        }


        public Edge getEdge(int source, int dest) {
            Edge target =
                    new Edge(source, dest, Double.POSITIVE_INFINITY);
            for (Edge edge : edges[source]) {
                if (edge.equals(target))
                    return edge;
            }

            return target;
        }


    }

    private Graph createCraph(ArrayList<ArrayList<Cell>> cellArray) {
        int rowCount = cellArray.size();
        int colCount = cellArray.get(0).size();
        ArrayList<Edge> edges = new ArrayList<>();


        for (int i = 0; i < cellArray.size(); i++) {
            for (int j = 0; j < cellArray.get(i).size(); j++) {
                Cell current = cellArray.get(i).get(j);
                if (current.isCharacter()) start = (colCount * i) + j;
                if (current.isFinish()) end = (colCount * i) + j;


                if (!current.isTree() && !current.isBLock()) {

                    if (j+1<10 && (cellArray.get(i).get(j + 1).isPath() || cellArray.get(i).get(j + 1).isFinish() || cellArray.get(i).get(j + 1).isCharacter()) && !current.equals(cellArray.get(i).get(j + 1))) {
                        edges.add(new Edge((colCount * i) + j, (colCount * i) + j + 1, 1));
                        if (cellArray.get(i).get(j + 1).isCharacter()) start = (colCount * i) + j + 1;
                        if (cellArray.get(i).get(j + 1).isFinish()) end = (colCount * i) + j + 1;
                    }

                    if (i+1<7 && (cellArray.get(i + 1).get(j).isPath() || cellArray.get(i + 1).get(j).isFinish() || cellArray.get(i + 1).get(j).isCharacter()) && !current.equals(cellArray.get(i + 1).get(j))) {
                        edges.add(new Edge((colCount * i) + j, (colCount * (i + 1)) + j, 1));
                        if (cellArray.get(i + 1).get(j).isCharacter()) start = (colCount * (i + 1)) + j;
                        if (cellArray.get(i + 1).get(j).isFinish()) end = (colCount * (i + 1)) + j;
                    }


                }
            }
        }
        Graph g = new ListGraph(70, false);

        for (Edge e : edges) {
            g.insert(e);
        }

        return g;
    }

    private int start;
    private int end;
    public ArrayList<Cell> calculateShortestPath(ArrayList<ArrayList<Cell>> cellArray) {
        Graph g = createCraph(cellArray);


        int pred[] = new int[g.getNumV()];
        double dist[] = new double[g.getNumV()];
        dijkstrasAlgorithm(g, start, pred, dist);

        Stack<Integer> vert = new Stack<>();
        while (end != -1) {
            vert.add(end);
            if (end == start) break;
            end = pred[end];
        }
        ArrayList<Cell> shortestPath = new ArrayList<>();
        while (!vert.isEmpty()) {
            int current = vert.pop();
            int x = (current - (current % 10)) / 10;
            int y = current - (x * 10);
            shortestPath.add(new Cell(x, y, CellType.PATH));
        }


        return shortestPath;
    }

    public static void dijkstrasAlgorithm(Graph graph, int start, int[] pred, double[] dist) {
        int numV = graph.getNumV();
        HashSet<Integer> vMinusS = new HashSet<Integer>(numV);
        // Initialize V–S.
        for (int i = 0; i < numV; i++) {
            if (i != start) {
                vMinusS.add(i);
            }
        }
        // Initialize pred and dist.
        for (int v : vMinusS) {
            pred[v] = start;
            dist[v] = graph.getEdge(start, v).getWeight();
        }
        // Main loop
        while (vMinusS.size() != 0) {
            // Find the value u in V–S with the smallest dist[u].
            double minDist = Double.POSITIVE_INFINITY;
            int u = -1;
            for (int v : vMinusS) {
                if (dist[v] <= minDist) {
                    minDist = dist[v];
                    u = v;
                }
            }
            // Remove u from vMinusS.
            vMinusS.remove(u);
            // Update the distances.
            for (int v : vMinusS) {
                if (graph.isEdge(u, v)) {
                    double weight = graph.getEdge(u, v).getWeight();
                    if (dist[u] + weight < dist[v]) {
                        dist[v] = dist[u] + weight;
                        pred[v] = u;
                    }
                }
            }
        }
    }




}
