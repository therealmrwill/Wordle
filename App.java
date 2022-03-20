import java.io.File;
import java.io.PrintWriter;
import java.net.CacheRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;


/**
 * App
 */
public class App {
    public static final int WORD_LENGTH = 5;
    public static final int NUM_OF_ROUNDS = 6;
    public static final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    
    public static void main(String[] args) {
        Scoring.init();
        System.out.println(Scoring.getCurrentInfo());
        
        
    }



    

}


   