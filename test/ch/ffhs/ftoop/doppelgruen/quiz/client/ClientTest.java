package ch.ffhs.ftoop.doppelgruen.quiz.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import ch.ffhs.ftoop.doppelgruen.quiz.game.Server;
import java.io.IOException;
import java.lang.reflect.Executable;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ClientTest {

  private static Thread serverThread;
  private static final int SERVER_PORT = 60100;
  private static final int FALSE_SERVER_PORT = 50000;

  @BeforeAll
  static void setUp() {
    serverThread = new Thread(new Server(SERVER_PORT));
    serverThread.start();
  }

  @AfterAll
  static void tearDown() {
    serverThread.interrupt();
  }

  @Test
  @DisplayName("Test testFactoryMethod normal case")
  void testFactoryMethod1() {
    var client = Client.createClient("localhost", SERVER_PORT);
    assertNotNull(client);
  }

  @Test
  @DisplayName("Test testFactoryMethod null serverName")
  void testFactoryMethod2() {

    assertThrows(NullPointerException.class, () -> Client.createClient(null, SERVER_PORT));
  }

  @Test
  @DisplayName("Test testFactoryMethod empty serverName")
  void testFactoryMethod3() {
    assertThrows(IllegalArgumentException.class, () -> Client.createClient("", SERVER_PORT));
  }

  @Test
  @DisplayName("Test testFactoryMethod false serverName")
  void testFactoryMethod4() {
    var objectToTest = Client.createClient("localhorst", SERVER_PORT);
    assertNull(objectToTest);
  }

  @Test
  @DisplayName("Test testFactoryMethod false ServerPort")
  void testFactoryMethod5() {
    //ACT
    var objectToTest = Client.createClient("localhost", FALSE_SERVER_PORT);

    //ASSERT
    assertNull(objectToTest);
  }

  @Test
  @DisplayName("Test serverOutput after normal connection")
  void testSendMsgToServer() {
    //ARRANGE
    var client = Client.createClient("localhost", SERVER_PORT);
    if (client == null) {
      fail();
    }

    //ACT
    String expected =
        "   _____        .__   __  .__       .__                                        ________";
    String result = "";
    try {
      result = client.getBufferedIn().readLine();
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }

    //ASSERT
    assertEquals(expected, result);
  }
}
