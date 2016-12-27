public class MarkovModel {
    // define instance variables
    // define length of ASCII alphabet
    private static final int ASCII = 128;
    // create symbol table
    private ST<String, ST<Character, Integer>> st = 
        new ST<String, ST<Character, Integer>>();
    // create variable k (order)
    private int k;
    
    // creates a Markov model of order k for the specified text
    public MarkovModel(String text, int k) {   
        // initiate k order
        this.k = k;
        
        // add on kgram to text
        String circText = text + text.substring(0, k);
        
        for (int i = 0; i < text.length(); i++) {
            // set kgram
            String kgram = circText.substring(i, i+k);  
            
            // if the symbol table has the kgram
            if (st.contains(kgram)) {
                // get inner symbol table (iST)
                ST<Character, Integer> iST = st.get(kgram);
                // get character after kgram
                char currentChar = circText.charAt(i+k);
                
                // if iST has character, increment frequency
                if (iST.contains(currentChar)) {
                    int val = iST.get(currentChar) + 1;
                    iST.put(currentChar, val);
                }
                
                // if doesn't have character, add in
                else iST.put(currentChar, 1);
            }
            
            // if the symbol table doesn't have the kgram
            else {
                ST<Character, Integer> iST = new ST<Character, Integer>();
                char currentChar = circText.charAt(i+k);
                int val = 1;
                iST.put(currentChar, val);
                st.put(kgram, iST);               
            }
        }
    }
                
          
    // returns the order k of this Markov model
    public int order() {
       return k;
    }

    // returns the number of times the specified kgram appears in the text
    public int freq(String kgram) {
       int sum = 0;
       if (kgram.length() != k) 
           throw new RuntimeException("kgram not of order k.");     
       else if (st.get(kgram) == null) return 0;
       else {
           ST<Character, Integer> iST = st.get(kgram);           
           for (char c : iST) sum = sum + iST.get(c);
           }
       return sum;
    }

    // returns the number of times the character c follows the specified
    // kgram in the text
    public int freq(String kgram, char c) {
        int repeat = 0;
        if (kgram.length() != k) 
            throw new RuntimeException("kgram not of order k.");   
        else if (st.get(kgram) == null) return 0;
        else {
            ST<Character, Integer> iST = st.get(kgram);
            if (iST.get(c) == null) return 0;
            else repeat = iST.get(c);
            return repeat;
        }
    }
        
    // returns a random character that follows the specified kgram in the text,
    // chosen with weight proportional to the number of times that character
    // follows the specified kgram in the text
       public char random(String kgram) {
           if (kgram.length() != k) 
               throw new RuntimeException("kgram not of order k."); 
           else if (!st.contains(kgram)) 
               throw new RuntimeException("kgram not in text.");
           else {
            // create integer array with length of ASCII alphabet's length
            int[] charCount = new int[ASCII];
            // use enhanced for loop to put frequency of each ASCII character 
            // in array for this kgram
                ST<Character, Integer> iST = st.get(kgram);
           for (char c : iST) {
                    charCount[c] = iST.get(c);  
                }
               char r = (char) StdRandom.discrete(charCount);
               return r;
           }
        }

    // unit tests this class
       public static void main(String[] args) {
           String text1 = "banana";
           MarkovModel model1 = new MarkovModel(text1, 2);
           StdOut.println("freq(\"an\", 'a')    = " + model1.freq("an", 'a'));
           StdOut.println("freq(\"na\", 'b')    = " + model1.freq("na", 'b'));
           StdOut.println("freq(\"na\", 'a')    = " + model1.freq("na", 'a'));
           StdOut.println("freq(\"na\")         = " + model1.freq("na"));
           StdOut.println();

           String text2 = "one fish two fish red fish blue fish"; 
           MarkovModel model2 = new MarkovModel(text2, 4);
           StdOut.println("freq(\"ish \", 'r') = " + model2.freq("ish ", 'r'));
           StdOut.println("freq(\"ish \", 'x') = " + model2.freq("ish ", 'x'));
           StdOut.println("freq(\"ish \")      = " + model2.freq("ish "));
           StdOut.println("freq(\"tuna\")      = " + model2.freq("tuna"));
       }
}
