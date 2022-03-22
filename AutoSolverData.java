import java.util.ArrayList;

public class AutoSolverData {
    private String fullData;
    private AutoSolver currentSolver;
    private boolean solved;
    private Word solution;
    private int roundsToSolve;
    private ArrayList<Word> guessedWords;
    private ArrayList<Validity> wordValidities;
    private ArrayList<Integer> wordsLeftList;

    public AutoSolverData(AutoSolver endingData, int debugLevel){
        //Makes sure that original data is not tampered with
        this.fullData = "";
        this.currentSolver  = new AutoSolver(endingData);
        this.solved = currentSolver.isSolved();
        this.solution = currentSolver.getSolution();
        this.roundsToSolve = currentSolver.getRound();
        this.guessedWords = currentSolver.getGuessedWords();
        this.wordValidities = currentSolver.getWordValidities();
        //this.wordsLeftList = currentSolver.getWordsLeftList();

        String solutionString = solution.getWord();
        
        fullData += solutionString + " - ";

        if(debugLevel == 1){
            fullData += this.solved;
        }
        else if(debugLevel == 2){
            if(solved)
                fullData += this.solved + ":" + this.roundsToSolve;
            else
                fullData += this.solved;
        }
        else if(debugLevel == 3){
            fullData += this.solved + ": ";

            for (Word word : guessedWords) {
                fullData += word.getWord() + " ";
            }
        }
        else if(debugLevel == 4){
            fullData += this.solved + ": ";

            for(Word word : guessedWords) {
                fullData += ValidityData.getColoredValidity(word.getWord(), solutionString) + " ";
            }
        }
        else if(debugLevel == 5){
            fullData += this.solved + ": ";

            for(Word word : guessedWords){
                fullData += "( " +  word.getWord() + ": "  + ValidityData.getValidityData(word.getWord(), solutionString) + " ) ";
            }
        }
        else if(debugLevel == 6){
            fullData += this.solved + ": ";

            for(Word word : guessedWords){
                fullData += "( " + word.getWord() + ": " + this.wordsLeftList.get(guessedWords.indexOf(word)) + " ) ";
            }
        }
        else{
            fullData += "Invalid Debug Level";
        }

    }

    public String getInfo(){
        return fullData;
    }

    public AutoSolver getAutoSolver(){
        return currentSolver;
    }

    public boolean isSolved(){
        return solved;
    }

    public Word getSolution(){
        return solution;
    }

    public String getSolutionString(){
        return solution.toString();
    }

    public int getRoundsSolved(){
        return roundsToSolve;
    }

    public ArrayList<Word> getGuessedWords(){
        return guessedWords;
    }

    public ArrayList<Integer> getWordsLeftList(){
        return wordsLeftList;
    }

    @Override 
    public String toString(){
        return this.getInfo();
    }
}
