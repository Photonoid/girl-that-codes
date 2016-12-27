public class RingBuffer {
    private double[] rb;        // ring buffer array
    private int first;          // first entry
    private int last;           // last entry
    private int size;           // current size of rb
    private int capacity;       // largest index in array
      
    //  creates an empty ring buffer with the specified capacity
    public         RingBuffer(int capacity) {  
        rb = new double[capacity];
        first = 0;
        last = 0;
        size = 0;
        this.capacity = capacity; 
    }
    
    //  returns the number of items currently in this ring buffer
    public     int size() {                    
        return  size;     
    } 
        
    //  is this ring buffer empty (size equals zero)?
    public boolean isEmpty() {                 
        return size() == 0;
    }
    //  is this ring buffer full (size equals capacity)?
    public boolean isFull() {                  
        return size() == capacity;
    }
    
    //  adds item x to the end of this ring buffer
    public    void enqueue(double x) {         
        if (isFull()) 
            throw new RuntimeException("Buffer is full.");
        rb[last] = x;
        last++; 
        if (last == capacity) last = 0;    
        if (size != capacity) size++;
       }
    
    //  deletes and returns the item at the front of this ring buffer
    public  double dequeue() {                 
        if (isEmpty()) throw new RuntimeException("Buffer is empty.");
        double val = rb[first];
        rb[first] = 0.0; 
        first++;
        if (first == capacity) first = 0; 
        size--;
        return val;
        }
        
    //  returns the item at the front of this ring buffer
    public  double peek() {                   
        if (isEmpty()) throw new RuntimeException("Buffer is empty.");
        return rb[first];
    }

    public static void main(String[] args) {   //  unit tests this class
      int N = Integer.parseInt(args[0]); // capacity 
      RingBuffer buffer = new RingBuffer(N);      
      for (int i = 1; i <= N; i++) {
          buffer.enqueue(i);
      }
      double t = buffer.dequeue(); 
      buffer.enqueue(t);      
      System.out.println("Size after wrap-around is " + buffer.size()); 
      while (buffer.size() >= 2) {
          double x = buffer.dequeue();
          double y = buffer.dequeue();
          buffer.enqueue(x + y);
      }
      System.out.println(buffer.peek());
  }
}
 
       
