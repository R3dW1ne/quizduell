package ch.ffhs.ftoop.doppelgruen.quiz.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import ch.ffhs.ftoop.doppelgruen.quiz.game.Location;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ParserTest {

  @Test
  @DisplayName("Test constructor normal case")
  void testConstructor1() {
    var parser = new Parser(new ByteArrayInputStream("".getBytes()));
    assertNotNull(parser);
  }

  @Test
  @DisplayName("Test constructor inputStream null")
  void testConstructor2() {
    assertThrows(NullPointerException.class, () -> new Parser(null));
  }

  @Test
  @DisplayName("Test switchToLookingForGameCommands")
  void testSwitchToLookingForGameCommands() {
    var parser = new Parser(new ByteArrayInputStream("".getBytes()));
    parser.switchToLookingForGameCommands();
    assertEquals("", parser.getAllCommandsAsString());
  }

  @Test
  @DisplayName("Test switchToInGameCommands")
  void testSwitchToInGameCommands() {
    var parser = new Parser(new ByteArrayInputStream("1".getBytes()));
    parser.switchToInGameCommands();
    Command actual = null;
    try {
      actual = parser.getCommand(null);
    } catch (IOException e) {
      fail();
    }
    assertEquals(CommandWord.KEY_1, actual.getCommandWord());
  }

  @Test
  @DisplayName("Test switchToLobbyCommands")
  void testSwitchToLobbyCommands() {
    var parser = new Parser(new ByteArrayInputStream("/stats".getBytes()));
    parser.switchToLobbyCommands();
    Command actual = null;
    try {
      actual = parser.getCommand(null);
    } catch (IOException e) {
      fail();
    }
    assertEquals(CommandWord.STATS, actual.getCommandWord());
  }

  @Test
  @DisplayName("Test getCommand /login")
  void testGetCommandLogin() {
    // Given
    var parser = new Parser(new ByteArrayInputStream("/login user".getBytes()));

    // When
    Command actual = null;
    try {
      actual = parser.getCommand(null);
    } catch (IOException e) {
      fail();
    }

    // Then
    assertEquals(CommandWord.LOGIN, actual.getCommandWord());
    assertEquals("user", actual.getSecondWord());
  }

  @Test
  @DisplayName("Test getCommand /play")
  void testGetCommandPlay() {
    // Given
    var parser = new Parser(new ByteArrayInputStream("/play".getBytes()));
    parser.switchToLobbyCommands();

    // When
    CommandWord actual = null;
    try {
      actual = parser.getCommand(null).getCommandWord();
    } catch (IOException e) {
      fail();
    }

    // Then
    assertEquals(CommandWord.PLAY, actual);
  }

  @Test
  @DisplayName("Test exception for \"/chat\"")
  void testGetCommandChat() {
    // Given
    var parser = new Parser(new ByteArrayInputStream("Hallo. Wie gehts!".getBytes()));
    parser.switchToLobbyCommands();

    // When
    Command actual = null;
    try {
      actual = parser.getCommand(Location.LOBBY);
    } catch (IOException e) {
      fail();
    }

    // Then
    assertEquals(CommandWord.CHAT, actual.getCommandWord());
    assertEquals("Hallo. Wie gehts!", actual.getSecondWord());
  }

  @Test
  @DisplayName("Test getAllCommandsAsString")
  void testGetAllCommandsAsString() {
    var parser = new Parser(new ByteArrayInputStream("".getBytes()));
    assertEquals("/login  /quit  /help  ", parser.getAllCommandsAsString());
  }
}
