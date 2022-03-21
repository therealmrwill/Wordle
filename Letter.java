public class Letter implements Comparable<Letter>{
   private Character letter;
   private Double score;

    public Letter(Character letter, Double score) {
        this.letter = letter;
        this.score = score;
    }

    

    public Letter(Character letter) {
        this.letter = letter;
        this.score = 0.0;
    }

    public Character getLetter() {
        return letter;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public void UpdateScore(Double amount){
        this.score += amount;
    }
   
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

    @Override
    public String toString(){
        return "(" + this.letter + ": " + this.score + ")";
    }
   
}
