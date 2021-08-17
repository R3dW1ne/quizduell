package ch.ffhs.ftoop.doppelgruen.quiz.answer_question_handling;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class QuestionListTest {
  private QuestionList cut;

  @Test
  @DisplayName("Test constructor normal case")
  void testConstructor1() {
    cut = new QuestionList();
    assertNotNull(cut);
  }

  @Test
  @DisplayName("Test get 1 random question")
  void testOneRandomQuestion() {
    cut = new QuestionList();

    List<Question> actual = cut.getUniqueRandomQuestions(1);

    assertEquals(1, actual.size());
  }

  @Test
  @DisplayName("Test get 2 random questions")
  void testTwoRandomQuestion() {
    cut = new QuestionList();

    List<Question> questions = cut.getUniqueRandomQuestions(2);

    assertNotEquals(questions.get(0), questions.get(1));
  }

  @Test
  @DisplayName("Test get 9 random questions")
  void test9RandomQuestion() {
    cut = new QuestionList();

    List<Question> questions = cut.getUniqueRandomQuestions(9);

    assertEquals(9, questions.size());
  }

  @Test
  @DisplayName("Test get 9 random questions very often")
  void test9RandomQuestionVeryOften() {
    cut = new QuestionList();

    cut.getUniqueRandomQuestions(9);
    cut.getUniqueRandomQuestions(9);
    cut.getUniqueRandomQuestions(9);
    cut.getUniqueRandomQuestions(9);
    cut.getUniqueRandomQuestions(9);
    cut.getUniqueRandomQuestions(9);
    cut.getUniqueRandomQuestions(9);
    cut.getUniqueRandomQuestions(9);
    cut.getUniqueRandomQuestions(9);
    cut.getUniqueRandomQuestions(9);
    cut.getUniqueRandomQuestions(9);
    cut.getUniqueRandomQuestions(9);
    cut.getUniqueRandomQuestions(9);
    cut.getUniqueRandomQuestions(9);
    cut.getUniqueRandomQuestions(9);
    cut.getUniqueRandomQuestions(9);
    cut.getUniqueRandomQuestions(9);
    cut.getUniqueRandomQuestions(9);
    cut.getUniqueRandomQuestions(9);
    cut.getUniqueRandomQuestions(9);

    List<Question> questions = cut.getUniqueRandomQuestions(9);

    assertEquals(9, questions.size());
  }

  @Test
  @DisplayName("Test get 9 unique random questions")
  void test9UniqueRandomQuestion() {
    cut = new QuestionList();

    List<Question> questions = cut.getUniqueRandomQuestions(9);

    // probably not the fastest solution, but the simplest one I know to remove duplicates
    Set<Question> questionsSet = new HashSet<>(questions);

    assertEquals(questions.size(), questionsSet.size());
  }

  @Test
  @DisplayName("Test getUniqueRandomQuestions 0 in parameter")
  void testUniqueRandomQuestion1() {
    cut = new QuestionList();

    assertThrows(IllegalArgumentException.class, () -> cut.getUniqueRandomQuestions(0));
  }

  @Test
  @DisplayName("Test getUniqueRandomQuestions 999 in parameter")
  void testUniqueRandomQuestion2() {
    cut = new QuestionList();

    assertThrows(IllegalArgumentException.class, () -> cut.getUniqueRandomQuestions(999));
  }
}
