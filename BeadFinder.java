import java.awt.Color;

public class BeadFinder {
    // 2-D array to keep track of visited pixels
    private boolean[][] tracker;
    // Stack to store blobs found in an image
    private Stack<Blob> blobs;
    // width of image
    private int width;
    // height of image
    private int height;
    // luminance threshold
    private double tau;
    // picture analyzed
    private Picture picture;
    
    //  finds all blobs in the specified picture using luminance threshold tau
    public BeadFinder(Picture picture, double tau) {
        this.picture = picture;
        width = picture.width();
        height = picture.height();
        this.tau = tau;
        tracker = new boolean[width][height];
        blobs = new Stack<Blob>();
        
        for (int col = 0; col < width; col++) {
            for (int row = 0; row < height; row++) {   
                // check if pixel could be a blob with luminance
                Color color = picture.get(col, row);
                double luminance = Luminance.lum(color);
                if (luminance >= tau) {
                    findBlobs(col, row);
                }
                else {
                    tracker[col][row] = true;
                }
            }
        }        
    }
    
    // private recursive depth first search method
    private void dfs(int x, int y, Blob blob) {        
        // base cases
        // out of bounds
        if (x < 0 || x >= width) return;    
        if (y < 0 || y >= height) return;  
        // already visited
        if (tracker[x][y]) return;
        
        // too dark
        Color color = picture.get(x, y);
        double luminance = Luminance.lum(color);
        if (luminance < tau) {              
            tracker[x][y] = true;
            return;
        }
        
        // add pixel to blob
        blob.add(x, y);   
        
        // mark pixel as visited
        tracker[x][y] = true;
        
        // recursively call dfs 
        dfs(x, y-1, blob);   
        dfs(x+1, y, blob);   
        dfs(x-1, y, blob);   
        dfs(x, y+1, blob);   
        
    }
    
    // private helper method to run dfs() from each unmarked pixel
    private void findBlobs(int x, int y) { 
        Blob blob = new Blob();
        if (!tracker[x][y]) {
            dfs(x, y, blob); 
            if (blob.mass() != 0) blobs.push(blob);  
        }
    }
    
    // private method to count beads 
    private int countBeads(int minPixels) {  
        int counter = 0;   
        if (minPixels == 0) minPixels = 1;
        for (Blob b : blobs) {             
            if (b.mass() >= minPixels) counter++;
            else continue;
        }
        return counter;
    }
    
    // private method to search and store beads in Blob[]
    private Blob[] getBeads(Blob[] beads, int minPixels) {         
        int i = 0;
        if (minPixels == 0) minPixels = 1;
        for (Blob b : blobs) {
            if (b.mass() >= minPixels) {
                beads[i] = b;
                i++;
            }
        }
        return beads;
    }
    
    //  returns all beads (blobs with >= P pixels)
    public Blob[] getBeads(int P) {       
        int N = countBeads(P);  
        Blob[] beads = new Blob[N];
        beads = getBeads(beads, P);
        return beads;
    }  
    
    //  unit tests the BeadFinder data type
    public static void main(String[] args) {
        int P = Integer.parseInt(args[0]);
        double tau = Double.parseDouble(args[1]);
        Picture picture = new Picture(args[2]);
        
        // create new BeadFinder object 
        BeadFinder allBlobs = new BeadFinder(picture, tau);      
        // create new Blob array with beads in it
        Blob[] allBeads = allBlobs.getBeads(P);
        
        // output center of masses and mass of beads
        int size = allBeads.length;        
        for (int i = 0; i < size; i++) {
            StdOut.println(allBeads[i].toString());
        }        
    }
}
