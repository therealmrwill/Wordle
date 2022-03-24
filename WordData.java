import java.util.ArrayList;
import java.util.HashMap;

public class WordData {
    //* Variables to make sure non-linkable
    private boolean finalData;

    private int round;
    private ValidityData validityData;
    private HashMap<Character, Letter> letterMap;
    private HashMap<Character, Letter> divMap;
    private HashMap<Integer, HashMap<Character, Letter>> posLetterMap;
    
    public WordData(HashMap<String, TestWord> wordData){
        this.finalData = false;

        this.round = 0;
        this.validityData = new ValidityData();
        this.letterMap = blankMap();
        this.divMap = blankMap();
        this.posLetterMap = new HashMap<>();

        for(int position = 0; position < App.WORD_LENGTH; position++){
            posLetterMap.put(position, blankMap());
        }
        
        this.fillMaps(wordData);
        
    }

        //* Base scoring helpers
        private HashMap<Character, Letter> blankMap(){
            HashMap<Character, Letter> dataOut = new HashMap<>();

            for(Character letter : App.alphabet.toCharArray()){
                dataOut.put(letter, new Letter(letter));
            }

            return dataOut;
        }
        private void fillMaps(HashMap<String, TestWord> wordData) {
            for(String word : wordData.keySet()){
                addWord(word);
            }
        }

    //* Public methods that allow for changing of data
    public void addWord(String word){
        updateAllMaps(word, 1.0);
    }
    public void removeWord(String word){
        updateAllMaps(word, -1.0);
    }

    //* Only method that truly will change any data other than validity
    //* Is protected for links and copies will not be able to be changed
    private void updateAllMaps(String word, Double changeAmount) {

        if(finalData = true){
            System.out.printf("Error in Scoring.updateAllMaps(%s, %.0f): Instance of scoring is marked final and cannot be changed %n", word, changeAmount);
            return;
        }

        if(word.length() != App.WORD_LENGTH){
            System.out.printf("Error in Scoring.updateAllMaps(%s, %.1f): Word size %d invalid %n", word, changeAmount, word.length()); 
            return;
        }

        ArrayList<Character> prevChars = new ArrayList<>();

        for(int position = 0; position < word.length(); position++){
            Character curChar = word.charAt(position);

            letterMap.get(curChar).changeScore(changeAmount);
            posLetterMap.get(position).get(curChar).changeScore(changeAmount);

            if(!prevChars.contains(curChar)){
                divMap.get(curChar).changeScore(changeAmount);
            }
        } 
    }


    //* Constructor which is only used to create a copy of a previous scoring data
    //* Only real use should be so that I don't have to rescore all words at the beginning of every new Solve
    private WordData(boolean finalData, int round, ValidityData validityData, HashMap<Character, Letter> letterMap, HashMap<Character, Letter> divMap, HashMap<Integer, HashMap<Character, Letter>> posLetterMap){
        this.finalData = finalData;
        this.round = round;
        this.validityData = validityData;
        this.letterMap = new HashMap<>(letterMap);
        this.divMap = new HashMap<>(divMap);
        this.posLetterMap = new HashMap<>(posLetterMap);
    }



    //*Getters and Setters
    public WordData getLockedCopy(){
        return new WordData(true, this.round + 0, this.validityData.finalCopy(), new HashMap<>(this.letterMap), new HashMap<>(this.divMap), new HashMap<>(this.posLetterMap));
    }
    public WordData getCopy(){ 
        return new WordData(false, this.round + 0, validityData.finalCopy(), new HashMap<>(this.letterMap), new HashMap<>(this.divMap), new HashMap<>(this.posLetterMap));
    }
    public void addRound(){
        if(finalData == true){
            System.out.println("Error in Scoring.addRound(): Instance of scoring is marked as final");
            return;
        }

        this.round++;
    }
    public void setValidityData(ValidityData validityData){
        if(finalData == true){
            System.out.println("Error in Scoring.resetValidityData(): Instance of scoring is marked as final");
            return;
        }

        this.validityData = validityData.finalCopy();
    }
    public void setFinal(){
        this.finalData = true;
    }


    //* Main use of this whole class
    //* Should be the main focus of any changes once this whole thing is finally functioning
    //* Should has all known data to score provided - should be able to think of tons of combinations
    public double scoreWord(TestWord word){
        double score = 0;
        ArrayList<Character> prevChars = new ArrayList<>();
        String wordString = new String(word.getName());

        for(int position = 0; position < wordString.length(); position++){
            int charScore = 0;
            char curChar = wordString.charAt(position);
            
            charScore += this.letterMap.get(curChar).getScore();
            charScore += this.divMap.get(curChar).getScore();
            charScore += this.posLetterMap.get(position).get(curChar).getScore();

            if(prevChars.contains(curChar)){
                charScore /= 1.5;
            }

            prevChars.add(curChar);
            score += charScore;

        }

        if(validityData.isValid(word) == false){
            score = 0;
        }

        return score;
    }

   


    //* Creates a string of data to store for later usage 
    //* Super helpful for debugging when we have bigger classes using this
    //* Cases for quick reference: toString
    public String saveInfo(String dataLevel){
        String dataOut = "";

        switch(dataLevel){

        }


        return dataOut;
    }

    @Override
    public String toString(){
        return saveInfo("toString");
    }
}
