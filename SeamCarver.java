import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Picture;
import java.awt.Color;


public class SeamCarver {
    
    // private instance variables
    private static final double MAX_ENERGY = Double.POSITIVE_INFINITY;
    private Picture picture;
    
    
    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        // throw exception if picture is null
        if (picture == null) throw new 
            java.lang.NullPointerException("Picture cannot be null.");
        
        // initialize instance variables
        this.picture = new Picture(picture);
    }
    
    // current picture
    public Picture picture() {
        // return picture that can be mutated
        return new Picture(this.picture);
    }
    
    // width of current picture
    public int width() {
        return picture.width();
    }
    
    // height of current picture
    public int height() {
        return picture.height();
    }
    
    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        // throw exceptions for corner cases 
        if (x < 0 || x > width() - 1 || 
            y < 0 || y > height() - 1) throw new
            java.lang.IndexOutOfBoundsException("Index out of range.");
                                                              
        int x1 = x + 1;
        int x2 = x - 1;
        int y1 = y + 1;
        int y2 = y - 1;

        // if border pixel
        if (x == width() - 1)  x1 = 0;
        if (x == 0)            x2 = width() - 1;
        if (y == height() - 1) y1 = 0;
        if (y == 0)            y2 = height() - 1;

        // find the square of the energy gradient for both x, y
        double energyX = 1.0 * energy(x1, y, x2, y);
        double energyY = 1.0 * energy(x, y1, x, y2);

        // return the energy gradient
        return Math.sqrt(energyX + energyY);
    }
    
    
    // private helper method to calculate energy 
    private int energy(int x1, int y1, int x2, int y2) {
        // get the color at (x1, y1)
        Color color1 = picture.get(x1, y1);
        int r1 = color1.getRed();
        int g1 = color1.getGreen();
        int b1 = color1.getBlue();
        
        // get the color at (x2, y2)
        Color color2 = picture.get(x2, y2);
        int r2 = color2.getRed();
        int g2 = color2.getGreen();
        int b2 = color2.getBlue();
        
        // find the square of the energy gradient
        int sqrGradient = (r1 - r2) * (r1 - r2)
                        + (g1 - g2) * (g1 - g2)
                        + (b1 - b2) * (b1 - b2);
        return sqrGradient;
    }  
    
    // private helper method to find the energies of the current picture
    private double[][] findEnergies() {
        double[][] energies = new double[width()][height()];
        for (int i = 0; i < width(); i++) {          
            for (int j = 0; j < height(); j++) {
                energies[i][j] = energy(i, j);
            }  
        }
        return energies;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        transpose();
        int[] seam = findVerticalSeam();
        transpose();
        return seam;
    }
    
    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        double[][] distTo = new double[width()][height()];
        int[][] edgeTo = new int[width()][height()];
        double[][] energies = findEnergies();
        
        // set all distTo to maximum energies
        for (int i = 0; i < width(); i++) {
            for (int j = 1; j < height(); j++) {
                distTo[i][j] = MAX_ENERGY;
            }
        }
        
        // corner case, if height is 1
        if (height() == 1) {            
            int[] seam = new int[height()];
            double min = MAX_ENERGY;         
            
            for (int j = 0; j < height(); j++) {
                for (int i = 0; i < width(); i++) {                      
                    if (energies[i][j] < min) {
                        min = energies[i][j];
                        seam[j] = i;                        
                    }
                }
            }    
            return seam;
        }
        
        else {
            // relax each vertex not on the bottom row
            for (int j = 0; j < height() - 1; j++) {
                for (int i = 0; i < width(); i++) {
                    relaxVertical(i, j, distTo, edgeTo, energies);
                }
            }
            
            // find smallest energy sum 
            double minDistTo = MAX_ENERGY;
            int last = -1;
            for (int i = 0; i < width(); i++) {
                if (distTo[i][height() - 1] < minDistTo) {
                    minDistTo = distTo[i][height() - 1];
                    last = i;
                }
            }
            
            // find vertical seam
            int[] seam = new int[height()];        
            for (int j = height() - 1; j >= 0; j--) {
                seam[j] = last;
                last = edgeTo[last][j];
            }
            return seam;
        }
    }

    // private helper method to a single vertex in the previous row
    // to find a vertical seam
    private void relaxVertical(int x, int y0, double[][] distTo, 
                               int[][] edgeTo, double[][] energies) {
        // initial case
        if (y0 == 0) {
            distTo[x][y0] = energies[x][y0];
            edgeTo[x][y0] = -1;
        }
        
        int y = y0 + 1;
        int prev = x - 1;
        int next = x + 1;
        
        // if corner case, width is 1
        if (width() - 1 == 0) {
            distTo[x][y] = distTo[x][y0] + energies[x][y];
            edgeTo[x][y] = x;
        }
        
        // if corner case, on left border
        else if (x == 0) {
            relaxVertical(x, y0, x, y, distTo, edgeTo, energies);
            relaxVertical(next, y0, x, y, distTo, edgeTo, energies);
        }
        
        // if corner case, on right border
        else if (x == width() - 1) {
            relaxVertical(prev, y0, x, y, distTo, edgeTo, energies);
            relaxVertical(x, y0, x, y, distTo, edgeTo, energies);
        }      
        
        // not corner case
        else {
            relaxVertical(prev, y0, x, y, distTo, edgeTo, energies);
            relaxVertical(x, y0, x, y, distTo, edgeTo, energies);
            relaxVertical(next, y0, x, y, distTo, edgeTo, energies);            
        }
    }
    
    // private helper method to update distTo/edgeTo 2D arrays
    private void relaxVertical(int x1, int y1, int x2, int y2,
                 double[][] distTo, int[][] edgeTo, double[][] energies) {
        double energyNext = energies[x1][y2];
        double sum = distTo[x2][y1] + energyNext;
        if (sum < distTo[x1][y2]) {
            distTo[x1][y2] = sum;
            edgeTo[x1][y2] = x2;
        }
    }
    
   // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        transpose();
        removeVerticalSeam(seam);
        transpose();
    }
    
    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        // throw exceptions for all corner cases
        // when seam is null
        if (seam == null) throw new
            java.lang.NullPointerException("Seam array cannot bu null.");
        // seam length not equal to picture height
        if (seam.length != height()) throw new
            java.lang.IllegalArgumentException("Seam array is not valid.");
        // width of picture is 1
        if (width() == 1) throw new
            java.lang.IllegalArgumentException("Cannot remove seam.");
        
        // when distance between two pixels in the seam is greater than 1
        int length = seam.length;
        for (int i = 0; i < length - 1; i++) {
            int delta = seam[i + 1] - seam[i];
            if (delta > 1 || delta < -1) throw new
                java.lang.IllegalArgumentException("Seam array is not valid.");
        }    
        
        for (int i = 0; i < length; i++) {
            if (seam[i] < 0 || seam[i] > width() - 1) throw new
                java.lang.IllegalArgumentException("Seam Array is not valid.");
        }
        
        // create new resized picture
        Picture resizedPic = new Picture(width() - 1, height());
        for (int j = 0; j < height(); j++) {
            int newX = 0;
            for (int i = 0; i < width(); i++) {
                // reset the color of each pixel except the seam
                if (i != seam[j]) {
                    resizedPic.set(newX, j, picture.get(i, j));
                    newX++;
                }
            }
        }
        picture = resizedPic;
    }
    
    // private helper method to transpose the picture
    private void transpose() {
        Picture transposedPic = new Picture(height(), width());
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                Color color = picture.get(i, j);
                transposedPic.set(j, i, color);
            }
        }
        picture = transposedPic;       
    }
   
    // do unit testing of this class
    public static void main(String[] args) {
        Picture picture = new Picture(args[0]);
        SeamCarver sc = new SeamCarver(picture);
        
        // original picture
        StdOut.println("Picture width = " + sc.width());
        StdOut.println("Picture height = " + sc.height());
        sc.picture.show();

        // resize the picture
        int removeColumns = Integer.parseInt(args[1]);
        int removeRows = Integer.parseInt(args[2]); 

        for (int j = 0; j < removeRows; j++) {
            int[] seam = sc.findHorizontalSeam();
            sc.removeHorizontalSeam(seam);
        }

        for (int i = 0; i < removeColumns; i++) {
            int[] seam = sc.findVerticalSeam();
            sc.removeVerticalSeam(seam);
        }

        // resized picture
        StdOut.println("Resized picture width = " + sc.width());
        StdOut.println("Resized picture height = " + sc.height());
        sc.picture().show();     
    }
}
