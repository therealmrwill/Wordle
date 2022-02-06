import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class WordSolver {
    //* Unchanged Params
    private static final int WORD_LENGTH = 5;
    private static final int NUM_OF_GUESSES = 6;


    //* WordChecker instance 
    private WordChecker wordChecker;

    //* Testing Params
    private List<String> baseWordMap;
    private int baseWordMapSize;
    private double dataListSize;
    

    //* Output Params
    private Map<String, Integer> findableWordMap;
    private List<String> undetectedWordList;
    private List<String> findableWordList;


    //* Constructors
    public WordSolver(String originFileName){
        // Creating a new instance of the WordChecker class
        this.wordChecker = new WordChecker(originFileName);

        System.out.println("WordChecker constructed");

        //Testing Params
        this.baseWordMap = this.wordChecker.getCurrentValidWordMap();
        this.baseWordMapSize = this.wordChecker.getValidMapSize();

        //Output Params
        this.findableWordMap = new HashMap<>();
        this.undetectedWordList = new ArrayList<>();
        this.findableWordList = new ArrayList<>();

    } 
    public WordSolver(List<String> dataList){
        // Creating a new instance of the WordChecker class
        this.wordChecker = new WordChecker(dataList);

        System.out.println("WordChecker constructed");

        //Testing Params
        this.baseWordMap = this.wordChecker.getCurrentValidWordMap();
        this.baseWordMapSize = this.wordChecker.getValidMapSize();

        //Output Params
        this.findableWordMap = new HashMap<>();
        this.undetectedWordList = new ArrayList<>();
        this.findableWordList = new ArrayList<>();

    }

    //* Data Setters
    //TODO: Create Setters


    //* Data Getters
    //Todo: Create Getters


    //* Main Public Methods
    public boolean runDataTest(List<String> dataToTest){
       this.dataListSize = dataToTest.size(); 
       for (String wordToFind : dataToTest) {
           boolean wordFound = wordFindable(wordToFind);
           if(wordFound){
                this.findableWordList.add(wordToFind);
           }else{
                this.undetectedWordList.add(wordToFind);
           }

           


           //? Line is just for debug purposes
           System.out.println("Word:" + wordToFind + " sorted");
        }

        dataTestResultsPrinter();       
       


       if(this.findableWordMap.size() == this.baseWordMapSize){
           return true;
       }
       else{
           return false;
       }

    }

    private void dataTestResultsPrinter() {
        System.out.println("\nData Test Completed!");

        System.out.println("\nResults:");

        System.out.println("Number of Detectable words: " + this.findableWordList.size());
        System.out.println("Number of Undetectable words: " + this.undetectedWordList.size());
        System.out.printf("Percentage of words not detected: %.2f%% ", (float)(this.undetectedWordList.size() / this.dataListSize) * 100);
        System.out.print("\n");

        if(!this.findableWordList.isEmpty()){
            System.out.println("\nDetectable Word Results:");
            Map<Integer, Integer> wordsByRound = wordsByRoundDataGetter();        
            Set<Map.Entry<Integer, Integer>> set = wordsByRound.entrySet();
            Iterator<Map.Entry<Integer, Integer>> i = set.iterator();
            while(i.hasNext()){
                Entry<Integer, Integer> currentData = i.next();
                System.out.print("Words found in round " + currentData.getKey() + ": " + currentData.getValue());
                System.out.printf(" - %.2f %%", (float)(currentData.getValue() / (double)this.findableWordList.size()) * 100);
                System.out.print("\n");
            }
        }
        
        debugFilePrinter(this.findableWordList, "DebugFiles/DetectableWords.txt");
        debugFilePrinter(this.undetectedWordList, "DebugFiles/UndetectableWords.txt");
        
        System.out.println("\nFile with list of detectable words: DetectableWords.txt");
        System.out.println("File with list of undetectable words: UndetectableWords.txt");

    }

    private Map<Integer, Integer> wordsByRoundDataGetter() {
        Map<Integer, Integer> wordsByRound = new HashMap<>();
        
        for(int i = 0; i < NUM_OF_GUESSES; i++){
            wordsByRound.put(i+1, 0);
        }

        Set<Map.Entry<String, Integer>> set = this.findableWordMap.entrySet();
        Iterator<Map.Entry<String, Integer>> i = set.iterator();

        while(i.hasNext()){
            int currentWordRoundNum = i.next().getValue();
            wordsByRound.put(currentWordRoundNum, (wordsByRound.get(currentWordRoundNum) + 1));
        }

        return wordsByRound;
    }

    public boolean wordFindable(String correctWord) {
        this.wordChecker = new WordChecker(baseWordMap);
        for(int i = 0; i < NUM_OF_GUESSES; i++){
            String currentWord = getBestWord(i);

            if(currentWord != null && currentWord.equals(correctWord)){
                this.findableWordMap.put(currentWord, (i+1));
                return true;
            }
            else if(currentWord != null){
                //Update WordChecker
                updateWordChecker(currentWord, correctWord);   


                //? For debug purposes only
                //debugFilePrinter(this.wordChecker.getOrderedValidWordMap(), "DebugFiles/ValidWords.txt");
            }
        }


        return false;
    }

    private void updateWordChecker(String currentWord, String correctWord) {
        for(int i = 0; i < currentWord.length(); i++){
            String currentCharString = currentWord.substring(i, (i+1));
            char currentChar = currentWord.charAt(i);
            
            //! Could be an issue check if line above is reading correct data (should be single character)
            if(correctWord.contains(currentCharString)){
                if(currentChar == correctWord.charAt(i)){
                    this.wordChecker.addGreenChar(currentChar, i);
                }
                else{
                    this.wordChecker.addYellowChar(currentChar, i);
                }
            }
            else{
                this.wordChecker.addBlackChar(currentChar);
            }    
        }

        this.wordChecker.allDataUpdate();
    }

    private String getBestWord(int round) {
        //TODO: Make this have more parts to it
        if(this.wordChecker.getValidMapSize() > 0){
            return this.wordChecker.getOrderedValidWordMap().get(this.wordChecker.getValidMapSize() - 1);
        }
        else{
            System.out.println("Valid Map has size 0");
            return null;
        }
    }

    private static void debugFilePrinter(List<String> orderedValidWordMap, String scoreOrderedFile) {
        PrintWriter pw = null;
            final int LINE_LENGTH = 10;
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
