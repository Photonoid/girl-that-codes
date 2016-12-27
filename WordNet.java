import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.Bag;

public class WordNet {
    
    // need two separate RedBlackBSTs
    // RedBlackBST to map noun to indices
    private RedBlackBST<String, Bag<Integer>> nounsBST;
    // RedBlackBST to map indices to nouns
    private RedBlackBST<Integer, String> indexBST;
    private Digraph hypernymsDG;
    
    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) throw new
            java.lang.NullPointerException("Argument cannot be null.");
        
        // initialize symbol tables
        nounsBST = new RedBlackBST<String, Bag<Integer>>();
        indexBST = new RedBlackBST<Integer, String>();
        
        // parse synsets and add data into BSTs
        parseSynsets(synsets);
        
        // parse hypernyms and add data into digraph
        int size = indexBST.size();
        this.hypernymsDG = parseHypernyms(hypernyms, size);
    }
    
    // private helper method to parse synsets and create data type
    private void parseSynsets(String synsets) {
        // parse the synsets
        In inS = new In(synsets);
        int index = -1;                  
        
        while (!inS.isEmpty()) {
            // parse out the gloss from the synset
            String lineRead = inS.readLine();            
            String[] line = lineRead.split(",");
            
            // index of each separate synset
            index = Integer.parseInt(line[0]); 
            // nouns on each line
            String[] nouns = line[1].split(" ");
            // add noun to the index-indexed BST
            indexBST.put(index, line[1]);
            
            for (String noun : nouns) {                 
                // create a new bag of integers
                Bag<Integer> indexBag = new Bag<Integer>();
                // add nouns to the noun-indexed BST w/o repeating
                if (nounsBST.contains(noun)) {
                    indexBag = nounsBST.get(noun);
                    indexBag.add(index);
                    nounsBST.put(noun, indexBag);
                }
                else {                    
                    indexBag.add(index);
                    nounsBST.put(noun, indexBag);
                }
            }
        }
    }
    
    // private helper method to parse hypernyms and create digraph
    private Digraph parseHypernyms(String hypernyms, int size) {
        // create new digraph
        Digraph DG = new Digraph(size);
        
        // parse hypernyms
        In inH = new In(hypernyms);      
        while (!inH.isEmpty()) {
            String lineRead = inH.readLine();
            // put separate strings on each line in array
            String[] line = lineRead.split(",");  
            // parse out vertex at beginning of edge
            int v = Integer.parseInt(line[0]);
            
            for (int i = 1; i < line.length; i++) {
                // parse out vertex at end of edge
                int w = Integer.parseInt(line[i]);
                // add to hypernyms digraph
                DG.addEdge(v, w);
            }
        }
        return DG;
    }
    
    
    // all WordNet nouns
    public Iterable<String> nouns() {
        return nounsBST.keys();
    }
    
    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new
            java.lang.NullPointerException("Argument cannot be null.");
        
        if (nounsBST.contains(word)) return true;
        return false;
    }
    
    // a synset (second field of synsets.txt) that is a shortest common ancestor
    // of noun1 and noun2 (defined below)
    public String sca(String noun1, String noun2) {
        if (noun1 == null || noun2 == null) throw new
            java.lang.NullPointerException("Argument cannot be null.");
        if (!isNoun(noun1) || !isNoun(noun2)) throw new 
            java.lang.IllegalArgumentException("Invalid argument.");
        
        // create new SCA with digraph of hypernyms
        ShortestCommonAncestor sca = new ShortestCommonAncestor(hypernymsDG);
        
        // get the integer indices of noun1 and noun2
        Bag<Integer> n1 = nounsBST.get(noun1);
        Bag<Integer> n2 = nounsBST.get(noun2);
        
        // find the integer that is the SCA of n1 and n2
        int ancestorInt = sca.ancestor(n1, n2);
        
        // return the noun associated with the integer
        return indexBST.get(ancestorInt);  
    }
    
    // distance between noun1 and noun2 (defined below)
    public int distance(String noun1, String noun2) {
        if (noun1 == null || noun2 == null) throw new
            java.lang.NullPointerException("Argument cannot be null.");
        if (!isNoun(noun1) || !isNoun(noun2)) throw new 
            java.lang.IllegalArgumentException("Invalid argument.");
        
        // create new SCA with digraph of hypernyms
        ShortestCommonAncestor sca = new ShortestCommonAncestor(hypernymsDG);
        
        // get the integer indices of noun1 and noun2
        Bag<Integer> n1 = nounsBST.get(noun1);
        Bag<Integer> n2 = nounsBST.get(noun2);
        
        // find the distance between n1 and n2;
        return sca.length(n1, n2); 
    }
    
    // do unit testing of this class
    public static void main(String[] args) {
        // test constructor
        WordNet wordNet = new WordNet(args[0], args[1]);
        
        // test isNoun
        StdOut.println("cinnamon is a Wordnet noun? " + wordNet.isNoun("cinnamon"));
        
        // test ancestor
        String sca = wordNet.sca("individual", "edible_fruit");
        StdOut.println("SCA of individual and edible_fruit is: " + sca);        
        
        // test distance
        int distance = wordNet.distance("white_marlin", "mileage");
        StdOut.println("SCA of white_marlin and mileage is: " + distance);
    }
}
