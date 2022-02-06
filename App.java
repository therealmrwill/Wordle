import java.io.PrintWriter;
import java.util.List;


/**
 * App
 */
public class App {

    public static void main(String[] args) {
        String baseFileString = "DebugTestFiles/FullWordList.txt";
        String invalidFile = "DebugTestFiles/InvalidWords.txt";
        String validFile = "DebugTestFiles/ValidWords.txt";

        WordChecker wordChecker = new WordChecker(baseFileString);

        wordChecker.addWord("AEROS", "GBBYB");

        debugFilePrinter(wordChecker.getOrderedValidWordMap(), validFile);
        debugFilePrinter(wordChecker.getOrderedInvalidWordMap(), invalidFile);
        

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


   