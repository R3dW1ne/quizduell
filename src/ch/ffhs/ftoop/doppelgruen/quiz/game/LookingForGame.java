package ch.ffhs.ftoop.doppelgruen.quiz.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import ch.ffhs.ftoop.doppelgruen.quiz.utils.SleepUtils;

/** A separate thread for the matchmaking. */
public class LookingForGame implements Runnable {

  private final Server server;
  private final Players players;
  private volatile boolean matchMakingActive = false;

  /**
   * Create a new matchmaking object
   *
   * @param server needs an instance of the responsible server
   * @throws NullPointerException If {@param server} is null
   */
  public LookingForGame(final Server server) {
    Objects.requireNonNull(server, "server should not be null");

    this.server = server;
    this.players = server.getPlayerController();
  }

  @Override
  public void run() {
    startMatchMakingEngine();
  }

  public void activateMatchMaking() {
    matchMakingActive = true;
  }

  /**
   * A lot of magic happens here to find the perfect opponent. Attributes like last games, results,
   * answer speed and a lot more will be checked. There will be calculated a score (MMR = match
   * making ranking) for each player and the two players with the nearest mmr will be matched.
   */
  private void startMatchMakingEngine() {
    while (matchMakingActive) {
      for (Player player : players.getPlayersIn(Location.LOOKING_FOR_GAME)) {
        for (Player opponent : players.getPlayersIn(Location.LOOKING_FOR_GAME)) {
          // Ok, maybe there is enough time in the next project :(
          // Just find another player to play with
          if ((player != opponent) && (player.getCurrentQuizSession() == null)) {
            List<Player> playerList = new ArrayList<>();
            player.setLocation(Location.INGAME);
            playerList.add(player);
            opponent.setLocation(Location.INGAME);
            playerList.add(opponent);

            var quiz = new QuizSession(server, playerList);
            player.setCurrentQuizSession(quiz);
            opponent.setCurrentQuizSession(quiz);
            new Thread(quiz).start();

            break;
          }
        }
      }
      if (players.getPlayersIn(Location.LOOKING_FOR_GAME).isEmpty()) {
        matchMakingActive = false;
      }
      SleepUtils.safeSleep(TimeUnit.SECONDS, 3);
    }
  }

  public boolean isMatchMakingActive() {
    return matchMakingActive;
  }
}
