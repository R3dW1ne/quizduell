package ch.ffhs.ftoop.doppelgruen.quiz.game;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

/** An instance of this class stores a list of the results to every question. */
public class QuizResults {

  private final List<QuizResult> quizResultList;
  private final Map<Player, Integer> points;
  private Player winner;

  public QuizResults(final List<Player> playerList) {
    Objects.requireNonNull(playerList, "playerList should not be null");
    if (playerList.isEmpty()) {
      throw new IllegalArgumentException("playerList should not be empty");
    }

    this.quizResultList = new CopyOnWriteArrayList<>();
    this.points = new HashMap<>();

    for (Player player : playerList) {
      points.put(player, 0);
    }
  }

  /**
   * @param quizResult A valid quizResult
   * @throws NullPointerException If {@param quizResult} is null
   */
  public void addQuizResult(final QuizResult quizResult) {
    Objects.requireNonNull(quizResult, "quizResult should not be null");

    quizResultList.add(quizResult);
  }

  private void calculatePointsForEachPlayer() {
    for (QuizResult aResult : quizResultList) {
      var player = aResult.getPlayerWithCorrectAnswer();
      if (player != null) {
        points.computeIfPresent(player, (key, oldValue) -> oldValue + 1);
      }
    }
  }

  private void updatePlayerStats() {

    var playerWithHighestPoints = points.keySet().iterator().next();
    var drawFound = false;

    // Find Players with most correct answers
    for (Map.Entry<Player, Integer> point : points.entrySet()) {
      if (point.getValue() > points.get(playerWithHighestPoints)) {
        playerWithHighestPoints = point.getKey();
      }
    }

    // Is there a Player with the same amount of correct answers?
    // If yes, its a draw!
    for (Map.Entry<Player, Integer> point : points.entrySet()) {
      if (point.getKey() != playerWithHighestPoints) {
        if (point.getValue() == (int) points.get(playerWithHighestPoints)) {
          point.getKey().drawnCurrentGame();
          playerWithHighestPoints.drawnCurrentGame();
          drawFound = true;
        } else {
          point.getKey().lostCurrentGame();
        }
      }
    }
    if (!drawFound) {
      winner = playerWithHighestPoints;
      winner.wonCurrentGame();
    }
  }

  public Player getWinner() {
    return winner;
  }

  public Map<Player, Integer> getPoints() {
    return points;
  }

  public void calculatePointsAndUpdatePlayerStats() {
    calculatePointsForEachPlayer();
    updatePlayerStats();
  }
}
