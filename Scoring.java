import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Scoring {
    private int uniqueId;
    private int round;
    private int roundsLeft;
    private ValidityTests validityData;
    private ArrayList<Character> letterOrdering;
    

    private HashMap<Integer, Letter> rankedMap;
    private HashMap<Integer, Letter> rankedDivMap;
    private ArrayList<HashMap<Integer, Letter>> rankedPosMap;
    
    public Scoring(ArrayList<Word> data){
        this.uniqueId = 0;
        this.round = 0;
        this.roundsLeft = App.NUM_OF_ROUNDS - round;
        this.validityData = new ValidityTests();
        this.letterOrdering = new ArrayList<>();

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

    public Scoring(Scoring scoringData){
        this.uniqueId = scoringData.uniqueId + 1;
        this.round = scoringData.round;
        this.roundsLeft = scoringData.roundsLeft;
        this.validityData = new ValidityTests(scoringData.validityData);
        this.letterOrdering = scoringData.letterOrdering;
        this.rankedMap = scoringData.rankedMap;
        this.rankedDivMap = scoringData.rankedDivMap;
        this.rankedPosMap = scoringData.rankedPosMap;
    }

    public String getValidity(){
        return validityData.toString();
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

                rankedMap.get(getIndex(currChar)).UpdateScore(changeAmount);

                rankedPosMap.get(position).get(getIndex(currChar)).UpdateScore(changeAmount);

                if(!previousCharacters.contains(currChar)){
                    rankedDivMap.get(getIndex(currChar)).UpdateScore(changeAmount);
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

    private int getIndex(Character letter){
        return letterOrdering.indexOf(letter);
    }

    //* Needed Methods
    public void removeWord(Word word){
        changeScores(word, -1.0);
    }

    public double scoreWord(String word){
        //* Definitely will need a hard re-build, just not tonight
        double score = 0;
        ArrayList<Character> previouCharacters = new ArrayList<>();

        for(int position = 0; position < word.length(); position++){
            int charScore = 0;
            char curChar = word.charAt(position);
            int index = getIndex(curChar);

            charScore += rankedMap.get(index).getScore();
            charScore += rankedDivMap.get(index).getScore();
            charScore += rankedPosMap.get(position).get(index).getScore();

            if(previouCharacters.contains(curChar)){
                charScore /= 2;
            }

            score += charScore;
            previouCharacters.add(curChar);

        }

        if(validityData.isValid(word) == false){
            score = 0;
        }

        return score;
    }

    public void setRound(int newRound){
        this.round = newRound;
        this.roundsLeft = App.NUM_OF_ROUNDS - round;
    }

    public void setValidity(ValidityTests newValidityData){
        this.validityData = new ValidityTests(newValidityData);
    }

    public int getRound(){
        return this.round;
    }

    @Override
    public String toString(){
        String dataOut = "";

        dataOut += "\nScoring Data: ";
        
        dataOut += "\nRound #" + this.round + ": rounds left: " + this.roundsLeft;


        //* All of this is to order the Letter data
        ArrayList<Letter> orderedList = new ArrayList<>();
        ArrayList<Letter> orderedDivList = new ArrayList<>();
        ArrayList<ArrayList<Letter>> orderedPosList = new ArrayList<>();

        for(int position = 0; position < App.WORD_LENGTH; position++){
            orderedPosList.add(new ArrayList<>());
        }

        for (Character currChar : letterOrdering) {
            int index = letterOrdering.indexOf(currChar);

            orderedList.add(rankedMap.get(index));
            orderedDivList.add(rankedDivMap.get(index));

            for(int position = 0; position < App.WORD_LENGTH; position++){
                orderedPosList.get(position).add(rankedPosMap.get(position).get(index));
            }
        }

        //Sorts all of the new Data
        Collections.sort(orderedList);
        Collections.sort(orderedDivList);
        for(int position = 0; position < App.WORD_LENGTH; position++){
            Collections.sort(orderedPosList.get(position));
        }        

        dataOut += "\nOrdered Letter List: ";
        dataOut += orderedList.get(0);
        // for(Letter letter : orderedList){
        //     dataOut += letter + " ";
        // }

        dataOut += "\nOrdered Diversity List: ";
        dataOut += orderedDivList.get(0);
        // for(Letter letter : orderedDivList){
        //     dataOut += letter + " ";
        // }

        dataOut += "\nOrdered Positional List: ";
        for(int position = 0; position < App.WORD_LENGTH; position++){
            dataOut += "\nPosition " + (position + 1) + " data: ";
            dataOut += orderedPosList.get(position).get(0);

            // for(Letter letter : orderedPosList.get(position)){
            //     dataOut += letter + " ";
            // }
        } 

        return dataOut;
    }
}
