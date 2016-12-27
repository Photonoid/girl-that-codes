public class Tour {
    
    // helper linked-list data type
    private class Node {
        private Point point;
        private Node next;
        
        private Node(Point p) {
            this.point = p;
            this.next = null;
        }
    }
    
    // first node of the circular linked list
    private Node first;
    
    //  creates an empty tour
    public Tour()  {  
        first = null;
    }
    
    //  creates the 4-point tour a->b->c->d->a (for debugging)
    public Tour(Point a, Point b, Point c, Point d)  { 
        first = new Node(a);
        first.next = new Node(b);
        first.next.next = new Node(c);
        first.next.next.next = new Node(d); 
        first.next.next.next.next = first;
    }
    
    //  returns the number of points in this tour
    public int size()     { 
        // if tour is empty
        if (first == null) return 0;
        
        // counts number of points
        else {
            Node current = first;
            int counter = 0;         
            do {
                counter++;
                current = current.next;
            } while (current != first);  
            return counter;
        }
    }
    
    //  returns the length of this tour
    public double length()  {  
        // if tour is empty
        if (first == null) return 0.0;
        
        // increments total length of tour
        else {
            Node current = first;
            double distance = 0.0;
            do {
                Point a = current.point;
                Point b = current.next.point;
                double increment = a.distanceTo(b);
                distance = distance + increment;
                current = current.next;
            } while (current != first);    
            
            return distance;
        }
    }
    
    //  draws this tour to standard drawing
    public void draw()  {
        // as long as the tour is not empty
        if (first != null) {
            Node current = first;
            do {
                Point a = current.point;
                Point b = current.next.point;
                a.drawTo(b);
                current = current.next;
            } while (current != first);     
        }
    }
    
    //  prints this tour to standard output
    public void show()  {
        // as long as the tour is not empty
        if (first != null) {
            Node current = first;
            do {
                StdOut.println(current.point);
                current = current.next;
            } while (current != first);     
        }
    }
    
    //  inserts p using the nearest neighbor heuristic
    public void insertNearest(Point p) { 
        // create a new node with point p
        Node newNode = new Node(p);
        
        // degenerate case for empty tour
        if (first == null) {
            first = newNode; 
            // make circular
            first.next = first; 
        } 
        
        else {
            // first node in linked list
            // current travels through the list
            Node current = first;              
            // nearest stores the nearest point
            Node nearest = null; 
            // set maximum distance 
            double maxDistance = Double.POSITIVE_INFINITY;
            
            do {
                // find the next distance between points
                double distance = p.distanceTo(current.point);
                
                // compare current smallest distance to new distance found
                // and store smallest distance as new maxDistance
                if (distance < maxDistance) {
                    nearest = current;
                    maxDistance = distance;                       
                }           
                
                // increment to next point
                current = current.next;                      
                // go through the entire tour with the loop
            } while (current != first);
            
            // insert new point 
            newNode.next = nearest.next;
            nearest.next = newNode;
        }
    }
      
    //  inserts p using the smallest increase heuristic
    public void insertSmallest(Point p)  {
        // create a new node with point p
        Node newNode = new Node(p);
        
        // degenerate case for empty tour
        if (first == null) {
            first = newNode; 
            // make circular
            first.next = first; 
        } 
        
        else {
            // first node in linked list
            // current travels through the list
            Node current = first;              
            // nearest stores the point with the smallest change in distance
            Node smallest = null; 
            
            // maximum change in distance
            double maxChange = Double.POSITIVE_INFINITY;
            
            do {
                // distance between inserted point and current point
                double distance1 = p.distanceTo(current.point);
                // distance between inserted point and next point
                double distance2 = p.distanceTo(current.next.point);
                // distance between current point and next point
                double distance3 = current.point.distanceTo(current.next.point);
                // compute change in distance
                double change = Math.abs(distance1 + distance2 - distance3);
                
                // compare current smallest distance to new distance found
                // and store the smallest change as the maximum change
                if (change < maxChange) {
                    smallest = current;
                    maxChange = change;                       
                }           
                
                // increment to next point
                current = current.next;                      
                // go through the entire tour with the loop
            } while (current != first);
            
            // insert new point 
            newNode.next = smallest.next;
            smallest.next = newNode;
        }
    }
    
    public static void main(String[] args) {
        // define 4 points forming a square
        Point a = new Point(100.0, 100.0);
        Point b = new Point(500.0, 100.0);
        Point c = new Point(500.0, 500.0);
        Point d = new Point(100.0, 500.0);
        
        // Set up a Tour with those four points
        // The constructor should link a->b->c->d->a
        Tour squareTour = new Tour(a, b, c, d);
        
        // Output the Tour
        squareTour.show();
        // Output size of Tour
        StdOut.println(squareTour.size());
        // Output length of Tour
        StdOut.println(squareTour.length());
        
        // Draw Tour
        StdDraw.setXscale(0, 600);
        StdDraw.setYscale(0, 600);
        squareTour.draw(); 
    }
}
