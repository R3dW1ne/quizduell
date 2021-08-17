package ch.ffhs.ftoop.doppelgruen.quiz.game;

import ch.ffhs.ftoop.doppelgruen.quiz.client.Client;
import ch.ffhs.ftoop.doppelgruen.quiz.utils.SleepUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class QuizResultsTest {
  private Players players;
  private Player player1;
  private Player player2;
  private static final int SERVER_PORT = 60500;
  private static final int SAFESLEEP_MILLISECONDS = 100;

  private QuizResults cut;

  @BeforeEach
  void setUp() {
    var server = new Server(SERVER_PORT);
    var serverThread = new Thread(server);
    serverThread.start();
    SleepUtils.safeSleep(TimeUnit.MILLISECONDS, SAFESLEEP_MILLISECONDS);
    var client = Client.createClient("localhost", SERVER_PORT);
    if (client == null) {
      fail();
    }
    var serverWorker = new ServerWorker(server, client.getSocket());
    players = server.getPlayerController();
    player1 = players.createPlayerIfPossible("player1", serverWorker);
    player2 = players.createPlayerIfPossible("player2", serverWorker);
    player1.setLocation(Location.INGAME);
    player2.setLocation(Location.INGAME);
  }

  @Test
  @DisplayName("Test constructor normal case")
  void testConstructor1() {
    cut = new QuizResults(players.getAllPlayers());
    assertNotNull(cut);
  }

  @Test
  @DisplayName("Test constructor null players")
  void testConstructor2() {
    assertThrows(NullPointerException.class, () -> new QuizResults(null));
  }

  @Test
  @DisplayName("Test constructor empty players")
  void testConstructor3() {
    List<Player> emptyPlayerList = new ArrayList<>();
    assertThrows(IllegalArgumentException.class, () -> new QuizResults(emptyPlayerList));
  }

  @Test
  @DisplayName("Test constructor points map")
  void testConstructor4() {
    cut = new QuizResults(players.getAllPlayers());
    assertEquals(2, cut.getPoints().size());
  }

  @Test
  @DisplayName("Test calculate points with one player and one answer")
  void calculatePointsAndUpdatePlayerStats1() {
    cut = new QuizResults(players.getAllPlayers());
    var quizResult = new QuizResult();
    quizResult.setPlayerWithCorrectAnswer(player1);

    cut.addQuizResult(quizResult);

    cut.calculatePointsAndUpdatePlayerStats();

    assertEquals(player1, cut.getWinner());
  }

  @Test
  @DisplayName("Test calculate points with three answer")
  void calculatePointsAndUpdatePlayerStats2() {
    cut = new QuizResults(players.getAllPlayers());
    var quizResult = new QuizResult();
    quizResult.setPlayerWithCorrectAnswer(player1);
    cut.addQuizResult(quizResult);
    var quizResult2 = new QuizResult();
    quizResult2.setPlayerWithCorrectAnswer(player2);
    cut.addQuizResult(quizResult2);
    cut.addQuizResult(quizResult2);

    cut.calculatePointsAndUpdatePlayerStats();

    assertEquals(player2, cut.getWinner());
  }

  @Test
  @DisplayName("Test calculate update player stats")
  void calculatePointsAndUpdatePlayerStats3() {

    cut = new QuizResults(players.getAllPlayers());
    var quizResult = new QuizResult();
    quizResult.setPlayerWithCorrectAnswer(player1);
    cut.addQuizResult(quizResult);

    cut.calculatePointsAndUpdatePlayerStats();

    assertEquals(1, player1.getStats().getWon());
  }

  @Test
  @DisplayName("Test calculate points with three answer -> draw")
  void calculatePointsAndUpdatePlayerStats4() {
    cut = new QuizResults(players.getAllPlayers());
    var quizResult = new QuizResult();
    quizResult.setPlayerWithCorrectAnswer(player1);
    cut.addQuizResult(quizResult);
    cut.addQuizResult(quizResult);
    var quizResult2 = new QuizResult();
    quizResult2.setPlayerWithCorrectAnswer(player2);
    cut.addQuizResult(quizResult2);
    cut.addQuizResult(quizResult2);

    cut.calculatePointsAndUpdatePlayerStats();

    assertNull(cut.getWinner());
  }
}
