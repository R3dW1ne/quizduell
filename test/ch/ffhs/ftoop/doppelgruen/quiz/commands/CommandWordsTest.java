package ch.ffhs.ftoop.doppelgruen.quiz.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CommandWordsTest {

  private CommandWords cut;

  @Test
  @DisplayName("Test constructor normal case")
  void testConstructor1() {
    cut = new CommandWords();
    assertNotNull(cut);
  }

  @Test
  @DisplayName("Test toString")
  void testToString() {
    cut = new CommandWords();
    assertEquals("/login  /quit  /help  ", cut.toString());
  }

  @Test
  @DisplayName("Test toString after remove all")
  void testToString1() {
    cut = new CommandWords();
    cut.removeAllCommands();
    assertEquals("", cut.toString());
  }

  @Test
  @DisplayName("Test getCommandWord")
  void testGetCommandWord() {
    cut = new CommandWords();
    assertEquals(CommandWord.LOGIN, cut.getCommandWord("/login"));
    assertEquals(CommandWord.QUIT, cut.getCommandWord("/quit"));
    assertEquals(CommandWord.HELP, cut.getCommandWord("/help"));
  }

  @Test
  @DisplayName("Test addCommandWord")
  void testAddCommandWord() {
    cut = new CommandWords();
    cut.addCommand(CommandWord.PLAY);
    assertEquals(CommandWord.PLAY, cut.getCommandWord("/play"));
  }
}
