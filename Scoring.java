import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Scoring {
    public static ArrayList<Letter> baseLetterScore;
    public static ArrayList<ArrayList<Letter>> basePositionalLetterScore;
    public static ArrayList<Letter> baseLetterDiversityScore;    

    public static ArrayList<Letter> currentLetterScore;
    public static ArrayList<ArrayList<Letter>> currentPositionalLetterScore;
    public static ArrayList<Letter> currentLetterDiversityScore;
    
    public static HashMap<Character, Integer> letterRank;
    public static ArrayList<HashMap<Character, Integer>> positionalLetterRank;
    public static HashMap<Character, Integer> letterDiversityRank;


    public static int currentRound;
    public static int roundsLeft;

    public static void init(ArrayList<Word> baseWords){
        baseLetterScore = freshLetterList();
        basePositionalLetterScore = new ArrayList<>();
        baseLetterDiversityScore = freshLetterList();
        for(int position = 0; position < App.WORD_LENGTH; position++){
            basePositionalLetterScore.add(freshLetterList());
        }

        for(Word word : baseWords) {
            if(word.isValid()){
                Scoring.addWord(word.getWord());
            }
        }

        //* Un-Needed Line, but helpful for debug
        Scoring.baseReOrder();

        Scoring.reset();
        Scoring.reOrder();
    }

    private static void baseReOrder() {
        Collections.sort(baseLetterScore);
        Collections.sort(baseLetterDiversityScore);
        for (ArrayList<Letter> letterList : basePositionalLetterScore) {
            Collections.sort(letterList);
        }
    }

    public static void reOrder() {
        Collections.sort(currentLetterScore);
        letterRank = Scoring.reRank(currentLetterScore);

        Collections.sort(currentLetterDiversityScore);
        Scoring.reRank(currentLetterDiversityScore);

        for (ArrayList<Letter> letterList : currentPositionalLetterScore) {
            Collections.sort(letterList);
            Scoring.reRank(letterList);
        }
    }

    private static HashMap<Character, Integer> reRank(ArrayList<Letter> currentList) {
        HashMap<Character, Integer> dataOut = new HashMap<>();

        for(int rank = 0; rank < currentList.size(); rank++){
            dataOut.put(currentList.get(rank).getLetter(), rank);
        }

        return dataOut;
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
        int rank = 0;

        for (char character : App.alphabet.toCharArray()) {
            dataOut.add(new Letter(character, 0.0, rank));
            rank++;
        }

        return dataOut;
    }

    public static String getCurrentInfo(){
        String dataOut = "";

        dataOut += "Current Scoring data:";
        dataOut += "\nRound: " + currentRound;
        dataOut += "\nRounds Left: " + roundsLeft;


        dataOut += "\nBase Letter Scores: \n";
        Collections.sort(currentLetterScore);
        for (Letter letter : currentLetterScore) {
            dataOut += letter + " ";
        }

        dataOut += "\nPositional Letter Scores: ";
        for(int position = 0; position < App.WORD_LENGTH; position++){
            dataOut += "\n Position " + (position + 1) + " scores: \n";
            Collections.sort(currentPositionalLetterScore.get(position));
            for (Letter letter : currentPositionalLetterScore.get(position)) {
                dataOut += letter + " ";
            }
        }

        dataOut += "\nLetter Diversity Scores: \n";
        Collections.sort(currentLetterDiversityScore);
        for (Letter letter : currentLetterDiversityScore) {
            dataOut += letter + " ";
        }


        return dataOut;
    }

    private static boolean addWord(String word){
        //* Relies on the fact that the words have not been sorted yet


        if(word.length() != App.WORD_LENGTH){
            System.out.println("Error in Scoring.AddWord(): Invalid Word Length");
            return false;
        }

        ArrayList<Character> pastChars = new ArrayList<>();
        word = word.toUpperCase();

        for(int position = 0; position < word.length(); position++){
            int letterPosition = letterPositionFinder(word.charAt(position));
            baseLetterScore.get(letterPosition).UpdateScore(1.0);
            basePositionalLetterScore.get(position).get(letterPosition).UpdateScore(1.0);

            if(!pastChars.contains(word.charAt(position))){
                baseLetterDiversityScore.get(letterPosition).UpdateScore(1.0);
                pastChars.add(word.charAt(position));
            }
        }


        return true;
    }

    private static int letterPositionFinder(char currentChar) {
        int dataOut = -1;

        switch (currentChar) {
            case 'A': dataOut = 0; break;
            case 'B': dataOut = 1; break;
            case 'C': dataOut = 2; break;
            case 'D': dataOut = 3; break;
            case 'E': dataOut = 4; break;
            case 'F': dataOut = 5; break;
            case 'G': dataOut = 6; break;
            case 'H': dataOut = 7; break;
            case 'I': dataOut = 8; break;
            case 'J': dataOut = 9; break;
            case 'K': dataOut = 10; break;
            case 'L': dataOut = 11; break;
            case 'M': dataOut = 12; break;
            case 'N': dataOut = 13; break;
            case 'O': dataOut = 14; break;
            case 'P': dataOut = 15; break;
            case 'Q': dataOut = 16; break;
            case 'R': dataOut = 17; break;
            case 'S': dataOut = 18; break;
            case 'T': dataOut = 19; break;
            case 'U': dataOut = 20; break;
            case 'V': dataOut = 21; break;
            case 'W': dataOut = 22; break;
            case 'X': dataOut = 23; break;
            case 'Y': dataOut = 24; break;
            case 'Z': dataOut = 25; break;

            default: System.out.println("Error in Scoring.AddWord().letterPositionFinder(): Invalid Character");; break;
        }

        return dataOut;
    }

    public static boolean removeWord(String word){
        if(word.length() != App.WORD_LENGTH){
            System.out.println("Error in Scoring.RemoveWord(): Invalid word length: " + word);
            return false;
        }
        
        ArrayList<Character> previousChars = new ArrayList<>();

         for(int position = 0; position < word.length(); position++){

            if(App.alphabet.contains(word.substring(position, position + 1))){
                currentLetterScore.get(letterRank.get(word.charAt(position))).UpdateScore(-1.0);
                currentPositionalLetterScore.get(position).get(positionalLetterRank.get(position).get(word.charAt(position))).UpdateScore(-1.0);;
                if(!previousChars.contains(word.charAt(position))){
                    currentLetterDiversityScore.get(letterDiversityRank.get(word.charAt(position))).UpdateScore(-1.0);
                    previousChars.add(word.charAt(position));
                }
            }
            else{
                System.out.println("Error in Scoring.RemoveWord(" + word + "): Invalid Character at position: " + (position + 1));
            }

         }


        return true;
    } 

    public static double scoreWord(String word){
        return -1;
    }

}
