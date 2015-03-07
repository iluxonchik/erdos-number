import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;


public class Main {

	public static void main(String[] args) {
		Graph g = GraphFromInputBuilder.build();
		//System.out.println("V: " + g.getV() + " E: " + g.getE());
		ErdosNumber en = new ErdosNumber(g, GraphFromInputBuilder.source);
		en.bfs();
		en.printOutput();
	}
}

class Graph {
	
	private int V;
	private int E;
	private LinkedList<Integer> node[];
	
	/**
	 * Instantiates a new graph.
	 *
	 * @param v number of vertices 
	 */
	@SuppressWarnings("unchecked")
	public Graph (int v) {
		// TODO: argument checks
		this.V = v;
		this.E = 0;
		
		// Allocate memory for nodes
		node = (LinkedList<Integer>[]) new LinkedList<?>[v];

		for (int i = 0; i < V; i++) {
			// Create an adjacency list for every node
			node[i] = new LinkedList<Integer>();
		}
	}
	
	public void addEdge(int u, int v) {
		// TODO: argument checks
		node[u].add(v);
		node[v].add(u);
	}
	
	/**
	 * Returns an iterator over the adjacency list of a vertex. 
	 *
	 * @param v vertex to access
	 * @return iterator over the adjacency list of vertex v
	 */
	public Iterable<Integer> getAdj(int v) {
		return node[v];
	}
	
	
	public void setE(int e) {
		this.E = e;
	}
	
	public int getE() { return this.E; }
	public int getV() { return this.V; }
}

class GraphFromInputBuilder {
	
	public static int source = 0;
	
	public static Graph build() {
		Graph g;
		Scanner sc = new Scanner(System.in);
		
		g = new Graph(sc.nextInt());
		g.setE(sc.nextInt());
		
		source = sc.nextInt() - 1;
		
		while(sc.hasNextInt()) {
			g.addEdge(sc.nextInt() - 1, sc.nextInt() - 1);
		}
		sc.close();
		return g;
	}
}

class ErdosNumber {
	private Graph g;
	private boolean nodeVisited[]; // true - node already visited; false - node not yet visited
	private int predecessor[]; // predecessor of node, indexed by node number
	private int distanceTo[]; // distance from node v (index) to the source node
	private int source; // source node for bfs
	private Queue<Integer> queue;
	
	private int numNodesAtDist[]; // number of nodes at distance i from the source (where i is the index)
	private int maxDist; // max Erdos Number
	
	public ErdosNumber(Graph g, int source) {
		this.g = g;
		this.source = source;
		numNodesAtDist = new int[g.getV()];
		nodeVisited = new boolean[g.getV()];
		predecessor = new int[g.getV()];
		distanceTo = new int[g.getV()];
	}
	
	public void bfs(Graph g, int source) {		
		queue = new LinkedList<Integer>();
		
		// TODO: initialise the distanceTo array with infinity?
		// Projects specifications seem to say that all of the nodes
		// have a finite distance. Not doing this saves Theta(V).
		
		distanceTo[source] = 0;
		nodeVisited[source] = true;
		queue.add(source);
		maxDist = 0;
		
		while (queue.peek() != null) {
			int u = queue.remove();
			// Test if current distance is the largest so far
			if (distanceTo[u] > maxDist) {
				maxDist = distanceTo[u];
			}
			numNodesAtDist[distanceTo[u]]++;
			for (int v : g.getAdj(u)) {
				if (!nodeVisited[v]) {
					queue.add(v);
					nodeVisited[v] = true;
					predecessor[v] = u;
					distanceTo[v] = distanceTo[u] + 1;
				}
			}
		}
	}
	
	public void bfs() {
		this.bfs(g, source);
	}
	
	
	public void printOutput() {
		System.out.println(maxDist);
		
		for (int i = 1; i <= maxDist; i++) {
			System.out.println(numNodesAtDist[i]);
		}
	}
}