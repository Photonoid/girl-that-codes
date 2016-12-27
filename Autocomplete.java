import java.util.Arrays;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Autocomplete {
    
    // create immutable private instance variable
    private final Term[] terms;
    
    // Initializes the data structure from the given array of terms.
    public Autocomplete(Term[] terms) {
        // initiate instance variables
        this.terms = terms;
        
        // throw exception if argument array is null
        if (terms == null) throw new java.lang.NullPointerException(); 
        
        // sort terms array in lexicographic order
        Arrays.sort(terms); 
    }
    
    // Returns all terms that start with the given prefix, 
    // in descending order of weight.
    public Term[] allMatches(String prefix) {
        // throw exception if prefix string is null
        if (prefix == null) throw new java.lang.NullPointerException();
        
        // create new term using given prefix
        Term prefixTerm = new Term(prefix, Integer.MAX_VALUE);
        
        // binary search for the index of the first term with the given term
        int firstTerm = BinarySearchDeluxe.firstIndexOf(terms, 
                       prefixTerm, Term.byPrefixOrder(prefix.length()));
        // binary search for the index of the last term with the given term
        int lastTerm = BinarySearchDeluxe.lastIndexOf(terms, 
                       prefixTerm, Term.byPrefixOrder(prefix.length()));
        // find the length different between the first and last terms
        int startTerm = firstTerm;
        int length;
        if (firstTerm == -1 && lastTerm == -1) {
            length = 0;
        }
        else {
            length = lastTerm - firstTerm + 1; }      
        
        // create a new term array with calculated length and input terms 
        Term[] newTerms = new Term[length];
        for (int i = 0; i < length; i++) {
            newTerms[i] = terms[startTerm];
            startTerm++;
        }
        Arrays.sort(newTerms, Term.byReverseWeightOrder());
        return newTerms;          
    }
    
    // Returns the number of terms that start with the given prefix.
    public int numberOfMatches(String prefix) {
        // throw exception if prefix string is null
        if (prefix == null) throw new java.lang.NullPointerException();
        
        // create new term using given prefix
        Term prefixTerm = new Term(prefix, Integer.MAX_VALUE);
        
        // binary search for the index of the first term with the given term
        int firstTerm = BinarySearchDeluxe.firstIndexOf(terms, 
                        prefixTerm, Term.byPrefixOrder(prefix.length()));
        // binary search for the index of the last term with the given term
        int lastTerm = BinarySearchDeluxe.lastIndexOf(terms, 
                        prefixTerm, Term.byPrefixOrder(prefix.length()));
        // find the length different between the first and last terms
        int length;
        if (firstTerm == -1 && lastTerm == -1) {
            length = 0;
        }
        else {
            length = lastTerm - firstTerm + 1; }         
        return length;
    }
    
    // unit testing(required)
    public static void main(String[] args) {
        // read in the terms from a file
        String filename = args[0];
        In in = new In(filename);
        int N = in.readInt();
        Term[] terms = new Term[N];
        for (int i = 0; i < N; i++) {
            // read the next weight
            long weight = in.readLong();           
            // scan past the tab
            in.readChar();                         
            // read the next query
            String query = in.readLine();          
            // construct the term
            terms[i] = new Term(query, weight);    
        }
        
        // read in queries from standard input and print out the top k matching terms
        int k = Integer.parseInt(args[1]);
        Autocomplete autocomplete = new Autocomplete(terms);
        while (StdIn.hasNextLine()) {
            String prefix = StdIn.readLine();
            Term[] results = autocomplete.allMatches(prefix);
            for (int i = 0; i < Math.min(k, results.length); i++)
                StdOut.println(results[i]);
        }
    }
}
   
