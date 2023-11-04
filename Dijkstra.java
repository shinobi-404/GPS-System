import java.util.*;
import java.io.*;

class Dijkstra{
private static Scanner djinfo; // global scanner variable for ease of use 
public static void main(String args[]) throws Exception {
    // creates a linked list collections 
    List<List<DjEdge>> dijGraph = new LinkedList<List<DjEdge>>(); // linked list implementation 
    
    // scans for the new file 
    File fw = new File("ans2-input.txt");
    djinfo = new Scanner(fw);
    
    // takes in variables from file 
    int nvert = reader(djinfo); // number of verticies 
    int svert = reader(djinfo); // source vertiicies 
    int nedges = reader(djinfo); // number of edges 
    
    // creates the graph with the number of verteices 
    for(int i = 0; i < nvert; i++) dijGraph.add(new LinkedList<DjEdge>());
    
    // calls for the dikstra algorithm 
    dijAlgo(svert-1, nedges, djinfo, dijGraph);
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

        // writes the file with the last name than firstname 
       FileWriter fw = new FileWriter("ans2-output.txt");
       
       int len = upaths.length; // takes the legnth of the upath 
       
       fw.write(len + "\n"); // creates the new line 

       // for loop for calcualting file output 
       for(int i = 0; i < len; i++){
           fw.write((i + 1) + " " + upaths[i] + " " + par[i] + "\n"); // format for file output 
       }

       fw.close(); // closes the file 
   }
  
}

// classes are created 
// comparator, sorts data between the nodes 
class graphComp implements Comparator<DjNode>{

   public int compare(DjNode graph1,DjNode graph2){
       return graph1.djWeight - graph2.djWeight; // returns the calcualted weight 
   }  
}

// creates the node for the node 
class DjNode{
   int djSour, djWeight;
   DjNode(int djSour,int djWeight){
       this.djSour = djSour;
       this.djWeight = djWeight;
   }
}

// creates the edges for the graph
class DjEdge{
   int djDest, djWeight;
   DjEdge(int djDest, int djWeight){
       this.djDest = djDest;
       this.djWeight = djWeight;
   }
}
















