import java.util.*;
import java.io.*;

class GPS{
private static Scanner djinfo; // global scanner variable for ease of use 
    public static void main(String args[]) throws Exception{
        // creates a linked list collections 
        
        List<List<DjEdge>> dijGraph = new LinkedList<List<DjEdge>>(); // linked list implementation 
        
        // scans for the new file 
        File fw = new File("asn3-input-1.txt");
        djinfo = new Scanner(fw);
        
        // takes in variables from file 
        int nvert = reader(djinfo); // number of verticies 
        int svert = reader(djinfo); // source vertiicies 
        int nedges = reader(djinfo); // number of edges 
        
        // creates the graph with the number of verteices 
        for(int i = 0; i < nvert; i++) dijGraph.add(new LinkedList<DjEdge>());
        
        // calls for the dikstra algorithm 
        dijAlgo(svert-1, nedges, djinfo, dijGraph);
        bellmanFordAlgo(svert - 1, nvert, dijGraph);
        floydWarshallAlgo(nvert, dijGraph);
    }

    // initializes the edges with the use of a helper function 
   public static void initEdges(List<List<DjEdge>> dijGraph, int nedges, Scanner djinfo){
    // reads the sources from teh file 
    for(int i = 0; i < nedges; i++){
            // reads the sources from the file         
            int djSour = djinfo.nextInt(); // reads the sourve vertx
            int djDest = djinfo.nextInt(); // reads the destination 
            int djWeight = djinfo.nextInt(); // reads the weight 
            initEdges1(dijGraph, djSour, djDest, djWeight); // calls the helper function 
        }

   }
    
   // helper function which helps initualize the initialize helper fucntion 
   public static void initEdges1(List<List<DjEdge>> dijGraph, int djSour, int djDest, int djWeight){
        dijGraph.get(djSour-1).add(new DjEdge(djDest-1, djWeight)); // initialuzes the source 
        dijGraph.get(djDest-1).add(new DjEdge(djSour-1, djWeight)); // initializes the destination 
    }
   
  // reader funciton helps read from the file 
    public static int reader(Scanner djinfo){
        int num = djinfo.nextInt(); // reads the number 
        djinfo.nextLine(); // goes to the next line 
        return num; // returns the number 
    }

    //method that calculates shortest upaths from one node to all the other nodes using dikstra algorithm
    public static void dijAlgo(int djSour, int Edges, Scanner djinfo, List<List<DjEdge>> dijGraph) throws Exception{
            initEdges(dijGraph, Edges, djinfo);
            
            // apply dikstra algorithm and get shortest upaths and par node for every vertex
            int vert = dijGraph.size(); // verticies 
            int [] par = new int[vert]; // parents 
            int [] upaths = new int[vert]; // paths not visited yet (unvisited)
            boolean [] vpaths = new boolean[vert]; // visited path 
            
            // initializes paths 
            for(int i = 0; i < upaths.length; i++) upaths[i] = Integer.MAX_VALUE; // initializes unvisited paths to infinity
            for(int i = 0; i < vpaths.length; i++) vpaths[i] = false; // initializes visited paths false (not visited)
        
            // Creates a priority queue for effeiecny 
            PriorityQueue<DjNode> dijQue = new PriorityQueue<DjNode>(new graphComp());

            upaths[djSour] = 0; // marks the unvisited path 

        // adds all the nodes to the priority que
            for(int i = 0; i < vert; i++) dijQue.add(new DjNode(i,upaths[i]));
            
            // caclaulates the vertex and calcualtes the 
            for (int i = 0; i < vert; i++){
                
                // calls the index 
                DjNode graNode = dijQue.poll(); // call the index
                vpaths[graNode.djSour] = true; // marks as visted in the visited array 
                List<DjEdge> indexDj = dijGraph.get(graNode.djSour);
                
                // calculates the graph 
                for(int j = 0; j < indexDj.size(); j++){
                    
                    // checks the path and if its index have been visited or not
                    if(!vpaths[indexDj.get(j).djDest] && upaths[indexDj.get(j).djDest] > upaths[j] + indexDj.get(j).djWeight){
                        
                        // calcuates the destination 
                        upaths[indexDj.get(j).djDest] = upaths[graNode.djSour] + indexDj.get(j).djWeight;
                        par[indexDj.get(j).djDest] = graNode.djSour + 1;
                        

                        dijQue.add(new DjNode(indexDj.get(j).djDest,upaths[indexDj.get(j).djDest]));
                    }
            }
        }
        
        // end of the algorithm 
        upaths[djSour] = -1; // marks unvisited path 
        par[djSour] = -1; // marks parent 
        
        
        writeDjFile(upaths, par); // calls the method to write the file 
    }  

    // calls the themethod to write hte output file. 
    public static void writeDjFile(int upaths[], int par[]) throws Exception{

         // writes the file
        FileWriter fw = new FileWriter("asn3-output-da.txt");
        // calcualtes the length and puts new line character accordingly 
        int len = upaths.length; 
        fw.write(len + "\n");

        // for loop for calcualting file output 
        for(int i = 0; i < len; i++){
            fw.write((i + 1) + " " + upaths[i] + " " + par[i] + "\n"); // format for file output 
        }

        fw.close(); // closes the file 
    }


   // calculates the FloydWarshall algorithmm
    public static void floydWarshallAlgo(int djSour, List<List<DjEdge>> djGraph) throws Exception{
        int[][] dist = new int[djSour][djSour];

        // Initialize the distance for the matrix
        for(int i = 0; i < djSour; i++){
            Arrays.fill(dist[i], Integer.MAX_VALUE);
            dist[i][i] = 0;
        }

        // calcualtes disntance usign edges and weights from the graph
        for (int u = 0; u < djSour; u++) {
            for (DjEdge edge : djGraph.get(u)) {
                int v = edge.djDest;
                int weight = edge.djWeight;
                dist[u][v] = weight;
            }
        }

        // shortest path is calcualted  
        for (int k = 0; k < djSour; k++){
            for (int i = 0; i < djSour; i++){
                for (int j = 0; j < djSour; j++){
                    if (dist[i][k] != Integer.MAX_VALUE && dist[k][j] != Integer.MAX_VALUE && dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                    }
                }
            }
        }

        // calls function to write file
        writeFloydWarshallFile(dist);
    }

    // calls the function that writes the output for the Floyd Warshall algorithm 
    public static void writeFloydWarshallFile(int upath[][]) throws Exception{
        FileWriter fw = new FileWriter("asn3-output-fw.txt");

        fw.write(upath.length + "\n");

        for (int i = 0; i < upath.length; i++) {
            for (int j = 0; j < upath.length; j++) {
                fw.write(upath[i][j] + " ");
            }
            fw.write("\n");
        }

        fw.close();
    }

    // calcualtes the bellman Ford alogrithm 
    public static void bellmanFordAlgo(int sour, int djSour, List<List<DjEdge>> djGraph) throws Exception{
        int[] dist = new int[djSour];
        int[] par = new int[djSour];

        // initailizes the array for use 
        Arrays.fill(dist, Integer.MAX_VALUE);
        Arrays.fill(par, -1);
        dist[sour] = 0;

        // Checks for repeated edges and vertices 
        for (int i = 1; i < djSour; i++) {
            for (int u = 0; u < djSour; u++) {
                for (DjEdge edge : djGraph.get(u)) {
                    int v = edge.djDest;
                    int weight = edge.djWeight;
                    if (dist[u] != Integer.MAX_VALUE && dist[u] + weight < dist[v]) {
                        dist[v] = dist[u] + weight;
                        par[v] = u;
                    }
                }
            }
        }

        // Creates the output file for Bellman Ford Algoirthm 
        writeBellmanFordFile(sour, dist, par);
    }


    // Creates the file for Bellman Ford File 
    public static void writeBellmanFordFile(int djSour, int upath[], int par[]) throws Exception{
        FileWriter fw = new FileWriter("asn3-output-bf.txt");

        fw.write(upath.length + "\n");

        for (int i = 0; i < upath.length; i++) {
            if (i == djSour) {
                fw.write((i + 1) + " " + 0 + " " + 0 + "\n");
            } else {
                fw.write((i + 1) + " " + upath[i] + " " + (par[i] + 1) + "\n");
            }
        }

        fw.close();
    }
}


