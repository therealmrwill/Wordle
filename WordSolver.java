import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordSolver {
    //* Unchanged Params
    private static final int WORD_LENGTH = 5;
    private static final int NUM_OF_GUESSES = 6;


    //* WordChecker instance 
    private WordChecker wordChecker;

    //* Testing Params
    private List<String> baseWordMap;
    private int baseWordMapSize;
    

    //* Output Params
    private Map<String, Integer> findableWordMap;
    private List<String> undetectedWordList;


    //* Constructors
    public WordSolver(String originFileName){
        // Creating a new instance of the WordChecker class
        this.wordChecker = new WordChecker(originFileName);

        //Testing Params
        this.baseWordMap = this.wordChecker.getCurrentValidWordMap();
        this.baseWordMapSize = this.wordChecker.getValidMapSize();

        //Output Params
        this.findableWordMap = new HashMap<>();
        this.undetectedWordList = new ArrayList<>();

    }

    //TODO: Create a second constructor with an input of an ArrayList 


    //* Data Setters
    //TODO: Create Setters


    //* Data Getters
    //Todo: Create Getters


    //* Main Public Methods
    public boolean runDataTest(){
       for (String wordToFind : this.baseWordMap) {
           if(!wordFindable(wordToFind)){
            this.undetectedWordList.add(wordToFind);
           }

           //Line is just for debug purposes
           System.out.println("Word:" + wordToFind + " sorted");
       }

       if(this.findableWordMap.size() == this.baseWordMapSize){
           return true;
       }
       else{
           return false;
       }

    }

    public boolean wordFindable(String correctWord) {
        this.wordChecker = new WordChecker(baseWordMap);
        for(int i = 0; i < NUM_OF_GUESSES; i++){
            String currentWord = getBestWord(i);

            if(currentWord != null && currentWord.equals(correctWord)){
                this.findableWordMap.put(currentWord, i);
                return true;
            }
            else if(currentWord != null){
                //Update WordChecker
                updateWordChecker(currentWord, correctWord);                
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

   
}