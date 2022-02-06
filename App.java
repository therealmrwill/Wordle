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
        String unsolvableFile = "DebugFiles/UnsolvableWords.txt";
       
        WordSolver wordSolver = new WordSolver(unsolvableFile);

        //TODO: If score array is updated re-run the data sorted
        //I would recommend running it on the unsolvable list first, to see if it removes any
        //Current unsolvable word list size 1,431
        wordSolver.runDataTest();

        //Enter a word and the program will figure out if it is attainable or not
        //wordSolver.wordFindable("STING");

        
        System.out.println("Completed!");
        
        
    }

    

}


   