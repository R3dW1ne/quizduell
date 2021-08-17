package ch.ffhs.ftoop.doppelgruen.quiz.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CommandTest {

  private Command cut;

  @Test
  @DisplayName("Test constructor normal case")
  void testConstructor1() {
    cut = new Command(CommandWord.LOGIN, "asd1");
    assertNotNull(cut);
  }

  @Test
  @DisplayName("Test constructor null command")
  void testConstructor2() {
    assertThrows(NullPointerException.class, () -> new Command(null, null));
  }

  @Test
  @DisplayName("Test hasSecondWord")
  void testHasSecondWord() {
    cut = new Command(CommandWord.LOGIN, "Second Word");
    assertTrue(cut.hasSecondWord());
  }

  @Test
  @DisplayName("Test getSecondWord")
  void testGetSecondWord() {
    cut = new Command(CommandWord.LOGIN, "Second Word");
    assertEquals("Second Word", cut.getSecondWord());
  }

  @Test
  @DisplayName("Test getCommandInt")
  void testGetCommandInt() {
    cut = new Command(CommandWord.KEY_1, null);
    assertEquals(1, cut.getCommandInt());
  }

  @Test
  @DisplayName("Test getCommandWord")
  void testGetCommandWord() {
    cut = new Command(CommandWord.LOGIN, null);
    assertEquals(CommandWord.LOGIN, cut.getCommandWord());
  }
}
