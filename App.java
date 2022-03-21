import java.io.File;
import java.io.PrintWriter;
import java.security.GeneralSecurityException;
import java.util.ArrayList;

/**
 * App
 */
public class App {
    public static final int WORD_LENGTH = 5;
    public static final int NUM_OF_ROUNDS = 6;
    public static final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final int DEBUG_LEVEL = 4;

    //Yes I know the yellow is not yellow - I just didn't want to blind myself while testing
    public static final String ANSI_YELLOW = "\u001b[35m";
    public static final String ANSI_GREEN = "\u001b[32m";
    public static final String ANSI_RED = "\u001b[31m";
    public static final String ANSI_RESET = "\u001b[0m";

    

    public static void main(String[] args) {
        int correct = 0;
        int incorrect = 0;
        ArrayList<Word> dataList = DataReader.readFromFile("FullWordList.txt");
        
                

        try {
            PrintWriter pw = new PrintWriter(new File("DebugFiles/SolveAll.txt"));           
            ArrayList<Word> wordList = DataReader.readFromFile("FullWordList.txt");


            for(Word word : wordList){
                Scoring scoreData = new Scoring(dataList);
                Validity validityData = new Validity();
                AutoSolver currentSolver = new AutoSolver(scoreData, validityData, DataReader.readFromFile("FullWordList.txt"));
                String data = currentSolver.RunWithData(word.getWord(), DEBUG_LEVEL);
                if(currentSolver.solved){
                    correct += 1;
                }else{
                    incorrect += 1;
                }
                pw.println(data);
                System.out.println(data);

            }
            
            pw.close();

        } catch (Exception e){ 
            e.printStackTrace();
        }

        
        System.out.println("Correct: " + correct);
        System.out.println("Incorrect " + incorrect);

    }




    

}


   