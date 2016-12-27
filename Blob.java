public class Blob {
    // instance variables
    private int mass;
    private double cx;
    private double cy; 
    
    //  creates an empty blob
    public Blob() {     
        mass = 0;
        cx = 0.0;
        cy = 0.0;
    }
        
    // adds pixel (x, y) to this blob    
    public void add(int x, int y) {
        // update mass and center of masses
        double xVal = cx * mass;
        double yVal = cy * mass;        
        mass++;
        cx = (xVal + x) / mass;
        cy = (yVal + y) / mass;
    }
        
    // returns the number of pixels added to this blob    
    public int  mass() {
        return mass;
    }
        
    // returns the Euclidean distance between the CM's    
    public double distanceTo(Blob that) {
        double x1 = that.cx;
        double y1 = that.cy;
        double x2 = this.cx;
        double y2 = this.cy;
        double dx = x1 - x2;
        double dy = y1 - y2;
        
        // distance formula
        double distance = Math.sqrt(dx * dx + dy * dy);
        return distance;       
    }
        
    // returns a spring representation of blob    
    public String toString() {
        return String.format("%2d (%8.4f, %8.4f)", mass, cx, cy); 
    }
        
    // unit tests all methods    
    public static void main(String[] args) {
        Blob blob1 = new Blob();
        blob1.add(1, -1);
        blob1.add(2, 0);
        blob1.add(3, 1);
        StdOut.println("blob1 = " + blob1.toString());
        StdOut.println("blob1 mass = " + blob1.mass());
        
        Blob blob2 = new Blob();
        blob2.add(-1, -10);
        blob2.add(-2, -5);
        blob2.add(-3, 0);
        StdOut.println("blob2 = " + blob2.toString());
        StdOut.println("blob2 mass = " + blob2.mass());
        
        StdOut.println("distance between centers = " + blob1.distanceTo(blob2));
    }
}
