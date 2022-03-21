import java.security.GeneralSecurityException;
import java.util.ArrayList;

/**
 * App
 */
public class App {
    public static final int WORD_LENGTH = 5;
    public static final int NUM_OF_ROUNDS = 6;
    public static final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final int DEBUG_LEVEL = 1;

    //Yes I know the yellow is not yellow - I just didn't want to blind myself while testing
    public static final String ANSI_YELLOW = "\u001b[35m";
    public static final String ANSI_GREEN = "\u001b[32m";
    public static final String ANSI_RED = "\u001b[31m";
    public static final String ANSI_RESET = "\u001b[0m";

    

    public static void main(String[] args) {
        Scoring scoreData = new Scoring(DataReader.readFromFile("FullWordList.txt"));
        Validity validityData = new Validity();
        AutoSolver solver = new AutoSolver(scoreData, validityData, DataReader.readFromFile("FullWordList.txt"));

        System.out.println(solver.RunWithData("TARES", 4));

    }




    

}


   