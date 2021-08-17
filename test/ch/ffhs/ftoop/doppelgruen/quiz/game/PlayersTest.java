package ch.ffhs.ftoop.doppelgruen.quiz.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import ch.ffhs.ftoop.doppelgruen.quiz.client.Client;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import ch.ffhs.ftoop.doppelgruen.quiz.utils.SleepUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PlayersTest {

  private static Thread serverThread;
  private static ServerWorker serverWorker;
  private static final int SERVER_PORT = 60300;
  private static final int SAFESLEEP_MILLISECONDS = 100;

  private Players cut;

  @BeforeAll
  static void setUp() {
    var server = new Server(SERVER_PORT);
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
    cut = new Players();
    assertNotNull(cut);
  }

  @Test
  @DisplayName("Test getAllPlayers")
  void testGetAllPlayers() {
    cut = new Players();
    assertEquals(new CopyOnWriteArrayList<>(), cut.getAllPlayers());

    var player = cut.createPlayerIfPossible("player", serverWorker);
    var expected = new CopyOnWriteArrayList<>();
    expected.add(player);
    assertEquals(expected, cut.getAllPlayers());
  }

  @Test
  @DisplayName("Test getAmountOfAllPlayers")
  void testGetAmountOfAllPlayers() {
    cut = new Players();
    assertEquals(0, cut.getAmountOfAllPlayers());

    cut.createPlayerIfPossible("player", serverWorker);
    assertEquals(1, cut.getAmountOfAllPlayers());
  }

  @Test
  @DisplayName("Test getPlayersIn location")
  void getPlayersIn1() {
    cut = new Players();
    assertEquals(new CopyOnWriteArrayList<>(), cut.getPlayersIn(Location.LOBBY));

    var player = cut.createPlayerIfPossible("player", serverWorker);
    player.setLocation(Location.INGAME);
    var expected = new CopyOnWriteArrayList<>();
    expected.add(player);
    assertEquals(expected, cut.getPlayersIn(Location.INGAME));
  }

  @Test
  @DisplayName("Test getPlayersIn null location")
  void getPlayersIn2() {
    cut = new Players();
    assertThrows(NullPointerException.class, () -> cut.getPlayersIn(null));
  }

  @Test
  @DisplayName("Test createPlayerIfPossible")
  void testCreatePlayerIfPossible1() {
    cut = new Players();
    var player = cut.createPlayerIfPossible("player 1", serverWorker);
    assertNotNull(player);
  }

  @Test
  @DisplayName("Test create 2 different players")
  void testCreatePlayerIfPossible2() {
    cut = new Players();
    cut.createPlayerIfPossible("player 1", serverWorker);
    cut.createPlayerIfPossible("player 2", serverWorker);

    assertEquals(2, cut.getAmountOfAllPlayers());
  }

  @Test
  @DisplayName("Test create 2 same players")
  void testCreatePlayerIfPossible3() {
    cut = new Players();
    cut.createPlayerIfPossible("player 1", serverWorker);
    var player = cut.createPlayerIfPossible("player 1", serverWorker);

    assertNull(player);
  }

  @Test
  @DisplayName("Test create player empty string")
  void testCreatePlayerIfPossible4() {
    cut = new Players();
    assertThrows(
        IllegalArgumentException.class, () -> cut.createPlayerIfPossible("", serverWorker));
  }

  @Test
  @DisplayName("Test create player null string")
  void testCreatePlayerIfPossible5() {
    cut = new Players();
    assertThrows(NullPointerException.class, () -> cut.createPlayerIfPossible(null, serverWorker));
  }

  @Test
  @DisplayName("Test create player null serverWorker")
  void testCreatePlayerIfPossible6() {
    cut = new Players();
    assertThrows(NullPointerException.class, () -> cut.createPlayerIfPossible("player 1", null));
  }

  @Test
  @DisplayName("Test delete player")
  void testDeletePlayer() {
    cut = new Players();
    cut.createPlayerIfPossible("player 1", serverWorker);
    var player = cut.createPlayerIfPossible("player 2", serverWorker);

    assertTrue(cut.deletePlayer(player));
  }

  @Test
  @DisplayName("Test delete null player")
  void testDeletePlayer2() {
    cut = new Players();
    assertThrows(NullPointerException.class, () -> cut.deletePlayer(null));
  }

  @Test
  @DisplayName("Test delete not valid player")
  void testDeletePlayer3() {
    cut = new Players();
    cut.createPlayerIfPossible("player 1", serverWorker);
    cut.createPlayerIfPossible("player 2", serverWorker);

    var player = new Player("player 1", serverWorker);

    assertFalse(cut.deletePlayer(player));
  }
}
