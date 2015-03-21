import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StreamTokenizer;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.LinkedList;


public class Main {

	public static void main(String[] args)  throws IOException {
			
			//System.setIn(new FileInputStream("in.txt"));
			Graph g = GraphFromInputBuilder.build();
			
			//System.out.println("V: " + g.getV() + " E: " + g.getE());
			ErdosNumber en = new ErdosNumber(g, GraphFromInputBuilder.source);
			en.bfs();
			
			en.printOutput();
			
			//char[] buff = new char[20];
			//BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			//GraphFromInputBuilder.readLine(buff, br);
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
		node = new LinkedList[v];

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
	public LinkedList<Integer> getAdj(int v) {
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
	
	public static Graph build() {
		
		int v;
		int e;
		int s;
		Graph g = null;
		
		StreamTokenizer st = new StreamTokenizer(new BufferedReader(new InputStreamReader(System.in)));
        st.parseNumbers();
        try{
                st.nextToken();
                v = (int) st.nval;
        		g = new Graph(v);
                st.nextToken();
                e = (int) st.nval;
                g.setE(e);
                st.nextToken();
                s = (int) st.nval;
                source = s-1;
                int i;
                i = 0;
                
                int u;
                int w;
                
                while(i < e)
                {
                        st.nextToken();
                        u = (int) st.nval;
                        st.nextToken();
                        w = (int) st.nval;
                        g.addEdge(u-1, w-1);
                        i++;
                }
        } catch (IOException excep){
                System.err.println("Something went wrong.");
        }
		
		return g;
	}
	
	public static int readLine(char[] buff, BufferedReader br) throws IOException{
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