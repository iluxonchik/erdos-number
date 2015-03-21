import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StreamTokenizer;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;


// TODO: Auto-generated Javadoc
/**
 * The Class Main.
 */
public class Main {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
			
			GraphFromInputBuilder builder = new GraphFromInputBuilder(System.in);
			Graph g = null;
			
			try {
				g = builder.build();
			} catch (IOException e) {
				System.out.println("Error while building the graph from the input file.");
				e.printStackTrace();
			}
			
			ErdosNumber en = new ErdosNumber(g, builder.getSource() - 1);
			en.bfs();
			
			try {
				en.printOutput();
			} catch (IOException e) {
				System.out.println("Error while printing the output.");
				e.printStackTrace();
			}
	}
}

class Graph {
	
	private int v; // number of vertices
	private int e; // number of edges
	private ArrayList<LinkedList<Integer>> node;
	
	/**
	 * Instantiates a new graph.
	 *
	 * @param v number of vertices 
	 */
	public Graph (int v, int e) {
		this.v = v;
		this.e = e;
		
		// Allocate memory for nodes
		node = new ArrayList<LinkedList<Integer>>(v);

		for (int i = 0; i < v; i++) {
			// Create an adjacency list for every node
			node.add(new LinkedList<Integer>());
		}
	}
	
	public void addEdge(int u, int v) {
		node.get(u).add(v);
		node.get(v).add(u);
	}
	
	/**
	 * Returns the adjacency list of a vertex. 
	 *
	 * @param v vertex to access
	 * @return adjacency list of vertex v
	 */
	public LinkedList<Integer> getAdj(int v) {
		return node.get(v);
	}
	
	// Getters
	public int getE() { return this.e; }
	public int getV() { return this.v; }

	// Setters
	public void setE(int e) { this.e = e; }
	public void setV(int v) { this.v = v; }
	
}

class GraphFromInputBuilder{
	
	private InputStream is;
	private int source; // source for BFS
	private int v; // number of vertices
	private int e; // number of edges
	
	
	public GraphFromInputBuilder(InputStream is) { this.is = is; }
	
	/**
	 * Builds a graph using the specified InputStream as an input. 
	 *
	 * @param is the InputStream from where the input will be read
	 * @return an instance of a Graph
	 * @throws IOException if an I/O error in parseNumberFromStream occurs
	 */
	public Graph build() throws IOException{
		Graph g = null;
		
		StreamTokenizer st = new StreamTokenizer(new BufferedReader(new InputStreamReader(System.in)));
        st.parseNumbers();        
        
        v = parseNumberFromStream(st);
        e = parseNumberFromStream(st);
        source = parseNumberFromStream(st);
        g = new Graph(v, e);
        parseGraphEdges(st, g);
        
		return g;
	}
	
	/**
	 * Parses the number from the InputStream using the StreamTokenizer passed as an argument. 
	 *
	 * @param st the StreamTokenizer to use
	 * @return the parsed integer
	 * @throws IOException if an I/O error in StreamTokenizer.nextToken() occurs
	 */
	private int parseNumberFromStream(StreamTokenizer st) throws IOException{
			st.nextToken();
			return (int) st.nval;
	}
	
	/**
	 * Parses the edges from the InputStream using the st and adds them to the graph g. 
	 *
	 * @param st the StreamTokenizer to use
	 * @param g the Graph to add the edges to
	 * @throws IOException if an I/O error in parseNumberFromStream() occurs
	 */
	private void parseGraphEdges(StreamTokenizer st, Graph g) throws IOException{
		final int e = g.getE();
        for (int i = 0, w = 0, u = 0; i < e; i++) {
            u = parseNumberFromStream(st);
            w = parseNumberFromStream(st);
            g.addEdge(u-1, w-1);
         }
	}
	

	// Getters
	public InputStream getInputStream() { return is; }
	public int getSource() { return source; }
	
	// Setters
	public void setInputStream(InputStream is) { this.is = is; }
	
	// Not used
	public int readLine(char[] buff, BufferedReader br) throws IOException{
		int index = 0;
		int charRead;
		int firstChar;
		
		firstChar = br.read();

		if (firstChar == -1)
			return -1;
		
		buff[index] = (char)firstChar;
		charRead = 1;
		
		while(buff[index] != '\n') {
			index += charRead;
			charRead = br.read(buff, index, 1);
		}
		return index;
		
	}
}

class ErdosNumber {
	private Graph g;
	private boolean nodeVisited[]; // true - node already visited; false - node not yet visited
	private int distanceTo[]; // distance from node v (index) to the source node
	private int source; // source node for bfs
	private int[] queue;
	private int[] nodesAtDistance;
	
	private int maxDist; // max Erdos Number
	private int currNode;
	
	public ErdosNumber(Graph g, int source) {
		this.g = g;
		this.source = source;
		nodesAtDistance = new int[g.getV()];
		nodeVisited = new boolean[g.getV()];
		distanceTo = new int[g.getV()];
		queue = new int[g.getV()];
	}
	
	public void bfs(Graph g, int source) {				
		// TODO: initialise the distanceTo array with infinity?
		// Projects specifications seem to say that all of the nodes
		// have a finite distance. Not doing this saves Theta(V).
		LinkedList<Integer> adj;
		int v;
		
		int front = 0;
		int rear = 0;
		
		
		distanceTo[source] = 0;
		nodeVisited[source] = true;
		
		queue[front++] = source;
		
		maxDist = 0;
		currNode = 0;
		
		while (front != rear) {
			currNode = queue[rear++];

			nodesAtDistance[distanceTo[currNode]]++;
			adj = g.getAdj(currNode);
			
			while(!adj.isEmpty()) {
				v = adj.remove();
				if (!nodeVisited[v]) {
					queue[front++] = v;
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
	 
	
	public void printOutput() throws IOException{
		
		  BufferedWriter log = new BufferedWriter(new OutputStreamWriter(System.out));
		  char out[] = new char[30];
		  int j;
		  int tmp;
		  int i;

		  // Make last character in the array '\n', so that all character sequences end in a newline
		  out[out.length - 1] = '\n';
		 
		  // TODO: put in a separate function
		   // add maxDist to the output
		  j = 28;
		  tmp = maxDist;
		  while(tmp > 0) {
			  out[j] = (char)((int)'0' + (tmp % 10));
              tmp = tmp/10;
              j--;
		  }
		  log.write(out, j+1, 29-j);
		  
		  // add nodes at all distances to the output
		  i = 1;
		  while(i <= maxDist) {			   tmp = nodesAtDistance[i];
		      j = 28;
		      while(tmp > 0)  {
		          out[j] = (char)((int)'0' + (tmp % 10));
		         tmp = tmp/10;
		           j--;
		      }
		      log.write(out, j+1, 29-j);
		      i++;
		  }
		  
		  log.flush();
		
	}
}