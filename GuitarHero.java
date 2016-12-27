public class GuitarHero {
      public static void main(String[] args) {
          // create keyboard
          String keyboard = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' "; 
          int length = keyboard.length();
         
          // create new GuitarString object to simulate guitar
          // string being plucked 
          GuitarString[] string = new GuitarString[length];
          for (int i = 0; i < length; i++) 
              string[i] = 
              new GuitarString(440 * Math.pow(2, ((double) (i - 24) / 12)));
         
          // recognize next character/note to be played
          while (true) {
              if (StdDraw.hasNextKeyTyped()) {
                  char key = StdDraw.nextKeyTyped();
                  for (int i = 0; i < length; i++) {
                      if (i == keyboard.indexOf(key)) string[i].pluck(); 
                  }
              }
              
              // superpositions all samples
              double sample = 0.0;
              for (int i = 0; i < length; i++) {
                  sample = sample + string[i].sample();
              }
              // play the (super)sample on standard audio
              StdAudio.play(sample);
  
              // advance the simulation of each guitar string by one step
              for (int i = 0; i < length; i++) {
                  string[i].tic();
              }
          }
       }
  }
