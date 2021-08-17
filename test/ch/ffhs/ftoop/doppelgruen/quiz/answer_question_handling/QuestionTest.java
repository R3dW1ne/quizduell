package ch.ffhs.ftoop.doppelgruen.quiz.answer_question_handling;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class QuestionTest {

  private Question cut;
  private Question.Builder qb;

  @Test
  @DisplayName("Test constructor normal case")
  void testConstructor1() {
    Map<Integer, Answer> answers = new HashMap<>();
    answers.put(1, new Answer("Antwortmöglichkeit 1", false));
    answers.put(2, new Answer("Antwortmöglichkeit 2", true));

    cut = new Question("Frage", answers);

    assertNotNull(cut);
  }

  @Test
  @DisplayName("Test constructor text null")
  void testConstructor2() {
    Map<Integer, Answer> answers = new HashMap<>();
    answers.put(1, new Answer("Antwortmöglichkeit 1", false));
    answers.put(2, new Answer("Antwortmöglichkeit 2", true));

    assertThrows(NullPointerException.class, () -> new Question(null, answers));
  }

  @Test
  @DisplayName("Test constructor text empty")
  void testConstructor3() {
    Map<Integer, Answer> answers = new HashMap<>();
    answers.put(1, new Answer("Antwortmöglichkeit 1", true));

    assertThrows(IllegalArgumentException.class, () -> new Question("", answers));
  }

  @Test
  @DisplayName("Test constructor answers null")
  void testConstructor4() {
    assertThrows(NullPointerException.class, () -> new Question("Frage", null));
  }

  @Test
  @DisplayName("Test constructor answers empty")
  void testConstructor5() {
    Map<Integer, Answer> answers = new HashMap<>();
    assertThrows(IllegalArgumentException.class, () -> new Question("Frage", answers));
  }

  @Test
  @DisplayName("Test constructor no correct Answer")
  void testConstructor6() {
    Map<Integer, Answer> answers = new HashMap<>();
    answers.put(1, new Answer("Antwortmöglichkeit 1", false));
    answers.put(2, new Answer("Antwortmöglichkeit 2", false));

    assertThrows(IllegalArgumentException.class, () -> new Question("Frage", answers));
  }

  @Test
  @DisplayName("Test constructor too many correct Answer")
  void testConstructor7() {
    Map<Integer, Answer> answers = new HashMap<>();
    answers.put(1, new Answer("Antwortmöglichkeit 1", true));
    answers.put(2, new Answer("Antwortmöglichkeit 2", true));

    assertThrows(IllegalArgumentException.class, () -> new Question("Frage", answers));
  }

  @Test
  @DisplayName("Test getQuestionWithAnswers normal case")
  void testgetQuestionWithAnswers() {
    Map<Integer, Answer> answers = new HashMap<>();
    answers.put(1, new Answer("Antwortmöglichkeit 1", false));
    answers.put(2, new Answer("Antwortmöglichkeit 2", true));

    cut = new Question("Frage", answers);

    assertEquals(
        cut.getQuestionWithAnswersInMultiline(),
            """
                    Frage\r
                    (1) Antwortmöglichkeit 1\r
                    (2) Antwortmöglichkeit 2\r
                    """);
  }

  @Test
  @DisplayName("Test isAnswerCorrect normal case")
  void testisAnswerCorrect() {
    Map<Integer, Answer> answers = new HashMap<>();
    answers.put(1, new Answer("Antwortmöglichkeit 1", false));
    answers.put(2, new Answer("Antwortmöglichkeit 2", true));

    cut = new Question("Frage", answers);

    boolean actual = cut.isAnswerCorrect(2);

    assertTrue(actual);
  }

  @Test
  @DisplayName("Test builder constructor normal case")
  void testBuilderConstructor1() {
    qb = new Question.Builder();
    assertNotNull(qb);
  }


  @Test
  @DisplayName("Test add no question")
  void addNoQuestion() {
    qb = new Question.Builder();
    qb.answer(new Answer("Antwort", true));

    assertThrows(IllegalArgumentException.class, () -> qb.build());
  }

  @Test
  @DisplayName("Test add no answer")
  void addNoAnswer() {
    qb = new Question.Builder();
    qb.question("Frage");

    assertThrows(IllegalArgumentException.class, () -> qb.build());
  }

  @Test
  @DisplayName("Test 1 answer")
  void oneAnswer() {
    qb = new Question.Builder();
    Answer answer = new Answer("Antwort", true);

    qb.question("Frage");
    qb.answer(answer);

    assertThrows(IllegalArgumentException.class, () -> qb.build());
  }

  @Test
  @DisplayName("Test 3 answer")
  void normalCase3Answer() {
    qb = new Question.Builder();
    Answer answer1 = new Answer("Antwort", true);
    Answer answer2 = new Answer("Antwort", false);
    Answer answer3 = new Answer("Antwort", false);
    Map<Integer, Answer> answers = new HashMap<>();
    answers.put(1, answer1);
    answers.put(2, answer2);
    answers.put(3, answer3);

    qb.question("Frage");
    qb.answer(answer1);
    qb.answer(answer2);
    qb.answer(answer3);

    Question expected = new Question("Frage", answers);
    assertEquals(qb.build().toString(), expected.toString());
  }

  @Test
  @DisplayName("Test get correct Answers")
  void testGetCorrectAnswer() {
    qb = new Question.Builder();
    Answer answer1 = new Answer("Antwort", true);
    Answer answer2 = new Answer("Antwort", false);
    Map<Integer, Answer> answers = new HashMap<>();
    answers.put(1, answer1);
    answers.put(2, answer2);

    qb.question("Frage");
    qb.answer(answer1);
    qb.answer(answer2);

    Question expected = new Question("Frage", answers);
    assertEquals(1, expected.getCorrectAnswer());
  }
}
