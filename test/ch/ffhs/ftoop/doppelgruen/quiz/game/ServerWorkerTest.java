package ch.ffhs.ftoop.doppelgruen.quiz.game;

import ch.ffhs.ftoop.doppelgruen.quiz.client.Client;
import ch.ffhs.ftoop.doppelgruen.quiz.utils.SleepUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class ServerWorkerTest {
  private static Server server;
  private static Client client;
  private static final int SERVER_PORT = 60900;
  private static final int SAFESLEEP_MILLISECONDS = 100;

  private ServerWorker cut;

  @BeforeAll
  static void setUp() {
    server = new Server(SERVER_PORT);
    var serverThread = new Thread(server);
    serverThread.start();
    SleepUtils.safeSleep(TimeUnit.MILLISECONDS, SAFESLEEP_MILLISECONDS);
    client = Client.createClient("localhost", SERVER_PORT);
  }

  @Test
  @DisplayName("Test constructor normal case")
  void testConstructor1() {
    cut = new ServerWorker(server, client.getSocket());

    assertNotNull(cut);
  }

  @Test
  @DisplayName("Test constructor null server case")
  void testConstructor2() {
    var socket = client.getSocket();
    assertThrows(NullPointerException.class, () -> new ServerWorker(null, socket));
  }

  @Test
  @DisplayName("Test constructor null clientsocket case")
  void testConstructor3() {
    assertThrows(NullPointerException.class, () -> new ServerWorker(server, null));
  }

  @Test
  @DisplayName("Test login and get worker from player")
  void testGetWorkerFromPlayer() {

    try {
      client.sendMsgToServer("/login Player1");
    } catch (IOException e) {
      fail();
    }
    SleepUtils.safeSleep(TimeUnit.MILLISECONDS, SAFESLEEP_MILLISECONDS);

    Players players = server.getPlayerController();
    cut = players.getAllPlayers().get(0).getWorker();
    assertNotNull(cut);
  }
}
