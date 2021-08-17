package ch.ffhs.ftoop.doppelgruen.quiz.game;

import ch.ffhs.ftoop.doppelgruen.quiz.client.Client;
import ch.ffhs.ftoop.doppelgruen.quiz.utils.SleepUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class LookingForGameTest {

  private static Server server;
  private static Thread serverThread;
  private static ServerWorker serverWorker;
  private static Players players;
  private static final int SERVER_PORT = 60200;
  private static final int SAFESLEEP_MILLISECONDS = 100;

  private LookingForGame cut;

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
    players = server.getPlayerController();
  }

  @AfterAll
  static void tearDown() {
    serverThread.interrupt();
  }

  @Test
  @DisplayName("Test constructor")
  void testConstrucor1() {
    cut = new LookingForGame(server);
    assertNotNull(cut);
  }

  @Test
  @DisplayName("Test constructor null server")
  void testConstructor2() {
    assertThrows(NullPointerException.class, () -> new LookingForGame(null));
  }

  @Test
  @DisplayName("Test matchmaking")
  void testMatchmaking() {
    var player1 = players.createPlayerIfPossible("player1", serverWorker);
    var player2 = players.createPlayerIfPossible("player2", serverWorker);

    player1.setLocation(Location.LOOKING_FOR_GAME);
    player2.setLocation(Location.LOOKING_FOR_GAME);

    cut = new LookingForGame(server);
    new Thread(cut).start();
    cut.activateMatchMaking();
    SleepUtils.safeSleep(TimeUnit.MILLISECONDS, SAFESLEEP_MILLISECONDS);

    assertEquals(Location.INGAME, player1.getLocation());
    assertEquals(Location.INGAME, player2.getLocation());
  }

  @Test
  @DisplayName("Test matchmaking2")
  void testMatchmaking2() {
    var server = new Server(SERVER_PORT);
    var serverThread = new Thread(server);
    serverThread.start();
    var client = Client.createClient("localhost", SERVER_PORT);
    if (client == null) {
      fail();
    }
    var serverWorker = new ServerWorker(server, client.getSocket());
    var players = server.getPlayerController();

    var player1 = players.createPlayerIfPossible("player1", serverWorker);
    var player2 = players.createPlayerIfPossible("player2", serverWorker);
    var player3 = players.createPlayerIfPossible("player3", serverWorker);
    var player4 = players.createPlayerIfPossible("player4", serverWorker);
    var player5 = players.createPlayerIfPossible("player5", serverWorker);

    player1.setLocation(Location.LOOKING_FOR_GAME);
    player2.setLocation(Location.LOOKING_FOR_GAME);
    player3.setLocation(Location.LOOKING_FOR_GAME);
    player4.setLocation(Location.LOOKING_FOR_GAME);
    player5.setLocation(Location.LOOKING_FOR_GAME);

    cut = new LookingForGame(server);
    new Thread(cut).start();
    cut.activateMatchMaking();
    SleepUtils.safeSleep(TimeUnit.MILLISECONDS, SAFESLEEP_MILLISECONDS);

    assertEquals(Location.INGAME, player1.getLocation());
    assertEquals(Location.INGAME, player2.getLocation());
    assertEquals(Location.INGAME, player3.getLocation());
    assertEquals(Location.INGAME, player4.getLocation());
    assertEquals(Location.LOOKING_FOR_GAME, player5.getLocation());
  }
}
