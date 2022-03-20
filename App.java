import java.util.ArrayList;
import java.util.Collections;


/**
 * App
 */
public class App {
    public static final int WORD_LENGTH = 5;
    public static final int NUM_OF_ROUNDS = 6;
    public static final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    
    public static void main(String[] args) {
        Scoring.init(DataReader.readFromFile("FullWordList.txt"));
        System.out.println(Scoring.getCurrentInfo());
        System.out.println("Completed");
    }




    

}


   