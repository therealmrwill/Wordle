import java.util.ArrayList;
import java.util.Collections;

public class AutoSolver {
   private Scoring scoreData;
   private Validity validityData;
   private ArrayList<Word> wordList;

   private ArrayList<Word> guessedWords;
   private Word foundSolution;
   public boolean solved = false;

   
   public AutoSolver(Scoring scoreData, Validity validityData, ArrayList<Word> wordList) {
      this.scoreData = scoreData;
      this.validityData = validityData;
      this.wordList = wordList;
      guessedWords = new ArrayList<>();
      foundSolution = new Word("NOPE.");
   }

   public boolean Run(String solution){
      for(int round = 1; round <= App.NUM_OF_ROUNDS; round++){
         scoreData.setRound(round);
         scoreData.setValidity(validityData);

         for (Word currentWord : wordList) {
            
            if(validityData.isValid(currentWord.getWord()) == false){
               currentWord.UpdateValidity(false);
               scoreData.removeWord(currentWord);
            }

            currentWord.setScore(scoreData.scoreWord(currentWord.getWord()));
         }
   
         Collections.sort(wordList);

         this.clearExcess();

         if(wordList.get(0).getWord().equals(solution)){
            foundSolution = wordList.get(0);
            validityData.addTestedWord(solution, validityData.getValidityData(solution, solution));

            //* Line is for debug 
            solved = true;
            return true;
         }else{
            guessedWords.add(wordList.get(0));
            String validityString = validityData.getValidityData(wordList.get(0).getWord(), solution);
            validityData.addTestedWord(wordList.get(0).getWord(), validityString);
         }


      }

      return false;
   }

   private boolean clearExcess() {
      for(int position = wordList.size() - 1; position >= 0; position--){
         if(wordList.get(position).isValid()){
            return true;
         }
         else{
            wordList.remove(position);
         }
      }

      return false;
   }

   public String RunWithData(String solution, int dataLevel){
      String dataOut = "";
      boolean solved = this.Run(solution);

      if(dataLevel == 1){
         if(solved){
            dataOut += solution + ": " + (guessedWords.size() + 1);
         }else{
            dataOut += solution + " not currently solvable";
         }
         
      }
      else if(dataLevel == 2){
         dataOut += solution + ": ";

         for (Word word : guessedWords) {
            dataOut += word.getWord() + ", ";
         }

         dataOut += this.foundSolution.getWord();
      }else if(dataLevel == 3){
         dataOut += solution + ": ";

         for (Word word : guessedWords){
            dataOut += word.getWord() + " " + word.getScore() + ", ";
         }     
         
         dataOut += this.foundSolution.getWord();
      }else if(dataLevel == 4){
         dataOut += solution + ": ";

         for(Word word: guessedWords){
            dataOut += validityData.getColoredValidity(word.getWord()) + ", ";
         }

         dataOut += validityData.getColoredValidity(foundSolution.getWord());
      }else{
         dataOut += "Invalid Data Level Specified: " + dataLevel;
      }


      return dataOut;
   }
}
