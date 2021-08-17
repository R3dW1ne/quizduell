package ch.ffhs.ftoop.doppelgruen.quiz.answer_question_handling;

import java.util.Objects;

/** An immutable Instance of this class stores an answer. It will be used by questions. */
public class Answer {
  private final String text;
  private final boolean correctAnswer;

  /**
   * Creates a new answer
   *
   * @param text text of an answer
   * @param correctAnswer {@code true} if it is the right answer
   * @throws IllegalArgumentException If text is empty
   * @throws NullPointerException If text is null
   */
  public Answer(final String text, final boolean correctAnswer) {
    Objects.requireNonNull(text, "text should not be null");
    if (text.isEmpty()) throw new IllegalArgumentException("text should not be empty");

    this.text = text;
    this.correctAnswer = correctAnswer;
  }

  /** @return the id with the text */
  public String getAnswer() {
    return text;
  }

  /** @return is true if answer is correct */
  public boolean isThisAnswerCorrect() {
    return correctAnswer;
  }
}
