package ch.ffhs.ftoop.doppelgruen.quiz.game;

import java.util.Objects;

/**
 * An instance of this class stores the player's name, his location, his worker instance and his
 * current quiz session while he is playing a Game
 */
public class Player {

  private final String name;
  private final ServerWorker worker;
  private final Stats stats;

  private Location location;
  private QuizSession currentQuizSession;

  /**
   * Creates a new player
   *
   * @param name The player's name
   * @param worker The corresponding server worker
   * @throws NullPointerException If {@param name} is null
   * @throws NullPointerException If {@param worker} is null
   * @throws IllegalArgumentException If {@param name} is empty
   */
  Player(final String name, final ServerWorker worker) {
    Objects.requireNonNull(name, "name should not be null");
    Objects.requireNonNull(worker, "worker should not be null");
    if (name.isEmpty()) {
      throw new IllegalArgumentException("name should not be empty");
    }

    this.name = name;
    this.location = Location.LOBBY;
    this.worker = worker;
    currentQuizSession = null;
    stats = new Stats();
  }

  public Location getLocation() {
    return location;
  }

  /**
   * @param location A new location for the Player
   * @throws NullPointerException If {@param location} is null
   */
  public void setLocation(final Location location) {
    Objects.requireNonNull(location, "location should not be null");

    this.location = location;
  }

  public String getName() {
    return name;
  }

  public ServerWorker getWorker() {
    return worker;
  }

  public QuizSession getCurrentQuizSession() {
    return currentQuizSession;
  }

  /**
   * The player starts a new quiz, so he need a new reference
   *
   * @param quizSession Responsible quiz session
   * @throws NullPointerException If {@param quizSession} is null
   */
  public void setCurrentQuizSession(final QuizSession quizSession) {
    Objects.requireNonNull(quizSession, "quizSession should not be null");

    currentQuizSession = quizSession;
  }

  public void deleteCurrentQuizSession() {
    currentQuizSession = null;
  }

  public Stats getStats() {
    return stats;
  }

  public void wonCurrentGame() {
    stats.incrementWon();
  }

  public void drawnCurrentGame() {
    stats.incrementDrawn();
  }

  public void lostCurrentGame() {
    stats.incrementLost();
  }
}
