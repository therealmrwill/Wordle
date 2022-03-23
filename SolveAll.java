import java.util.ArrayList;

public class SolveAll {
    private ArrayList<Word> guessableWordList;
    private ArrayList<Word> testingWordList;
    private Scoring baseScoringData;
    private ValidityTests baseValidityData;

    private ArrayList<String> solvedWordData;
    private ArrayList<String> unSolvedWordData;

    public SolveAll(ArrayList<Word> guessableWordList, ArrayList<Word> testingWordList){
        this.guessableWordList = guessableWordList;
        this.testingWordList = testingWordList;
        this.baseScoringData = new Scoring(guessableWordList);
        this.baseValidityData = new ValidityTests();

        this.solvedWordData = new ArrayList<>();
        this.unSolvedWordData = new ArrayList<>();
    }

    public boolean Run(){
        ArrayList<Word> testList = new ArrayList<>(testingWordList);


        for(Word word : testList){

            Scoring scoringData = new Scoring(baseScoringData);
            ValidityTests validityData = new ValidityTests(baseValidityData);
            ArrayList<Word> currGuessList = new ArrayList<>(guessableWordList);
            AutoSolver currentSolver = new AutoSolver(scoringData, validityData, currGuessList);
            String currentData = currentSolver.RunWithData(word.getWord(), App.DEBUG_LEVEL);

            if(currentSolver.solved == true){
                this.solvedWordData.add(currentData);
            }else{
                this.unSolvedWordData.add(currentData);
            }

            if(App.DEBUG_LEVEL > 1){
                System.out.println(currentData);
            }

            //Garbage collection
            scoringData = null;
            validityData = null;
            currGuessList = null;
            currentSolver = null;
            currentData = null;
        }

        return true;
    }



//     ArrayList<Word> wordList = DataReader.readFromFile("DebugFiles/WordlePossibleSolutions.txt");


//     for(Word word : wordList){
//         Scoring scoreData = new Scoring(wordList);
//         Validity validityData = new Validity();
//         AutoSolver currentSolver = new AutoSolver(scoreData, validityData, DataReader.readFromFile("FullWordList.txt"));
//         String data = currentSolver.RunWithData(word.getWord(), App.DEBUG_LEVEL);
        
//         System.out.println(data);

//     } 
// }
}
