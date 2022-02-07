import java.io.File;
import java.io.PrintWriter;
import java.net.CacheRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


/**
 * App
 */
public class App {
    private static final int WORD_LENGTH = 5;
    public static void main(String[] args) {
        String baseFileString = "FullWordList.txt";
        String invalidFile = "DebugFiles/InvalidWords.txt";
        String validFile = "DebugFiles/ValidWords.txt";
        String unsolvableFile = "DebugFiles/OriginUndetectableWords.txt";
       
        WordSolver wordSolver = new WordSolver(fileToArray(baseFileString));

        //TODO: If score array is updated re-run the data sorted
        //Current unsolvable word list size 1,431
        //wordSolver.runDataTest(fileToArray(unsolvableFile));
        wordSolver.runDataTest(fileToArray(baseFileString));

        //Enter a word and the program will figure out if it is attainable or not

        
        System.out.println("Completed!");
        
        
    }

    private static List<String> fileToArray(String baseFileString) {
        List<String> wordsInArray = new ArrayList<>();
        File fileToScan = new File(baseFileString);
        Scanner fScanner = null;

        try{
            fScanner = new Scanner(fileToScan);

            while(fScanner.hasNext()){
                String currentWord = fScanner.next();
                if(currentWord.length() == WORD_LENGTH){
                    wordsInArray.add(currentWord);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(fScanner != null){
                fScanner.close();
            }
        }



        return wordsInArray;
    }

    

}


   