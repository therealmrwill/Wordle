import java.util.ArrayList;
import java.util.Collections;

public class AutoSolver {
   private Scoring scoreData;
   private Validity validityData;
   private ArrayList<Word> wordList;
   private boolean checkingInvalids;

   private ArrayList<Word> guessedWords;
   private Word foundSolution;

   
   public AutoSolver(Scoring scoreData, Validity validityData, ArrayList<Word> wordList, boolean checkingInvalids) {
      this.scoreData = scoreData;
      this.validityData = validityData;
      this.wordList = wordList;
      this.checkingInvalids = checkingInvalids;
      guessedWords = new ArrayList<>();
      foundSolution = new Word("No Solution Found");
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

            currentWord.UpdateScore(scoreData.scoreWord(currentWord.getWord()));
         }
   
         Collections.sort(wordList);

         this.clearExcess();

         if(wordList.get(0).getWord().equals(solution)){
            foundSolution = wordList.get(0);
            validityData.addTestedWord(solution, validityData.getValidityData(solution, solution));
            return true;
         }else{
            guessedWords.add(wordList.get(0));
            validityData.addTestedWord(wordList.get(0).getWord(), validityData.getValidityData(wordList.get(0).getWord(), solution));
         }


      }

      return false;
   }

   private void clearExcess() {
      for(int position = wordList.size() - 1; position <= 0; position--){
         if(wordList.get(position).isValid()){
            return;
         }else{
            wordList.remove(position);
         }
      }
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
            dataOut += word.getWord() + "-" + word.getScore() + ", ";
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
