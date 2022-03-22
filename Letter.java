public class Letter implements Comparable<Letter>{
    //* Parameters
    private int uniqueID;
    private Character letter;
    private Double score;

    //* Mostly Proven link-free - letter stored in the same position but i think its fine

    //* Base constructor
    //* Should really only be used on the first creation of a Letter
    //* Proven link-Free
    public Letter(Character letter) {
        this.uniqueID = 0;
        this.letter = Character.toUpperCase(letter);
        this.score = 0.0;
    }

    //* Constructor used when making a duplicate
    //* All data is separate from input data
    //* Can fill this one with any data - even duplicated data
    public Letter(int uniqueID, Character letter, Double score){
        this.uniqueID = uniqueID + 1;
        this.letter = Character.toUpperCase(letter);
        this.score = score + 0;
    }

    //* More risky constructor - will not be used for my code
    public Letter(Letter letter){
        this.uniqueID = letter.uniqueID + 1;
        this.letter = letter.letter;
        this.score = letter.score;
    }

    //* Helper in creating duplicate data - this method should be goto way to create duplicate
    //* Uses constructor 2 to create new word 
    //* Also un-links all variables
    //*   - Doubles security when paired with constructor
    public Letter getCopy(){
        return new Letter((this.uniqueID + 0), Character.toUpperCase(this.letter), this.score + 0);
    }

    //* Getters - all proven un-linkable
    public int getUniqueID() {
        return this.uniqueID;
    }
    public Character getLetter() {
        return Character.toUpperCase(this.letter);
    }
    public Double getScore() {
        return this.score + 0;
    }

    //* Setters - all proven un-linkable
    public void setScore(Double score) {
        this.score = score + 0;
    }
    public void changeScore(Double amount){
        this.score += amount;
    }
   

    //* Allows Letters to be compared
    //* Letters with higher score picked first
    //* If scores are equal first Letter alphabetically
    //*  - No 2 Letters in a list should be exactly the same anyways
    @Override
    public int compareTo(Letter letter){
        int compare = letter.score.compareTo(this.score);
        if(compare != 0){
            return compare;
        }else{
            compare = this.letter.compareTo(letter.letter);
            return compare;
        }
    }

    //* toString() allows debugger and us to see the contents of a word
    //* Protected against linkage 
    //*  - Though it would take a mastermind to link something through the toString() data
    @Override
    public String toString(){
        return "(V" + this.uniqueID + ".0: " + this.letter + ": " + this.score + ")";
    }
   
}
