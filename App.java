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

        //Enter a word and the program will figure out if it is attainable or not
        //I don't think it runs fully at the moment though
        System.out.println(wordSolver.wordFindable("ROCKS"));

        System.out.println("Completed!");
        
        
    }

    private static void debugFilePrinter(List<String> orderedValidWordMap, String scoreOrderedFile) {
        PrintWriter pw = null;
            final int LINE_LENGTH = 20;
            int posInLine = 0;
    
            try {
                pw = new PrintWriter(scoreOrderedFile);
    
                for(int i = orderedValidWordMap.size() - 1; i >= 0; i--){
                    if(posInLine % LINE_LENGTH == 0 && posInLine != 0){
                        pw.println("");
                        posInLine++;
                    }
                    else{
                        posInLine++;
                    }
    
                    if(orderedValidWordMap.get(i) != null){
                        pw.print(orderedValidWordMap.get(i) + "  ");
                    }
                    else{
                        pw.print("*Error*");
                    }
    
                }
    
            } catch (Exception e) {
                System.err.println("Uh Oh");
                e.printStackTrace();
            } finally{
                if(pw != null){
                    pw.close();
                }   
            }



    }

}


   