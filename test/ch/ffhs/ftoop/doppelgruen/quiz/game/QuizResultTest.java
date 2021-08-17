package ch.ffhs.ftoop.doppelgruen.quiz.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import ch.ffhs.ftoop.doppelgruen.quiz.client.Client;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class QuizResultTest {

  private static Thread serverThread;
  private static ServerWorker serverWorker;
  private static final int SERVER_PORT = 60600;

  private QuizResult cut;

  @BeforeAll
  static void setUp() {
    var server = new Server(SERVER_PORT);
    serverThread = new Thread(server);
    serverThread.start();
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
    cut = new QuizResult();
    assertNotNull(cut);
  }

  @Test
  @DisplayName("Test get player with correct answer")
  void testPlayerWithCorrectAnswer1() {
    cut = new QuizResult();
    assertNull(cut.getPlayerWithCorrectAnswer());
  }

  @Test
  @DisplayName("Test set player with correct answer")
  void testPlayerWithCorrectAnswer2() {
    cut = new QuizResult();

    var player = new Player("player 1", serverWorker);
    cut.setPlayerWithCorrectAnswer(player);
    assertEquals(player, cut.getPlayerWithCorrectAnswer());
  }

  @Test
  @DisplayName("Test set null player with correct answer")
  void testPlayerWithCorrectAnswer3() {
    cut = new QuizResult();
    assertThrows(NullPointerException.class, () -> cut.setPlayerWithCorrectAnswer(null));
  }

  @Test
  @DisplayName("Test give an Answer")
  void testGiveAnAnswer1() {
    cut = new QuizResult();

    var player = new Player("player 1", serverWorker);
    cut.giveAnAnswer(player);
    assertEquals(1, cut.getPlayersAlreadyGaveAnAnswer().size());
  }

  @Test
  @DisplayName("Test give an null Answer")
  void testGiveAnAnswer2() {
    cut = new QuizResult();

    assertThrows(NullPointerException.class, () -> cut.giveAnAnswer(null));
  }

  @Test
  @DisplayName("Test is Answer Already Given By null player")
  void testIsAnswerAlreadyGivenBy1() {
    cut = new QuizResult();

    assertThrows(NullPointerException.class, () -> cut.isAnswerAlreadyGivenBy(null));
  }

  @Test
  @DisplayName("Test is Answer Already Given By null player")
  void testIsAnswerAlreadyGivenBy2() {
    cut = new QuizResult();

    var player = new Player("player 1", serverWorker);
    assertFalse(cut.isAnswerAlreadyGivenBy(player));
    cut.giveAnAnswer(player);
    assertTrue(cut.isAnswerAlreadyGivenBy(player));
  }

  @Test
  @DisplayName("Test give an Answer")
  void testGiveAnAnswer3() {
    cut = new QuizResult();

    var player = new Player("player 1", serverWorker);
    cut.giveAnAnswer(player);
    cut.giveAnAnswer(player);
    assertEquals(2, cut.getPlayersAlreadyGaveAnAnswer().size());
  }

  @Test
  @DisplayName("Test is Question Already Answered Correct")
  void testIsQuestionAlreadyAnsweredCorrect() {
    cut = new QuizResult();

    var player = new Player("player 1", serverWorker);
    assertFalse(cut.isQuestionAlreadyAnsweredCorrect());
    cut.setPlayerWithCorrectAnswer(player);
    assertTrue(cut.isQuestionAlreadyAnsweredCorrect());
  }
}
