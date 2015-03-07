import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;


public class Main {

	public static void main(String[] args) {
		Graph g = GraphFromInputBuilder.build();
		System.out.println("V: " + g.getV() + " E: " + g.getE());
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
	
	private static int source = -1;
	
	public static Graph build() {
		Graph g;
		Scanner sc = new Scanner(System.in);
		
		g = new Graph(sc.nextInt());
		g.setE(sc.nextInt());
		
		source = sc.nextInt() - 1;
		
		while(sc.hasNextInt()) {
			g.addEdge(sc.nextInt() - 1, sc.nextInt() - 1);
		}
		return g;
	}
}