import java.util.ArrayList;

public class SolveAll {
    private ArrayList<TestWord> guessableWordList;
    private ArrayList<TestWord> testingWordList;
    private WordData baseScoringData;
    private ValidityTests baseValidityData;

    private ArrayList<String> solvedWordData;
    private ArrayList<String> unSolvedWordData;

    public SolveAll(ArrayList<TestWord> guessableWordList, ArrayList<TestWord> testingWordList){
        this.guessableWordList = guessableWordList;
        this.testingWordList = testingWordList;
        this.baseScoringData = new WordData(guessableWordList);
        this.baseValidityData = new ValidityTests();

        this.solvedWordData = new ArrayList<>();
        this.unSolvedWordData = new ArrayList<>();
    }

    public boolean Run(){
        ArrayList<TestWord> testList = new ArrayList<>(testingWordList);


        for(TestWord word : testList){

            WordData scoringData = new WordData(baseScoringData);
            ValidityTests validityData = new ValidityTests(baseValidityData);
            ArrayList<TestWord> currGuessList = new ArrayList<>(guessableWordList);
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
