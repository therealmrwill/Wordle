public class Word implements Comparable<Word>{
    private String name;
    private Double score;
    private boolean valid;


    
    public Word(String word, Double score, boolean valid) {
        this.name = word;
        this.score = score;
        this.valid = valid;

    }

    public Word(String word, boolean valid){
        this.name = word;
        this.score = 0.0;
        this.valid = valid;
    }

    public Word(String word){
        this.name = word;
        this.score = 0.0;
        this.valid = true;

    }

    public String getWord(){
        return this.name;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public boolean isValid() {
        return valid;
    }

    public void UpdateValidity(boolean valid) {
        this.valid = valid;
    }

    @Override    
    public int compareTo(Word word){
        int compare = word.score.compareTo(this.score);
        if(compare != 0){
            return compare;
        }
        else{
            //Update this so that it begins checking positional scores to choose best answer
            compare = this.name.compareTo(word.name);
            return compare;
        }
        
    }

    @Override
    public String toString(){
        return "(" + name + ": " + score + ": " + valid + ")";
    }

}
