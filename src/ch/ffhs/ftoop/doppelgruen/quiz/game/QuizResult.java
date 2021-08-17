package ch.ffhs.ftoop.doppelgruen.quiz.game;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * An instances of this class stores a list of players who have given an answer to the current
 * question and the player who gave the correct answer.
 *
 * <p>The information will be needed to ensure that every player can only answers once to any
 * question and to calculate the result after the quiz.
 */
public class QuizResult {

  private static final String PLAYER_EXCEPTION = "player should not be null";

  private final List<Player> playersAlreadyGaveAnAnswer;
  private Player playerWithCorrectAnswer;

  public QuizResult() {
    this.playersAlreadyGaveAnAnswer = new CopyOnWriteArrayList<>();
    this.playerWithCorrectAnswer = null;
  }

  public Player getPlayerWithCorrectAnswer() {
    return playerWithCorrectAnswer;
  }

  /**
   * @param player A valid player instance
   * @throws NullPointerException If {@param player} is null
   */
  public void setPlayerWithCorrectAnswer(final Player player) {
    Objects.requireNonNull(player, PLAYER_EXCEPTION);

    playerWithCorrectAnswer = player;
  }

  /**
   * @param player A valid player instance
   * @return true if the given player already had given an answer
   * @throws NullPointerException If {@param player} is null
   */
  public boolean isAnswerAlreadyGivenBy(final Player player) {
    Objects.requireNonNull(player, PLAYER_EXCEPTION);

    return playersAlreadyGaveAnAnswer.contains(player);
  }

  public List<Player> getPlayersAlreadyGaveAnAnswer() {
    return playersAlreadyGaveAnAnswer;
  }

  boolean isQuestionAlreadyAnsweredCorrect() {
    return (playerWithCorrectAnswer != null);
  }

  /**
   * @param player A valid player instance
   * @throws NullPointerException If {@param player} is null
   */
  public void giveAnAnswer(final Player player) {
    Objects.requireNonNull(player, PLAYER_EXCEPTION);

    playersAlreadyGaveAnAnswer.add(player);
  }
}
