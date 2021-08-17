package ch.ffhs.ftoop.doppelgruen.quiz.game;

import ch.ffhs.ftoop.doppelgruen.quiz.client.Client;
import ch.ffhs.ftoop.doppelgruen.quiz.utils.SleepUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class QuizSessionTest {

  private static Server server;
  private static Thread serverThread;
  private static Players players;
  private static final int SERVER_PORT = 60700;
  private static final int SAFESLEEP_MILLISECONDS = 100;

  @BeforeAll
  static void setUp() {
    server = new Server(SERVER_PORT);
    serverThread = new Thread(server);
    serverThread.start();
    SleepUtils.safeSleep(TimeUnit.MILLISECONDS, SAFESLEEP_MILLISECONDS);
    var client = Client.createClient("localhost", SERVER_PORT);
    if (client == null) {
      fail();
    }
    var serverWorker = new ServerWorker(server, client.getSocket());
    players = server.getPlayerController();
    var player1 = players.createPlayerIfPossible("player1", serverWorker);
    var player2 = players.createPlayerIfPossible("player2", serverWorker);
    player1.setLocation(Location.INGAME);
    player2.setLocation(Location.INGAME);
  }

  @AfterAll
  static void tearDown() {
    serverThread.interrupt();
  }

  @Test
  @DisplayName("Test constructor")
  void testConstructor1() {
    var cut = new QuizSession(server, players.getAllPlayers());
    assertNotNull(cut);
  }

  @Test
  @DisplayName("Test constructor null server")
  void testConstructor2() {
    var playerList = players.getAllPlayers();
    assertThrows(NullPointerException.class, () -> new QuizSession(null, playerList));
  }

  @Test
  @DisplayName("Test constructor null players")
  void testConstructor3() {
    assertThrows(NullPointerException.class, () -> new QuizSession(server, null));
  }
}
