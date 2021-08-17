package ch.ffhs.ftoop.doppelgruen.quiz.output;

import ch.ffhs.ftoop.doppelgruen.quiz.game.Player;
import ch.ffhs.ftoop.doppelgruen.quiz.game.Stats;
import java.io.IOException;

public interface IOutput {

  void outputHeadline() throws IOException;

  void welcomeMessage() throws IOException;

  void loginError() throws IOException;

  void loginErrorNameMissing() throws IOException;

  void loginSuccessful() throws IOException;

  void goodbyeMessage() throws IOException;

  void help(final String aString) throws IOException;

  void unknownCommand() throws IOException;

  void saying(final String aString) throws IOException;

  void findingOpponent() throws IOException;

  void currentAmountOfPlayersOnline(final int anInt) throws IOException;

  void quizStart() throws IOException;

  void quizEnd() throws IOException;

  void rightAnswer() throws IOException;

  void wrongAnswer() throws IOException;

  void fasterAnswer() throws IOException;

  void justOneAnswer() throws IOException;

  void alreadyGaveFastestCorrectAnswer() throws IOException;

  void noAnswer(final int correctAnswer) throws IOException;

  void pointForOpponent(final Player player, final int answer) throws IOException;

  void quizResult(final Player aPlayer, final int points) throws IOException;

  void quizResultHeadline() throws IOException;

  void questionWithPossibleAnswers(final String aString) throws IOException;

  void currentPlayersOnline(final Player aPlayer) throws IOException;

  void stats(final Stats stats) throws IOException;

  void quizWon(final Player aPlayer) throws IOException;

  void chat(final Player aPlayer, final String chatMessage) throws IOException;

  void loginErrorNameNotValid() throws IOException;

  void timeout() throws IOException;
}
