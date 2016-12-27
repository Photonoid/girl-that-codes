public class GuitarString {
    private int N;
    private RingBuffer buffer;
    
    // creates a guitar string of the specified frequency,
    // using sampling rate of 44,100
    public GuitarString(double frequency) {
        N = (int) Math.ceil(44100 / frequency);
        buffer = new RingBuffer(N);
        for (int i = 0; i < N; i++) 
            buffer.enqueue(0.0);
    }

    // creates a guitar string whose size and initial values are given by
    // the specified array
    public GuitarString(double[] init) {
        N = init.length;
        buffer = new RingBuffer(N);
        for (int i = 0; i < N; i++) {
            buffer.enqueue(init[i]);
        }    
    }

    // plucks the guitar string (by replacing the buffer with white noise)
     public void pluck() {      
        for (int i = 0; i < N; i++) {
            double val = StdRandom.uniform(-0.5, 0.5);
            buffer.dequeue();
            buffer.enqueue(val);
        }
    }

    // advances the Karplus-String simulation one time step
    public void tic() {
        double val1 = buffer.dequeue();
        double val2 = buffer.peek();
        double val = 0.996 * (val1 + val2) / 2.0;
        buffer.enqueue(val);
    }

    // returns the current sample
    public double sample() {
        return buffer.peek();
    }

    // unit tests this class
    public static void main(String[] args) {
      int N = Integer.parseInt(args[0]);
      double[] samples = { 0.2, 0.4, 0.5, 0.3, -0.2, 0.4, 0.3, 0.0, -0.1, -0.3 };  
      GuitarString testString = new GuitarString(samples);
      for (int i = 0; i < N; i++) {
          double sample = testString.sample();
          System.out.printf("%6d %8.4f\n", i, sample);
          testString.tic();
      }
  }

}

    
