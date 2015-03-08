import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;


public class Main {

	public static void main(String[] args)  throws IOException {
		//System.setIn(new FileInputStream("in.txt"));
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

class GraphFromInputBuilder{
	
	public static int source = 0;
	
	public static Graph build() throws IOException {
		final int[] pow = new int[] { 1, 10, 100, 1000, 10000, 100000 };
		int x = 0;
		int y = 0;
		int len;
		int index;
		
		
		Graph g;
		String line;

		BufferedReader br;
		br = new BufferedReader(new InputStreamReader(System.in));
		line = br.readLine();
		
		len = line.length();
		index = len - 1;
		while (index > 0 && line.charAt(index) != ' ') {
			char c = line.charAt(index);
			y += (c - '0') * pow[len - index - 1];
			index--;
		}

		len = index;
		index--;
		while (index >= 0) {
			char c = line.charAt(index);
			x += (c - '0') * pow[len - index - 1];
			index--;
		}
		
		g = new Graph(x);
		g.setE(y);
		
		source = Integer.parseInt(br.readLine()) - 1;
		
		while((line = br.readLine()) != null) {
			
			x = 0;
			y = 0;
			
			len = line.length();
			index = len - 1;
			while (index > 0 && line.charAt(index) != ' ') {
				char c = line.charAt(index);
				y += (c - '0') * pow[len - index - 1];
				index--;
			}

			len = index;
			index--;
			while (index >= 0) {
				char c = line.charAt(index);
				x += (c - '0') * pow[len - index - 1];
				index--;
			}
			
			g.addEdge(x - 1, y - 1);
		}
		br.close();
		return g;
	}
}

class ErdosNumber {
	private Graph g;
	private boolean nodeVisited[]; // true - node already visited; false - node not yet visited
	private int distanceTo[]; // distance from node v (index) to the source node
	private int source; // source node for bfs
	private ArrayDeque<Integer> queue;
	private HashMap<Integer, Integer> nodesAtDistance;
	
	private int maxDist; // max Erdos Number
	private int currNode;
	
	public ErdosNumber(Graph g, int source) {
		this.g = g;
		this.source = source;
		nodesAtDistance = new HashMap<Integer, Integer>();
		nodeVisited = new boolean[g.getV()];
		distanceTo = new int[g.getV()];
	}
	
	public void bfs(Graph g, int source) {		
		queue = new ArrayDeque<Integer>();
		
		// TODO: initialise the distanceTo array with infinity?
		// Projects specifications seem to say that all of the nodes
		// have a finite distance. Not doing this saves Theta(V).
		
		distanceTo[source] = 0;
		nodeVisited[source] = true;
		queue.add(source);
		maxDist = 0;
		currNode = 0;
		
		while (queue.peek() != null) {
			currNode = queue.remove();
			if (nodesAtDistance.get(distanceTo[currNode]) == null)
				nodesAtDistance.put(distanceTo[currNode], 1);
			else
				nodesAtDistance.put(distanceTo[currNode], nodesAtDistance.get(distanceTo[currNode]) + 1);
			//numNodesAtDist[distanceTo[u]]++;
			for (int v : g.getAdj(currNode)) {
				if (!nodeVisited[v]) {
					queue.add(v);
					nodeVisited[v] = true;
					distanceTo[v] = distanceTo[currNode] + 1;
				}
			}
		}
		maxDist = distanceTo[currNode];
	}
	
	public void bfs() {
		this.bfs(g, source);
	}
	 
	
	public void printOutput() {
		System.out.println(maxDist);
		
		for (int i = 1; i <= maxDist; i++) {
			System.out.println(nodesAtDistance.get(i));
		}
	}
}

