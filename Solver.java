import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

public class Solver {
    // private instance variables for Solver class
    private SearchNode goalNode; 
    private final MinPQ<SearchNode> pq;
    private int totalMoves;
    private Board initialBoard;
    
    // private class to keep track of board information
    private final class SearchNode implements Comparable<SearchNode> {
        // private instance methods
        private Board board;
        private SearchNode previous;
        private int moves;
        private int priorityVal;
        
        // create a search node with the board, moves, and previous node
        private SearchNode(Board board1, int moves1, SearchNode previous1) {
            // initiate instance variables
            board = board1;
            previous = previous1;
            moves = moves1;
            // set the value of priority as -1
            priorityVal = -1; 
        }
        
        // keep track of priority
        private int priority() {
            // if newly created board
            if (priorityVal == -1) 
                priorityVal = moves + board.hamming();
            return priorityVal;
        }
        
        // compare the priorities between search nodes
        public int compareTo(SearchNode that) {
            if      (this.priority() < that.priority()) return -1;
            else if (this.priority() > that.priority()) return 1;
            else                                        return 0;
        }
    }
    
    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        // throw exceptions for corner cases
        if (initial == null) throw new java.lang.NullPointerException();
        if (!initial.isSolvable()) throw new 
            java.lang.IllegalArgumentException();        
        
        // initialize instance variables
        initialBoard = initial;
        pq = new MinPQ<SearchNode>();
        totalMoves = 0;       
        
        // create new searchNode with initial board, moves, 
        // and null as previous node
        SearchNode currentNode = new SearchNode(initialBoard, totalMoves, null);
        // insert current searchNode into MinPQ
        pq.insert(currentNode);     
        
        // while the board is not yet the goal board
        while (!currentNode.board.isGoal()) {            
            // make the current node the minimum one in the pq
            currentNode = pq.delMin();  
            
            // pointer to previous board
            SearchNode previousNode;            
            if (pq.isEmpty()) previousNode = currentNode;  
            else previousNode = currentNode.previous;
            
            // iterate over all neighbors of the current node
            for (Board board : currentNode.board.neighbors()) {
                // if the neighbor board is not equal to previous board
                if (!board.equals(previousNode.board)) {
                    // insert the neighbor board as new searchnode 
                    // increment the number of moves
                    pq.insert(new SearchNode(board, 
                              currentNode.moves + 1, currentNode));
                }              
            }            
        }
        
        // when reached the goal board
        if (currentNode.board.isGoal()) { 
            goalNode = currentNode; 
            totalMoves = goalNode.moves;
        }
    }
    
    // return number of moves necessary
    public int moves() {  
        return totalMoves;
    }
    
    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        Stack<Board> solution = new Stack<Board>();
        
        // create placeholding tempNode 
        // to not modify goalNode
        SearchNode tempNode = goalNode;
        tempNode.previous = goalNode.previous;
        solution.push(tempNode.board);
        
        while (tempNode.previous != null) {
            tempNode = tempNode.previous;
            solution.push(tempNode.board);
        }
        return solution;
    }
    
    // unit testing
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
            blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        
        // check if puzzle is solvable; if so, solve it and output solution
        if (initial.isSolvable()) {
            Solver solver = new Solver(initial);
            StdOut.println("Minimum number of moves = " + solver.moves());
            // print boards
            for (Board board : solver.solution()) {
                StdOut.println(board);
            }
            
            // test moves             
            StdOut.println("moves() method returns: " + solver.moves());
            StdOut.println(" ");
        }
        
        // if not, report unsolvable
        else StdOut.println("Unsolvable puzzle");
    }
}

