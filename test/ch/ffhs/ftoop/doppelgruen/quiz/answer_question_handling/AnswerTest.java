package ch.ffhs.ftoop.doppelgruen.quiz.answer_question_handling;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AnswerTest {

  private Answer cut;

  @Test
  @DisplayName("Test constructor normal case")
  void testConstructor1() {
    cut = new Answer("Antwort", false);
    assertNotNull(cut);
  }

  @Test
  @DisplayName("Test get answer")
  void testGetAnswers() {
    cut = new Answer("Antwort", true);

    String actual = cut.getAnswer();

    String expected = "Antwort";
    assertEquals(actual, expected);
  }

  @Test
  @DisplayName("Test correct answer")
  void testcorrectAnswers() {
    cut = new Answer("Antwort", true);

    boolean actual = cut.isThisAnswerCorrect();

    assertTrue(actual);
  }

  @Test
  @DisplayName("Test constructor empty text")
  void testConstructor2() {
    assertThrows(IllegalArgumentException.class, () -> new Answer("", false));
  }

  @Test
  @DisplayName("Test constructor null text")
  void testConstructor3() {
    assertThrows(NullPointerException.class, () -> new Answer(null, false));
  }
}
