package trivia;

import java.util.ArrayList;
import java.util.LinkedList;

public class GameBetter implements IGame {

   ArrayList<Player> players = new ArrayList<>();

   LinkedList<Question> questions = new LinkedList<>();

   int currentPlayerId = 0;

   public GameBetter() {
      initializeBoard();
   }

   private void initializeBoard() {
      for (int i = 0; i < 50; i++) {
         questions.addLast(new Question(Question.Category.POP, "Pop Question %d".formatted(i)));
         questions.addLast(new Question(Question.Category.SCIENCE, "Science Question %d".formatted(i)));
         questions.addLast(new Question(Question.Category.SPORTS, "Sports Question %d".formatted(i)));
         questions.addLast(new Question(Question.Category.ROCK, "Rock Question %d".formatted(i)));
      }
   }

   public boolean isPlayable() {
      return (howManyPlayers() >= 2);
   }

   public boolean add(String playerName) {

      players.add(new Player(players.size() + 1, playerName,  0, 0, false, false));

      printPlayerAddedMessage(playerName);
      return true;
   }

   private void printPlayerAddedMessage(final String playerName) {
      System.out.println(playerName + " was added");
      System.out.println("They are player number " + players.size());
   }

   public int howManyPlayers() {
      return players.size();
   }

   public void roll(int roll) {

      if ( !isPlayable() ) {
         return;
      }

      printStatusBeforeRolling(roll);

      if (getCurrentPlayer().isInPenaltyBox() ) {
         getCurrentPlayer().setGettingOutOfPenaltyBox(roll % 2 != 0);
         if (!getCurrentPlayer().isGettingOutOfPenaltyBox() ){

            playerCannotGoOutOfPenaltyBox();

            return;

         } else {
            playerCanGoOutOfPenaltyBox();

         }
      }

      movePlayerAndPrintStatusAndAskQuestion(roll);

   }

   private void movePlayerAndPrintStatusAndAskQuestion(int roll) {
      moveCurrentPlayerToNewPlace(roll);

      printPlayerStatus();
      askQuestion();
   }

   private void playerCannotGoOutOfPenaltyBox() {
      printPlayerIsNotGettingOutPenalty();
   }

   private void playerCanGoOutOfPenaltyBox() {
      printPlayerIsGettingOutPenalty();
   }

   private Player getCurrentPlayer() {
      return players.get(currentPlayerId);
   }

   private void printPlayerIsNotGettingOutPenalty() {
      System.out.println(getCurrentPlayer().getName() + " is not getting out of the penalty box");
   }

   private void printPlayerIsGettingOutPenalty() {
      System.out.println(getCurrentPlayer().getName() + " is getting out of the penalty box");
   }

   private void printStatusBeforeRolling(final int roll) {
      System.out.println(getCurrentPlayer().getName() + " is the current player");
      System.out.println("They have rolled a " + roll);
   }

   private void printPlayerStatus() {
      System.out.println(getCurrentPlayer().getName()
                         + "'s new location is "
                         + getCurrentPlayer().getPlace());
      System.out.println("The category is " + currentCategory());
   }

   private void moveCurrentPlayerToNewPlace(int roll) {
      getCurrentPlayer().move(roll);
   }

   private void askQuestion() throws IllegalArgumentException {
      switch (currentCategory()) {
         case "Pop" -> System.out.println(getQuestionAndRemoveIt(Question.Category.POP).getContent());
         case "Science" -> System.out.println(getQuestionAndRemoveIt(Question.Category.SCIENCE).getContent());
         case "Sports" -> System.out.println(getQuestionAndRemoveIt(Question.Category.SPORTS).getContent());
         case "Rock" -> System.out.println(getQuestionAndRemoveIt(Question.Category.ROCK).getContent());
         default -> throw new IllegalArgumentException("No question for category " + currentCategory());
      }
   }

   private Question getQuestionAndRemoveIt(Question.Category category) {

      for (Question question : questions ) {
         if ( question.getCategory() == category ) {
            questions.remove(question);
            return question;
         }
      }

      throw new IllegalArgumentException("No question for category " + category);
   }

   private String currentCategory() {
      return switch(getCurrentPlayer().getPlace()) {
               case 0, 4, 8 ->  "Pop";
               case 1, 5, 9 -> "Science";
               case 2, 6, 10 -> "Sports";
               default -> "Rock";
      };
   }

   public boolean wasCorrectlyAnswered() {
      if (getCurrentPlayer().isInPenaltyBox() && !getCurrentPlayer().isGettingOutOfPenaltyBox()) {
         doMovePlayerInTheBoard();
         return true;
      }

      System.out.println("Answer was correct!!!!");
      getCurrentPlayer().incrementPurse();
      System.out.println(getCurrentPlayer().getName()
                         + " now has "
                         + getCurrentPlayer().getPurse()
                         + " Gold Coins.");

      boolean winner = didPlayerWin();
      doMovePlayerInTheBoard();

      return winner;

   }

   private void doMovePlayerInTheBoard() {
      currentPlayerId++;
      if (currentPlayerId == players.size()) currentPlayerId = 0;
   }

   public boolean wrongAnswer() {
      System.out.println("Question was incorrectly answered");
      System.out.println(getCurrentPlayer().getName() + " was sent to the penalty box");
      getCurrentPlayer().setInPenaltyBox(true);

      doMovePlayerInTheBoard();
      return true;
   }


   private boolean didPlayerWin() {
      return getCurrentPlayer().getPurse() != 6;
   }
}
