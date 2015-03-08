import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;


public class Main {

	public static void main(String[] args)  throws IOException {
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
		Graph g;
		String rawLine;
		String[] line;
		BufferedReader br;
		//Scanner sc = new Scanner(System.in);
		br = new BufferedReader(new InputStreamReader(System.in));
		line = br.readLine().split(" ");
		g = new Graph(Integer.parseInt(line[0]));
		g.setE(Integer.parseInt(line[0]));
		
		source = Integer.parseInt(br.readLine()) - 1;
		
		while((rawLine = br.readLine()) != null) {
			line = rawLine.split(" ");
			g.addEdge(Integer.parseInt(line[0]) - 1, Integer.parseInt(line[1]) - 1);
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
	private Queue<Integer> queue;
	private HashMap<Integer, Integer> nodesAtDistance;
	
	private int maxDist; // max Erdos Number
	
	public ErdosNumber(Graph g, int source) {
		this.g = g;
		this.source = source;
		nodesAtDistance = new HashMap<Integer, Integer>();
		nodeVisited = new boolean[g.getV()];
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
			
			if (nodesAtDistance.get(distanceTo[u]) == null)
				nodesAtDistance.put(distanceTo[u], 1);
			else
				nodesAtDistance.put(distanceTo[u], nodesAtDistance.get(distanceTo[u]) + 1);
			//numNodesAtDist[distanceTo[u]]++;
			for (int v : g.getAdj(u)) {
				if (!nodeVisited[v]) {
					queue.add(v);
					nodeVisited[v] = true;
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
			System.out.println(nodesAtDistance.get(i));
		}
	}
}