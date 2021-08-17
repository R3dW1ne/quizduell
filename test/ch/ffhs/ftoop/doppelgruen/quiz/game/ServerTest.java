package ch.ffhs.ftoop.doppelgruen.quiz.game;

import ch.ffhs.ftoop.doppelgruen.quiz.answer_question_handling.QuestionList;
import ch.ffhs.ftoop.doppelgruen.quiz.client.Client;
import ch.ffhs.ftoop.doppelgruen.quiz.utils.SleepUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class ServerTest {

  private Server cut;
  private static final int SERVER_PORT = 60800;
  private static final int SAFESLEEP_MILLISECONDS = 100;

  @BeforeAll
  static void setUp() {}

  @Test
  @DisplayName("Test constructor normal case")
  void testConstructor1() {
    cut = new Server(SERVER_PORT);
    assertNotNull(cut);
  }

  @Test
  @DisplayName("Test constructor serverPort 0 case")
  void testConstructor2() {
    assertThrows(IllegalArgumentException.class, () -> new Server(0));
  }

  @Test
  @DisplayName("Test getQuestionList")
  void testGetQuestionList() {
    cut = new Server(SERVER_PORT);
    assertEquals(QuestionList.class, cut.getQuestionList().getClass());
  }

  @Test
  @DisplayName("Test getLogger")
  void TestGetLogger() {
    cut = new Server(SERVER_PORT);
    assertEquals(Logger.class, cut.getLogger().getClass());
  }

  @Test
  @DisplayName("Test getPlayerController")
  void testGetPlayerController() {
    cut = new Server(SERVER_PORT);
    assertEquals(Players.class, cut.getPlayerController().getClass());
  }

  @Test
  @DisplayName("Test serverSocket.accept() in run")
  void testServerSocketAcceptClient() {
    cut = new Server(SERVER_PORT);
    var serverThread = new Thread(cut);
    serverThread.start();
    SleepUtils.safeSleep(TimeUnit.MILLISECONDS, SAFESLEEP_MILLISECONDS);

    Client client = Client.createClient("localhost", SERVER_PORT);
    if (client == null) fail();
    var socket = client.getSocket();
    assertNotNull(socket);
  }

  @Test
  @DisplayName("")
  void getGameMatcher() {
    cut = new Server(SERVER_PORT);
    assertEquals(LookingForGame.class, cut.getGameMatcher().getClass());
  }
}
