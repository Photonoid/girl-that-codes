public class BeadTracker {
    public static void main(String[] args) {    
        // read min pixel size, luminance threshold and displacement 
        // as command-line arguments
        int P = Integer.parseInt(args[0]);         
        double tau = Double.parseDouble(args[1]);
        double delta = Double.parseDouble(args[2]);
        int argsSize = args.length;
        
        
        for (int i = 3; i < (argsSize - 1); i++) {  
            Picture pic1 = new Picture(args[i]);
            BeadFinder beadfinder1 = new BeadFinder(pic1, tau);
            Blob[] store1 = beadfinder1.getBeads(P);
            int size1 = store1.length;
            
            // read in second frame, create beadfinder object and store 
            // beads in an array and find length of array 
            Picture pic2 = new Picture(args[i + 1]);
            BeadFinder beadfinder2 = new BeadFinder(pic2, tau);
            Blob[] store2 = beadfinder2.getBeads(P);
            int size2 = store2.length;
            
            // corner case if no beads are available
            if (size1 == 0 || size2 == 0) break;
            
            // locate bead in both frames by finding smallest 
            // distance (smaller than delta) between beads
            // and print distance to standard output
            else {
                for (int j = 0; j < size2; j++) {
                    double distance = delta;
                    for (int k = 0; k < size1; k++) { 
                        double minDistance = store2[j].distanceTo(store1[k]); 
                        // if displacement is exactly equal to delta
                        if (minDistance == delta) StdOut.println(delta);
                        else if (minDistance < distance) {
                            distance = minDistance;
                        }
                    }
                    // if none of the distances are less than delta
                    if (distance == delta) continue;
                    else StdOut.printf("%1.4f\n", distance);                    
                }                 
            }            
        }
    }
}
