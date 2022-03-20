import java.util.ArrayList;

public class Scoring {
    public static ArrayList<Letter> baseLetterScore;
    public static ArrayList<ArrayList<Letter>> basePositionalLetterScore;
    public static ArrayList<Letter> baseLetterDiversityScore;    

    public static ArrayList<Letter> currentLetterScore;
    public static ArrayList<ArrayList<Letter>> currentPositionalLetterScore;
    public static ArrayList<Letter> currentLetterDiversityScore;    
    public static int currentRound;
    public static int roundsLeft;

    public static void init(){
        baseLetterScore = freshLetterList();
        basePositionalLetterScore = new ArrayList<>();
        baseLetterDiversityScore = freshLetterList();
        for(int position = 0; position < App.WORD_LENGTH; position++){
            basePositionalLetterScore.add(freshLetterList());
        }

        Scoring.reset();
    }

    public static void reset(){
        currentLetterScore = baseLetterScore;
        currentPositionalLetterScore = basePositionalLetterScore;
        currentLetterDiversityScore = baseLetterDiversityScore;
        currentRound = 0;
        roundsLeft = App.NUM_OF_ROUNDS - currentRound;
    }

    public static ArrayList<Letter> freshLetterList(){
        ArrayList<Letter> dataOut = new ArrayList<>();

        for (char character : App.alphabet.toCharArray()) {
            dataOut.add(new Letter(character, 0.0));
        }

        return dataOut;
    }

    public static String getCurrentInfo(){
        String dataOut = "";

        dataOut += "Current Scoring data:";
        dataOut += "\nRound: " + currentRound;
        dataOut += "\nRounds Left: " + roundsLeft;


        dataOut += "\nBase Letter Scores: \n";
        for (Letter letter : currentLetterScore) {
            dataOut += letter + " ";
        }

        dataOut += "\nPositional Letter Scores: ";
        for(int position = 0; position < App.WORD_LENGTH; position++){
            dataOut += "\n Position " + (position + 1) + " scores: \n";
            for (Letter letter : currentPositionalLetterScore.get(position)) {
                dataOut += letter + " ";
            }
        }

        dataOut += "\nLetter Diversity Scores: \n";
        for (Letter letter : baseLetterDiversityScore) {
            dataOut += letter + " ";
        }


        return dataOut;
    }

    
}
