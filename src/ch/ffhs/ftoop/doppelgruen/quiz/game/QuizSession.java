package ch.ffhs.ftoop.doppelgruen.quiz.game;

import ch.ffhs.ftoop.doppelgruen.quiz.answer_question_handling.Question;
import ch.ffhs.ftoop.doppelgruen.quiz.output.SayingList;
import ch.ffhs.ftoop.doppelgruen.quiz.utils.SleepUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/** A separate thread for every quiz session */
public class QuizSession implements Runnable {

  private final List<Question> questions;
  private final List<Player> players;
  private final Logger logger;
  private final QuizResults quizResults;

  private Question currentQuestion;
  private QuizResult currentQuestionResult;
  private boolean currentQuestionDone;

  /**
   * @param server The instance of the responsible server
   * @param players A list of players who play together
   * @throws NullPointerException If {@param server} is null
   * @throws NullPointerException If {@param players} is null
   */
  public QuizSession(final Server server, final List<Player> players) {
    Objects.requireNonNull(server, "server should not be null");
    Objects.requireNonNull(players, "players should not be null");

    this.questions = server.getQuestionList().getUniqueRandomQuestions(9);
    this.players = players;
    this.logger = server.getLogger();
    this.quizResults = new QuizResults(players);
  }

  @Override
  public void run() {
    try {
      prepareQuiz();
      postQuestions();
      closeQuiz();
    } catch (IOException e) {
      logger.warning(e.getMessage());
    }
  }

  /**
   * The result have to be calculated and printed. Every player needs to set to the Lobby.
   *
   * @throws IOException If there is an error during stream writing
   */
  private void closeQuiz() throws IOException {

    quizResults.calculatePointsAndUpdatePlayerStats();
    for (Player player : players) {
      var output = player.getWorker().getOutput();
      player.setLocation(Location.LOBBY);
      player.deleteCurrentQuizSession();
      output.quizResultHeadline();

      for (Map.Entry<Player, Integer> points : quizResults.getPoints().entrySet()) {
        output.quizResult(points.getKey(), points.getValue());
      }
      output.quizWon(quizResults.getWinner());
      output.quizEnd();

      player.getWorker().getParser().switchToLobbyCommands();
    }
  }

  /**
   * Output helpful information about the starting game. Switch Context to ingame;
   *
   * @throws IOException If there is an error during stream writing
   */
  private void prepareQuiz() throws IOException {

    for (Player player : players) {
      player.getWorker().getOutput().saying(SayingList.getOpponentFound());
    }

    SleepUtils.safeSleep(TimeUnit.SECONDS, 3);

    for (Player player : players) {
      player.getWorker().getOutput().quizStart();
    }

    SleepUtils.safeSleep(TimeUnit.SECONDS, 5);

    // Kommandos erst unmittelbar vor Spielbeginn wechseln
    for (Player player : players) {
      player.getWorker().getParser().switchToInGameCommands();
    }
  }

  /**
   * Iterates through all questions and send them to the players.
   *
   * @throws IOException If there is an error during stream writing
   */
  private void postQuestions() throws IOException {
    for (Question aQuestion : questions) {
      currentQuestion = aQuestion;
      currentQuestionDone = false;
      currentQuestionResult = new QuizResult();

      for (Player player : players) {
        player
            .getWorker()
            .getOutput()
            .questionWithPossibleAnswers(currentQuestion.getQuestionWithAnswersInMultiline());
      }

      sleepUntilTimeOrQuestionOver();
      quizResults.addQuizResult(currentQuestionResult);

      if (currentQuestionResult.getPlayerWithCorrectAnswer() == null) {
        for (Player player : players) {
          player.getWorker().getOutput().noAnswer(currentQuestion.getCorrectAnswer());
        }
        SleepUtils.safeSleep(TimeUnit.SECONDS, 3);
      }
    }
  }

  private void sleepUntilTimeOrQuestionOver() {
    var i = 0;
    do {
      SleepUtils.safeSleep(TimeUnit.SECONDS, 1);
      i++;
    } while (i <= 30 && !currentQuestionDone);
  }

  /**
   * Checks the answer of the given player. First, there have to be no correct answer given before.
   * Second, the player can only give one answer per question. Third, the answer have to be correct
   * to get a point.
   *
   * @param player Instance of a player who has given an answer
   * @param answer Number of an answer
   * @throws NullPointerException If {@param player} is null
   * @throws NullPointerException If {@param answer} is null
   * @throws IOException If there is an error during stream writing
   */
  public void checkAnswer(final Player player, final int answer) throws IOException {
    Objects.requireNonNull(player, "player should not be null");
    if (answer <= 0) {
      throw new IllegalArgumentException("answer should be greater 0");
    }

    if (currentQuestionResult == null) {
      throw new NullPointerException("Question is not ready jet");
    }

    if (currentQuestionResult.getPlayerWithCorrectAnswer() == player) {
      player.getWorker().getOutput().alreadyGaveFastestCorrectAnswer();
      return;
    }

    if (currentQuestionResult.isQuestionAlreadyAnsweredCorrect()) {
      player.getWorker().getOutput().fasterAnswer();
      return;
    }

    if (currentQuestionResult.isAnswerAlreadyGivenBy(player)) {
      player.getWorker().getOutput().justOneAnswer();
      return;
    }

    currentQuestionResult.giveAnAnswer(player);
    if (currentQuestion.isAnswerCorrect(answer)) {
      currentQuestionResult.setPlayerWithCorrectAnswer(player);
      currentQuestionDone = true;
      player.getWorker().getOutput().rightAnswer();

      for (Player aPlayer : players) {
        if (player != aPlayer) {
          aPlayer.getWorker().getOutput().pointForOpponent(player, answer);
        }
      }

    } else {
      player.getWorker().getOutput().wrongAnswer();
    }

    // If every player gave an answer, next Question pls.
    if (currentQuestionResult.getPlayersAlreadyGaveAnAnswer().size() == players.size()) {
      currentQuestionDone = true;
    }
  }
}
