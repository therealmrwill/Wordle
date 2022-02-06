import java.io.PrintWriter;
import java.net.CacheRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


/**
 * App
 */
public class App {

    public static void main(String[] args) {
        String baseFileString = "FullWordList.txt";
        String invalidFile = "DebugFiles/InvalidWords.txt";
        String validFile = "DebugFiles/ValidWords.txt";
       
        WordSolver wordSolver = new WordSolver(baseFileString);

        //TODO: If score array is updated re-run the data sorted

        //Enter a word and the program will figure out if it is attainable or not
        wordSolver.wordFindable("Word here");

        System.out.println("Completed!");
        
        
    }

    

}


   