package ch.ffhs.ftoop.doppelgruen.quiz.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import ch.ffhs.ftoop.doppelgruen.quiz.client.Client;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import ch.ffhs.ftoop.doppelgruen.quiz.utils.SleepUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PlayerTest {

  private static Server server;
  private static Thread serverThread;
  private static ServerWorker serverWorker;
  private static final int SERVER_PORT = 60400;
  private static final int SAFESLEEP_MILLISECONDS = 100;

  private Player cut;

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
    serverWorker = new ServerWorker(server, client.getSocket());
  }

  @AfterAll
  static void tearDown() {
    serverThread.interrupt();
  }

  @Test
  @DisplayName("Test constructor normal case")
  void testConstructor1() {
    cut = new Player("player 1", serverWorker);
    assertNotNull(cut);
  }

  @Test
  @DisplayName("Test constructor null player")
  void testConstructor2() {
    assertThrows(NullPointerException.class, () -> new Player(null, serverWorker));
  }

  @Test
  @DisplayName("Test constructor empty player")
  void testConstructor3() {
    assertThrows(IllegalArgumentException.class, () -> new Player("", serverWorker));
  }

  @Test
  @DisplayName("Test constructor null serverWorker")
  void testConstructor4() {
    assertThrows(NullPointerException.class, () -> new Player("player 1", null));
  }

  @Test
  @DisplayName("Test getLocation")
  void testGetLocation() {
    cut = new Player("player 1", serverWorker);

    var location = cut.getLocation();

    assertEquals(Location.LOBBY, location);
  }

  @Test
  @DisplayName("Test setLocation")
  void testSetLocation() {
    cut = new Player("player 1", serverWorker);

    cut.setLocation(Location.INGAME);
    var location = cut.getLocation();

    assertEquals(Location.INGAME, location);
  }

  @Test
  @DisplayName("Test setLocation null location")
  void testSetLocation2() {
    cut = new Player("player 1", serverWorker);
    assertThrows(NullPointerException.class, () -> cut.setLocation(null));
  }

  @Test
  @DisplayName("Test setCurrentQuizSession")
  void testSetCurrentQuizSession() {
    cut = new Player("player 1", serverWorker);

    var players = new ArrayList<Player>();
    players.add(cut);
    var expectedQuizSession = new QuizSession(server, players);
    cut.setCurrentQuizSession(expectedQuizSession);
    var actualQuizSession = cut.getCurrentQuizSession();

    assertEquals(expectedQuizSession, actualQuizSession);
  }

  @Test
  @DisplayName("Test setCurrentQuizSession null quizsession")
  void testSetCurrentQuizSession2() {
    cut = new Player("player 1", serverWorker);
    assertThrows(NullPointerException.class, () -> cut.setCurrentQuizSession(null));
  }
}
