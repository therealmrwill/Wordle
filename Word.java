import javax.lang.model.util.ElementScanner14;

public class Word implements Comparable<Word>{
    //* Parameters
    private int uniqueID;
    private String name;
    private Double score;
    private Boolean valid;

    //* All methods checked for linkage issues - duplicates fully separate

    //* Main constructor used for new Words - data is all separate from input
    public Word(String word){
        this.uniqueID = 1;
        this.name = new String(word);
        this.score = -1.0;
        this.valid = true;
        checkLength();
    }

    //* Constructor used when making a duplicate
    //* All data is separate from input data
    //* Can fill this one with any data - even duplicated data
    public Word(int uniqueID, String word, Double score, boolean valid) {
        this.uniqueID = uniqueID + 1;
        this.name = new String(word);
        this.score = score + 0;
        if(valid){this.valid = true;}else{this.valid = false;}
        checkLength();
    }

    //* More risky constructor - will not be used for my code
    public Word(Word word){
        this.uniqueID = word.getUniqueID() + 1;
        this.name = new String(word.name);
        this.score = word.score + 0;
        if(word.isValid()){this.valid = true;}else{this.valid = false;}
        checkLength();
    }

    //* Helper in creating duplicate data - this method should be go to way to create duplicate
    //* Uses constructor 2 to create new word 
    //* Also un-links all variables
    //*   - Doubles security when paired with constructor
    public Word getCopy(){
        if(valid)
            return new Word(this.uniqueID + 0, new String(this.name), (this.score + 0), true);
        else  
            return new Word(this.uniqueID + 0, new String(this.name), (this.score + 0), false);  
    }

    //* Checks to make sure that word complies to standards
    //* If not, makes filler word so that later data doesn't break
    private void checkLength() {
        if(this.name.length() != App.WORD_LENGTH){
            System.out.println("Error in :" + this.toString() + " - Invalid Word Length - Removing Word");
            blankOut();
        }
    }

    //* Sets Word data to useless data that will pass all tests, but will provide nothing of value
    //* Basically like clearing the word
    //* Use Word = void to clear word though
    public void blankOut(){
        this.name = "     ";
        this.score = -1.0;
        this.valid = false;
    }

    //* Getters - Protects against linkage
    public int getUniqueID(){
        return this.uniqueID + 0;
    }
    public String getName(){
        return new String(this.name);
    }

    public Double getScore() {
        return score + 0;
    }
    public boolean isValid() {
        if(this.valid){return true;}else{return false;}
    }

    //* Setters = Protects against linkage
    //*  - Name never needs to be set - will always stay the same
    public void setScore(Double score) {
        this.score = score + 0;
    }
    public void setValid(Boolean valid) {
        if(valid){this.valid = true;}else{this.valid = false;}
    }

    //* Allows Words to be compared
    //* Words with higher score picked first
    //* If scores are equal first word alphabetically
    //*  - No 2 words in the list should be exactly the same anyways
    @Override    
    public int compareTo(Word word){
        int compare = word.score.compareTo(this.score);
        if(compare != 0){
            return compare;
        }
        else{
            compare = this.name.compareTo(word.name);
            return compare;
        }   
    }


    //* toString() allows debugger and us to see the contents of a word
    //* Protected against linkage 
    //*  - Though it would take a mastermind to link something through the toString() data
    @Override
    public String toString(){
        return "(V" + getUniqueID() + ".0: " + getName() + ": " + getScore() + ": " + isValid() + ")";
    }

}
