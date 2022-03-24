
import java.util.ArrayList;
import java.util.Collections;


public class AutoSolver {
   private int uniqueID;
   private WordData scoreData;
   private ValidityTests validityData;
   private ArrayList<TestWord> wordList;

   //Data Variables
   private String solutionString;
   private ArrayList<ArrayList<TestWord>> wordListLeft;

   private ArrayList<TestWord> guessedWords;
   private ArrayList<Integer> wordsLeft;
   private ArrayList<ValidityTests> wordValidities;
   private ArrayList<TestWord> validWordList;

   private TestWord foundSolution;
   private boolean solved;

   
   public AutoSolver(WordData initScoreData, ValidityTests initValidityData, ArrayList<TestWord> initWordList, TestWord testWord) {
      this.uniqueID = 0;
      this.scoreData = new WordData(initScoreData);
      this.validityData = new ValidityTests(initValidityData);
      this.wordList = new ArrayList<>(initWordList);

      //* Input Parameters should not be changed beyond this point

      this.solutionString = testWord.getWord();
      this.guessedWords = new ArrayList<>();
      this.wordsLeft = new ArrayList<>();
      this.wordListLeft = new ArrayList<>();
      this.wordValidities = new ArrayList<>();
      this.validWordList = new ArrayList<>(this.wordList);
      this.foundSolution = null;
      this.solved = false;

      for(int round = 1; round <= App.NUM_OF_ROUNDS && this.solved == false; round++){
         //Making sure scoring data is all accurate
         this.scoreData.setRound(round);
         this.scoreData.setValidity(new ValidityTests(this.validityData));

         for(TestWord curWord : this.wordList){
            String curWordString = curWord.getWord();

            //Checks if word is valid with current validity checkers
            if(this.validityData.isValid(curWordString) == false){
               //Sets the words validity to false
               curWord.UpdateValidity(false);
               //Removes the word from the scoring data
               scoreData.removeWord(curWord);
               //Removes the word from the valid word list
               if(validWordList.contains(curWord)){
                  validWordList.remove(curWord);
               }
            }

         }

         //Has to be a separate loop otherwise words scored without finished scoring
         for(TestWord curWord : this.wordList){
            String curWordString = curWord.getWord();
            //Gets words new score based on new scoring data
            curWord.setScore(scoreData.scoreWord(curWordString));
         }

         //Sorts the list into their order based on score
         Collections.sort(this.wordList);

         this.clearExcess(this.wordList);

         //Data time - this area is of major importance 

         //Data that happens regardless
         if(wordList.size() > 0){
            //CurrentGuess can be put straight into data, it will remain unchanged
            TestWord currentGuess = new TestWord(wordList.get(0));
            
            this.guessedWords.add(currentGuess);
            this.wordsLeft.add(this.validWordList.size());
            this.wordListLeft.add(this.validWordList);
            this.wordValidities.add(new ValidityTests(this.validityData));

            if(currentGuess.getWord().equals(solutionString)){
               this.solved = true;
               this.foundSolution = currentGuess;
            }
            else{
               String validityInfo = ValidityTests.getValidityData(currentGuess.getWord(), solutionString);
               validityData.addTestedWord(currentGuess.getWord(), validityInfo);
            }


         }else{
            System.out.println("Error in AutoSolver.init(): wordList size < 1");
         }
         


      }
   }

   public AutoSolver(AutoSolver autoSolverData){
      this.uniqueID = autoSolverData.uniqueID + 1;
      this.scoreData = new WordData(autoSolverData.scoreData);
      this.validityData = new ValidityTests(autoSolverData.validityData);
      this.wordList = new ArrayList<>(autoSolverData.wordList);
      this.guessedWords = new ArrayList<>(autoSolverData.guessedWords);
      this.foundSolution = autoSolverData.foundSolution;
      this.solved = autoSolverData.solved;
   }

   private void clearExcess(ArrayList<TestWord> clearAbleList) {
      for(int position = wordList.size() - 1; position >= 0; position--){
         if(wordList.get(position).isValid()){
            return;
         }
         else{
            wordList.remove(position);
         }
      }

   }

   public boolean isSolved(){
      return this.solved;
   }

   public TestWord getSolution(){
      if(this.foundSolution != null){
         return this.foundSolution;
      }
      else{
         //Return what we know about the word
         char[] knownWord = this.validityData.getLockedChars();
         String knownWordString = "";
         for(char c : knownWord){
            knownWordString += c;
         }

         TestWord fakeWord = new TestWord(knownWordString, -1.0, false);

         return fakeWord;
      }
      
   }

   public String getSolutionString(){
      return this.getSolution().getWord();
   }

   public int getRound(){
      return this.guessedWords.size();
   }

   public ArrayList<TestWord> getGuessedWords(){
      return this.guessedWords;
   }

   public ArrayList<Integer> getNumWordsLeft(){
      return this.wordsLeft;
   }

   public ArrayList<ArrayList<TestWord>> getWordListLeft(){
      return wordListLeft;
   }

   public ArrayList<ValidityTests> getWordValidities(){
      return this.wordValidities;
   }

   public ValidityTests getValidity(){
      return this.validityData;
   } 
      
   @Override 
   public String toString(){
      AutoSolverData data = new AutoSolverData(this, App.DEBUG_LEVEL);
      return data.toString();
   }

   public AutoSolverData getInto(){
      return new AutoSolverData(this, App.DEBUG_LEVEL);
   }

}
