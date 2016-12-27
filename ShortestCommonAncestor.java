import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.DirectedCycle;

public class ShortestCommonAncestor {
    
    private final Digraph G;
    
    // constructor takes a rooted DAG as argument
    public ShortestCommonAncestor(Digraph G) {
        if (G == null) throw new
            java.lang.NullPointerException("Argument cannot be null.");
        // throw exception if Digraph G is not a rooted DAG  
        if (!isDAG(G)) throw new 
            java.lang.IllegalArgumentException("Not a rooted DAG");
        this.G = new Digraph(G);       
    }
    
    private boolean isDAG(Digraph digraph) {
        // check if there is one single root         
        // integer to store number of roots
        int n = 0;
        for (int i = 0; i < digraph.V(); i++) {
            if (digraph.outdegree(i) == 0) n++;
        }
        if (n != 1) return false;
        
        // check if there are any cycles
        DirectedCycle cycle = new DirectedCycle(digraph);        
        if (cycle.hasCycle()) return false;
        
        // else return it is a DAG
        return true;
    }
    
    // length of shortest ancestral path between v and w
    public int length(int v, int w) {
        // throw exception if vertex is invalid
        if (!isVertex(v) || !isVertex(w)) throw new 
            java.lang.IndexOutOfBoundsException("Not a valid vertex.");
        
        // initialize ancestor and length variables
        int length = Integer.MAX_VALUE;
        
        // create two new BFS to find shortest paths 
        BreadthFirstDirectedPaths breadthV = 
            new BreadthFirstDirectedPaths(G, v); 
        BreadthFirstDirectedPaths breadthW = 
            new BreadthFirstDirectedPaths(G, w);
        
        return findLength(breadthV, breadthW, length);      
    }
    
    private int findLength(BreadthFirstDirectedPaths breadthV, 
                           BreadthFirstDirectedPaths breadthW, 
                           int length) {        
        // iterate through all vertices to find shortest path
        for (int i = 0; i < G.V(); i++) {
            if (breadthV.hasPathTo(i) && breadthW.hasPathTo(i)) {
                // create temporary integer variable to keep track of shortest length
                int tempLength = breadthV.distTo(i) + breadthW.distTo(i);
                // update length and ancestor if shorter
                if (tempLength < length) {
                    length = tempLength;
                }
            }
        }
        return length;
    }
        
    // a shortest common ancestor of vertices v and w
    public int ancestor(int v, int w) {
        // throw exception if vertex is invalid
        if (!isVertex(v) || !isVertex(w)) throw new 
            java.lang.IndexOutOfBoundsException("Not a valid vertex.");
        
        // initialize ancestor and length variables
        int ancestor = -1;
        int length = Integer.MAX_VALUE;
        
        // create two new BFS to find shortest paths 
        BreadthFirstDirectedPaths breadthV = 
            new BreadthFirstDirectedPaths(G, v); 
        BreadthFirstDirectedPaths breadthW = 
            new BreadthFirstDirectedPaths(G, w);
        
        return findAncestor(breadthV, breadthW, ancestor, length);   
    }
    
    // private helper method to determine if valid vertex
    private boolean isVertex(int i) {
        return (i >= 0 && i <= (G.V() - 1));
    }   
        
    private int findAncestor(BreadthFirstDirectedPaths breadthV, 
                             BreadthFirstDirectedPaths breadthW, 
                             int ancestor, int length) {        
        // iterate through all vertices to find shortest path
        for (int i = 0; i < G.V(); i++) {
            if (breadthV.hasPathTo(i) && breadthW.hasPathTo(i)) {
                // create temporary integer variable to keep track of shortest length
                int tempLength = breadthV.distTo(i) + breadthW.distTo(i);
                // update length and ancestor if shorter
                if (tempLength < length) {
                    length = tempLength;
                    ancestor = i;
                }
            }
        }
        return ancestor;
    }
    
    // length of shortest ancestral path of vertex subsets A and B
    public int length(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        // throw exception if argument is null
        if (subsetA == null || subsetB == null) throw new 
            java.lang.NullPointerException("Argument cannot be null.");    
        
        // throw exception if any of the vertices are invalid
        if (!isVertex(subsetA) || !isVertex(subsetB)) throw new
            java.lang.IndexOutOfBoundsException(
                      "Subsets contain an invalid vertex.");    
        
        // throw exception if subset is empty
        if (isEmpty(subsetA) || isEmpty(subsetB)) throw new 
            java.lang.IllegalArgumentException(
                      "Subset cannot be empty.");
        
        // initialize ancestor and length variables
        int ancestor = -1;
        int length = Integer.MAX_VALUE;
        
        // create two new BFS to find shortest paths 
        BreadthFirstDirectedPaths breadthA = 
            new BreadthFirstDirectedPaths(G, subsetA); 
        BreadthFirstDirectedPaths breadthB = 
            new BreadthFirstDirectedPaths(G, subsetB);
        
        return findLength(breadthA, breadthB, length);   
    }
    
    // a shortest common ancestor of vertex subsets A and B
    public int ancestor(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        // throw exception if argumenet is null
        if (subsetA == null || subsetB == null) throw new 
            java.lang.NullPointerException("Argument cannot be null.");    
        
        // throw exception if any of the vertices are invalid
        if (!isVertex(subsetA) || !isVertex(subsetB)) throw new
            java.lang.IndexOutOfBoundsException(
                      "Subsets contain an invalid vertex.");    
        
        // throw exception if subset is empty
        if (isEmpty(subsetA) || isEmpty(subsetB)) throw new 
            java.lang.IllegalArgumentException(
                      "Subset cannot be empty.");
        
        // initialize ancestor and length variables
        int ancestor = -1;
        int length = Integer.MAX_VALUE;
        
        // create two new BFS to find shortest paths 
        BreadthFirstDirectedPaths breadthA = 
            new BreadthFirstDirectedPaths(G, subsetA); 
        BreadthFirstDirectedPaths breadthB = 
            new BreadthFirstDirectedPaths(G, subsetB);
        
        return findAncestor(breadthA, breadthB, ancestor, length);              
    }
    
    // private helper method to determine if valid vertex
    private boolean isVertex(Iterable<Integer> subsetI) {
        for (int i : subsetI) {
            if (!isVertex(i)) return false;
        }
        return true;
    }
    
    // private helper method to determine if subset is empty
    private boolean isEmpty(Iterable<Integer> subsetI) {
        int n = 0;
        for (int i : subsetI) {
            n++;
        }
        if (n == 0) return true;
        return false;
    }       
    
    // do unit testing of this class
    public static void main(String[] args) {  
        // testing length and ancestor for integers        
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        StdOut.println("DIGRAPH IS: " + G);
        ShortestCommonAncestor sca = new ShortestCommonAncestor(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sca.length(v, w);
            int ancestor = sca.ancestor(v, w);                
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
            StdOut.println(" ");
        }        
    }    
}
