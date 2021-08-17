package ch.ffhs.ftoop.doppelgruen.quiz.answer_question_handling;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

/**
 * An instance of this class provides access to all questions of the game. The questions were read
 * from file in the constructor. The game will usually only need one instance of this class.
 */
public class QuestionList {

  private final List<Question> questions;
  private final Random rnd = new Random();

  /** Reads all questions from filesystem to memory. No parameter needed! */
  public QuestionList() {
    var fl =
        new QuestionFile(
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME),
            "ftoop_multiplayerquiz_fragenkatalog_",
            "txt");
    questions = fl.readQuestions();
  }

  public QuestionList(final Logger logger) {
    var fl = new QuestionFile(logger, "ftoop_multiplayerquiz_fragenkatalog_", "txt");
    questions = fl.readQuestions();
  }

  /**
   * Saves all created indices to ensure that all found questions are unique. The more questions are
   * found, the slower the algorithm becomes. But that is not important for this project because we
   * only need 9 different questions for a game.
   *
   * @param numberOfRequestedQuestions number of needed questions
   * @return List of questions
   * @throws IllegalArgumentException If numberOfRequestedQuestions < 1 or if
   *     numberOfRequestedQuestions greater then questions.size()
   */
  public List<Question> getUniqueRandomQuestions(final int numberOfRequestedQuestions) {
    if (numberOfRequestedQuestions < 1) {
      throw new IllegalArgumentException("numberOfRequestedQuestions should not be smaller 1");
    }
    if (numberOfRequestedQuestions > questions.size()) {
      throw new IllegalArgumentException(
          "numberOfRequestedQuestions should not be greater than the number of available questions ");
    }

    List<Question> result = new ArrayList<>();
    List<Integer> generatedIndices = new ArrayList<>();

    for (var i = 0; i < numberOfRequestedQuestions; i++) {

      var newIndexFound = false;
      var generatedIndex = -1;

      // the more questions are found, the slower the algorithm becomes
      // but this is not important for this project
      while (!newIndexFound && generatedIndices.size() < questions.size()) {
        generatedIndex = rnd.nextInt(questions.size());

        if (!generatedIndices.contains(generatedIndex)) {
          generatedIndices.add(generatedIndex);
          newIndexFound = true;
        }
      }

      if (generatedIndex != -1) {
        result.add(questions.get(generatedIndex));
      }
    }
    return result;
  }
}
