package ch.ffhs.ftoop.doppelgruen.quiz.answer_question_handling;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.logging.Logger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class QuestionFileTest {

  private QuestionFile cut;
  private final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  @Test
  @DisplayName("Test constructor normal case")
  void testConstructor1() {
    cut = new QuestionFile(logger, "ftoop_multiplayerquiz_fragenkatalog_", "txt");
    assertNotNull(cut);
  }

  @Test
  @DisplayName("Test constructor empty filename")
  void testConstructor2() {
    assertThrows(IllegalArgumentException.class, () -> new QuestionFile(logger, "", "txt"));
  }

  @Test
  @DisplayName("Test constructor filename null")
  void testConstructor3() {
    assertThrows(NullPointerException.class, () -> new QuestionFile(logger, null, "txt"));
  }

  @Test
  @DisplayName("Test constructor empty file extension")
  void testConstructor4() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new QuestionFile(logger, "ftoop_multiplayerquiz_fragenkatalog_", ""));
  }

  @Test
  @DisplayName("Test constructor file extension null")
  void testConstructor5() {
    assertThrows(
        NullPointerException.class,
        () -> new QuestionFile(logger, "ftoop_multiplayerquiz_fragenkatalog_", null));
  }

  @ParameterizedTest(name = "Test readQuestions()  {index}: ''{0}''")
  @CsvSource({
    "ftoop_multiplayerquiz_fragenkatalog_, 117",
    "Fragenkatalog_ohne, 3",
    "Fragenkatalog_mit, 1"
  })
  void testReadQuestions(String fileName, int amount) {
    cut = new QuestionFile(logger, fileName, "txt");

    List<Question> actual = cut.readQuestions();

    assertEquals(amount, actual.size());
  }
}
