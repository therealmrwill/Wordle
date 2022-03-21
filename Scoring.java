import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Scoring {
    private int round;
    private Validity validityData;
    private ArrayList<Character> letterOrdering;

    private HashMap<Integer, Letter> rankedMap;
    private HashMap<Integer, Letter> rankedDivMap;
    private ArrayList<HashMap<Integer, Letter>> rankedPosMap;
    
    public Scoring(ArrayList<Word> data){
        this.round = 0;
        this.validityData = new Validity();

        for(Character newChar : App.alphabet.toCharArray()){
            letterOrdering.add(newChar);
        }

        this.rankedMap = blankMap();
        this.rankedDivMap = blankMap();

        this.rankedPosMap = new ArrayList<>();
        for(int position = 0; position < App.WORD_LENGTH; position++){
            this.rankedPosMap.add(blankMap());
        }

        addWordList(data);
    }

    public void addWordList(ArrayList<Word> data) {
        for (Word word : data) {
            changeScores(word, 1.0);  
        }
            
    }

    private void changeScores(Word word, double changeAmount){
        if(word.getWord().length() == App.WORD_LENGTH){
            ArrayList<Character> previousCharacters = new ArrayList<>();

            for(int position = 0; position < word.getWord().length(); position++){
                Character currChar = word.getWord().charAt(position);

                rankedMap.get(letterOrdering.indexOf(currChar)).UpdateScore(changeAmount);

                rankedPosMap.get(position).get(letterOrdering.indexOf(currChar)).UpdateScore(changeAmount);

                if(!previousCharacters.contains(currChar)){
                    rankedDivMap.get(letterOrdering.indexOf(currChar)).UpdateScore(changeAmount);
                }

                previousCharacters.add(currChar);
            }

        }else{
            System.out.println("Error in Scoring.changeScores(): Invalid Word: " + word);
        }
    }

    private HashMap<Integer, Letter> blankMap(){
        HashMap<Integer, Letter> dataOut = new HashMap<>();

        for(Character letter : App.alphabet.toCharArray()){
            dataOut.put(dataOut.size(), new Letter(letter));
        }

        return dataOut;
    }

    //* Needed Methods
    public void removeWord(Word word){
        changeScores(word, -1.0);
    }

    public double scoreWord(String word){
        return -1.0;
    }

    public void setRound(int newRound){
        this.round = newRound;
    }

    public void setValidity(Validity newValidityData){
        this.validityData = newValidityData;
    }

    @Override
    public String toString(){
        String dataOut = "";


        return dataOut;
    }
}
