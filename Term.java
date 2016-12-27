
import java.util.Arrays;
import java.util.Comparator;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Term implements Comparable<Term> {
    
    private final String query;
    private final long weight;
    
    // Initializes a term with the given query string and weight.
    public Term(String query, long weight) {
        this.query = query;
        this.weight = weight;
        
        // throw exception if query string is null
        if (query == null) throw new java.lang.NullPointerException();
        // throw exception if weight is negative
        if (weight < 0) throw new java.lang.IllegalArgumentException(); 
    }    
    
    // Compares the two terms in descending order by weight.
    public static Comparator<Term> byReverseWeightOrder() {
        return new ReverseWeightOrder();
    }
    
    // Helper reverse-weight-order comparator
    private static class ReverseWeightOrder implements Comparator<Term> {
        public int compare(Term a, Term b) {
            long diff = b.weight - a.weight;
            return (int) diff;            
        }
    }
    
    // Compares the two terms in lexicographic order 
    // using only the first r characters of each query.
    public static Comparator<Term> byPrefixOrder(int r) { 
        return new PrefixOrder(r);
    }
    
    // Helper prefix-order comparator.    
    private static class PrefixOrder implements Comparator<Term> {
        private final int r;        
        // throw exception if r is negative 
        public PrefixOrder(int r) {
            this.r = r;
            if (r < 0) throw new java.lang.IllegalArgumentException();
        }
        
        public int compare(Term a, Term b) {
            // get the lengths of each term's query
            int aLength = a.query.length();
            int bLength = b.query.length(); 
            
            // compare the queries in the four different cases 
            if (aLength < r && bLength < r) return a.query.compareTo(b.query);
            else if (aLength < r) return a.query.compareTo(b.query.substring(0, r));
            else if (bLength < r) return a.query.substring(0, r).compareTo(b.query);
            else return a.query.substring(0, r).compareTo(b.query.substring(0, r));
        }
    }
    
    
    // Compares the two terms in lexicographic order by query.
    public int compareTo(Term that) {        
        return this.query.compareTo(that.query);
    }
    
    private static void printTerms(Term[] terms) {
        for (int i = 0; i < terms.length; i++) {
            StdOut.println(terms[i]);
        }
    }
    
    // Returns a string representation of this term in the following format:
    // the weight, followed by a tab, followed by the query.
    public String toString() {
        return Long.toString(this.weight) + "\t" + this.query;
    } 
    
    // unit testing (required)
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
        
        // print terms as given in text
        for (Term term : terms) {
            StdOut.println(term);
        }
        StdOut.println();
        
        // sort and print terms by prefix order
        // with the first 5 characters
        Arrays.sort(terms, Term.byPrefixOrder(5));
        for (Term term : terms) {
            StdOut.println(term);
        }
        StdOut.println();
        
        // sort and print terms by reverse weight order
        Arrays.sort(terms, Term.byReverseWeightOrder());
        for (Term term : terms) {
            StdOut.println(term);
        }
        StdOut.println();       
        
        // sort and print terms by lexicographic order
        Arrays.sort(terms);
        for (Term term : terms) {
            StdOut.println(term);
        }        
    }
}
        
