package ch.ffhs.ftoop.doppelgruen.quiz.answer_question_handling;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/** An immutable instance of this class stores an question with its possible answers. */
public class Question {

  private final String text;
  private final Map<Integer, Answer> possibleAnswers;

  /**
   * Creates a new question
   *
   * @param text text of an question
   * @param answers list of possible answers
   * @throws NullPointerException If {@param text} is null
   * @throws NullPointerException If {@param answers} is null
   * @throws IllegalArgumentException If {@param text} is empty
   * @throws IllegalArgumentException If {@param number} of answers < 2
   * @throws IllegalArgumentException If not exactly one {@param answer} is true
   */
  public Question(final String text, final Map<Integer, Answer> answers) {
    Objects.requireNonNull(text, "text should not be null");
    Objects.requireNonNull(answers, "answers should not be null");
    if (text.isEmpty()) {
      throw new IllegalArgumentException("text should not be empty");
    }
    if (answers.size() < 2) {
      throw new IllegalArgumentException("Possible answers should not be at least 2");
    }
    if (notExactlyOneAnswerIsTrue(answers)) {
      throw new IllegalArgumentException("Exactly one answer have to be true");
    }

    this.text = text;
    this.possibleAnswers = new HashMap<>();
    this.possibleAnswers.putAll(answers);
  }

  private boolean notExactlyOneAnswerIsTrue(final Map<Integer, Answer> answers) {
    var numberOfCorrectAnswers = 0;
    for (Map.Entry<Integer, Answer> entry : answers.entrySet()) {
      if (entry.getValue().isThisAnswerCorrect()) {
        numberOfCorrectAnswers++;
      }
    }
    return (numberOfCorrectAnswers != 1);
  }

  /**
   * String with question and answers for output purpose. Every answer is in a new line
   *
   * @return a multiline string
   */
  public String getQuestionWithAnswersInMultiline() {
    var sb = new StringBuilder().append(text).append("\r\n");
    for (Map.Entry<Integer, Answer> entry : possibleAnswers.entrySet()) {
      sb.append("(")
          .append(entry.getKey())
          .append(") ")
          .append(entry.getValue().getAnswer())
          .append("\r\n");
    }
    return sb.toString();
  }

  /** @return The number of the correct answer. Is -1 if there is no correct answer. */
  public int getCorrectAnswer() {
    // for each not best solution for iteration!
    for (Map.Entry<Integer, Answer> entry : possibleAnswers.entrySet()) {
      if (entry.getValue().isThisAnswerCorrect()) {
        return entry.getKey();
      }
    }
    return -1;
  }

  public boolean isAnswerCorrect(final int key) {
    return possibleAnswers.get(key).isThisAnswerCorrect();
  }

  /**
   * Returns the question with all its answers line by line. The exact details of the representation
   * are unspecified and subject to change, , but the following may be regarded as typical:
   * "[Question: text=How many leaves..., noOfAnswers=3]"
   *
   * @return the instance as string
   */
  @Override
  public String toString() {
    return "[Question: text=" + text + ", noOfAnswers=" + possibleAnswers.size() + "]";
  }

  /** Builder-pattern for Questions. Needed for immutable question object */
  public static class Builder {

    private final Map<Integer, Answer> answers = new HashMap<>();
    private String question = "";

    public Builder answer(Answer answer) {
      answers.put(answers.size() + 1, answer);
      return this;
    }

    public Builder question(String text) {
      question = text;
      return this;
    }

    public boolean isQuestionadded() {
      return (!question.isEmpty());
    }

    /**
     * Build an instance of the class question
     *
     * @return instance of question
     */
    public Question build() {
      return new Question(question, answers);
    }
  }
}
