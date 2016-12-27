import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stack;

public class Board {
    
    // private final instance variables 
    private final int[][] blocks;    
    private final int N;
    
    // construct a board from an N-by-N array of tiles
    // (where tiles[i][j] = tile at row i, column j)
    public Board(int[][] tiles)  { 
        // make defensive copy of double array
        N = tiles.length;
        blocks = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                blocks[i][j] = tiles[i][j];   
            }
        }
    }
    
    // return tile at row i, column j (or 0 if blank)
    public int tileAt(int i, int j) {
        if (i < 0 || i > N - 1 || j < 0 || j > N - 1)
            throw new java.lang.IndexOutOfBoundsException();
        return blocks[i][j];
    }
    
    // private helper method to compute the goal value at position        
    private int goalVal(int i, int j) {
        // if at the end of the board 
        if (isEnd(i, j)) return 0;
        // return goal value if not at end of board
        return 1 + i * N + j;
    }    
    
    // private helper method to compute goal board
    private int[][] goalBoard() {
        int[][] goal = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                goal[i][j] = goalVal(i, j);
            }
        }
        return goal;
    }

    // return board size N
    public int size() {
        return N;
    }    
    
    // private helper method to determine if at end of board
    private boolean isEnd(int i, int j) {
        return i == N - 1 && j == N - 1;
    }   
    
    // number of tiles out of place
    public int hamming() {
        // initiate sum
        int sum = 0;
        // compare actual and goal values at position
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (blocks[i][j] != goalVal(i, j) && !isEnd(i, j)) {
                    sum++;
                }
            }
        }
        return sum;
    }
       
    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int sum = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                int val = blocks[i][j];
                if (val != 0 && val != goalVal(i, j)) {
                    int x = (val - 1) / N;
                    int y = val - x * N - 1;
                    int d = Math.abs(i - x) + Math.abs(j - y);
                    sum += d;
                }
            }
        }
        return sum;
    }
    
    // private helper method to determine if tiles are equal
    private boolean isEqual(int[][] tiles1, int[][] tiles2) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (tiles1[i][j] != tiles2[i][j]) return false;
            }
        } 
        return true;
    }    
    
    // is this board the goal board?
    public boolean isGoal() {
        return isEqual(this.blocks, goalBoard());
    }
        
    // is this board solvable?
    public boolean isSolvable() {
        return solvable(blocks);
    }
    
    // private helper method to determine if the board is solvable
    private boolean solvable(int[][] tiles) {
        if (tiles == null) return false;
        int size = N*N;
        
        // put elements into 1-D array
        int[] a = new int[size]; 
        // create int to remember row number of blank
        int blankX = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {                
                // add into 1-D array              
                a[i * N + j] = tiles[i][j]; 
                // store row number if blank 
                if (a[i * N + j] == 0) blankX = i;            
            }
        }
        
        // count inversions
        int inversions = 0;
        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {                
                if (a[j] != 0 && a[i] != 0) 
                     if (a[i] > a[j]) inversions++;                
            }
        }       
        
        // determine if solvable
        // for odd-sized boards, not solvable if odd count
        if (N % 2 == 1) {
            if (inversions % 2 != 0) return false;
            return true;
        }
        // for even-sized boards, not solvable if even count
        else {
            // use blank row number and inversions for sum
            int sum = blankX + inversions;
            if (sum % 2 == 0) return false;
            return true;
        }
    }
    
    // does this board equal y?
    public boolean equals(Object y) {        
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;        
        Board that = (Board) y;
        return (this.N == that.N) && isEqual(this.blocks, that.blocks);
    }
    
    // private helper method to see if it is possible to exchange
    // tiles and then exchange them if possible
    private boolean exchange(int a1, int b1, int a2, int b2) {
        if (a2 < 0 || a2 > N - 1 || b2 < 0 || b2 > N - 1) 
            return false;
        int x = blocks[a1][b1];
        blocks[a1][b1] = blocks[a2][b2];
        blocks[a2][b2] = x;
        return true;
    }
    
    // create iterable object that iterates over all neightbors   
    public Iterable<Board> neighbors() {
        int a = 0;
        int b = 0;
        boolean exists = false;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (blocks[i][j] == 0) {
                    a = i;
                    b = j;
                    exists = true;
                    break;
                }
            }
            if (exists) break;
        }
        
        // create new stack of boards 
        Stack<Board> boardStack = new Stack<Board>();
        // create new board with tiles
        Board board = new Board(blocks);
        // create boolean to see if possible to exchange
        boolean isNeighbor = board.exchange(a, b, a - 1, b);
        if (isNeighbor) boardStack.push(board);
        
        // if possible exchange with other neighbors
        // exchange and push board into a stack of boards
        board = new Board(blocks);
        isNeighbor = board.exchange(a, b, a, b -1);
        if (isNeighbor) boardStack.push(board);
        
        board = new Board(blocks);
        isNeighbor = board.exchange(a, b, a + 1, b);
        if (isNeighbor) boardStack.push(board);
        
        board = new Board(blocks);
        isNeighbor = board.exchange(a, b, a, b + 1);
        if (isNeighbor) boardStack.push(board);
        
        return boardStack;
    }
    
    // string representation of this board
    public String toString() {
        // create new string
        StringBuilder s = new StringBuilder();
        
        // insert the size of the boards
        s.append(N + "\n");
        
        // insert each element in board
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", blocks[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }
    
    // unit testing (required)
    public static void main(String[] args) {
      
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
            blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        StdOut.println(initial);
        
        // test isSolvable method
        StdOut.println("Board is solvable - " + initial.isSolvable());
        
        // other unit testing code
        
        // create new boards
        int[][] tiles1 = {{0, 1, 3}, {4, 2, 5}, {7, 8, 6}};
        int[][] tiles2 = {{1, 2, 3}, {4, 5, 6}, {7, 8, 0}};
        Board a = new Board(tiles1);
        Board b = new Board(tiles2);
        
        // test toString method
        StdOut.println(a);
        StdOut.println(b);        
        
        // test size method
        int size1 = a.size();
        int size2 = b.size();
        StdOut.println("Board 1 size is: " + size1);
        StdOut.println("Board 2 size is: " + size2);
        StdOut.println(" ");
        
        // test tileAt method
        StdOut.println("Tiles in board 1: ");
        for (int i = 0; i < size1; i++) {
            for (int j = 0; j < size1; j++) {
                StdOut.println("Tile at x = " + i + 
                       ", y = " + j + " is: " + a.tileAt(i, j));
            }
        }
        StdOut.println("Tiles in board 2: ");
        for (int i = 0; i < size2; i++) {
            for (int j = 0; j < size2; j++) {
                StdOut.println("Tile at x = " + i + 
                       ", y = " + j + " is: " + b.tileAt(i, j));
            }
        }
        StdOut.println(" ");
        
        // test hamming method
        StdOut.println("Hamming shows board 1 has " + 
               a.hamming() + " tiles out of place.");
        StdOut.println("Hamming shows board 2 has " + 
               b.hamming() + " tiles out of place.");
        StdOut.println(" ");
        
        // test manhattan method
        StdOut.println("Manhattan shows board 1 has " + 
               a.manhattan() + " total moves.");
        StdOut.println("Manhattan shows board 2 has " + 
               b.manhattan() + " total moves.");
        StdOut.println(" ");
        
        // test isGoal method
        StdOut.println("Board 1 is the goal board. - " + a.isGoal());
        StdOut.println("Board 2 is the goal board. - " + b.isGoal());
        StdOut.println(" ");
        
        // test isSolvable method
        StdOut.println("Board 1 is solvable - " + a.isSolvable());
        StdOut.println("Board 2 is solvable - " + b.isSolvable());
        StdOut.println(" ");
        
        // test equals method
        StdOut.println("Board 1 equals board 2 - " + a.equals(b));
        StdOut.println(" ");
       
        // test iterable
        StdOut.println("Iterable for board 1: ");
        for (Board board: a.neighbors()) {
            StdOut.println(board);
        }
        StdOut.println("Iterable for board 2: ");
        for (Board board: b.neighbors()) {
            StdOut.println(board);
        } 
    }
}
