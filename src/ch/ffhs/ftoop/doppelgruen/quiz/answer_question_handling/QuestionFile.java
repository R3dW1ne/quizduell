package ch.ffhs.ftoop.doppelgruen.quiz.answer_question_handling;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/** An instance of this class reads the Questions from the current class path */
public class QuestionFile {

  private final String beginningOfTheFilename;
  private final String fileExtension;
  private final Logger logger;

  /**
   * Creates a new QuestionFile
   *
   * @param beginningOfTheFilename every file that is to be read must start with this
   * @param fileExtension limit the files to be read to one extension
   */
  public QuestionFile(
      final Logger logger, final String beginningOfTheFilename, final String fileExtension) {
    Objects.requireNonNull(beginningOfTheFilename, "beginningOfTheFilename should not be null");
    Objects.requireNonNull(fileExtension, "fileExtension should not be null");

    if (beginningOfTheFilename.isEmpty()) {
      throw new IllegalArgumentException("beginningOfTheFilename should not be empty");
    }
    if (fileExtension.isEmpty()) {
      throw new IllegalArgumentException("fileExtension should not be empty");
    }

    this.beginningOfTheFilename = beginningOfTheFilename;
    this.fileExtension = fileExtension;
    this.logger = logger;
  }

  /**
   * @param line String from the imported file
   * @return true if the line doesn't start with "A* ", "A ", "B* ", "B "...
   */
  private boolean isLineAQuestion(final String line) {
    if (!isLineValid(line)) {
      return false;
    }

    for (var alphabet = 'A'; alphabet <= 'Z'; alphabet++) {
      if (line.startsWith((alphabet) + " ") || line.startsWith((alphabet) + "* ")) {
        return false;
      }
    }
    return true;
  }

  /**
   * @param line input from the text file
   * @return true, if the line is not empty and not starts with #
   */
  private boolean isLineValid(final String line) {
    return (!line.isEmpty() && !line.startsWith("#"));
  }

  /**
   * Reads all files and creates objects for every question and answer
   *
   * @return List of all questions and answers in this directory
   */
  public List<Question> readQuestions() {
    List<Question> questions = new ArrayList<>();
    try (Stream<Path> stream = Files.walk(Paths.get(System.getProperty("user.dir") + "\\src\\"))) {
      List<Path> files =
          stream
              .filter(Files::isRegularFile)
              .filter(p -> p.getFileName().toString().startsWith(beginningOfTheFilename))
              .filter(p -> p.getFileName().toString().endsWith(fileExtension))
              .collect(Collectors.toList());

      for (Path file : files) {
        questions.addAll(readFileLineByLine(file));
      }
    } catch (IOException exception) {
      System.err.format("IOException: %s%n", exception);
    }
    return questions;
  }

  /**
   * Prepare line read. Log exceptions
   *
   * @param file current file to import
   * @return list of imported questions
   */
  private List<Question> readFileLineByLine(final Path file) {
    List<Question> questions = new ArrayList<>();
    try (var br = new BufferedReader(new FileReader(file.toString()))) {
      questions.addAll(processLine(br));
    } catch (IOException e) {
      logger.warning(e.getMessage());
    }
    return questions;
  }

  /**
   * loop through the file line by line and create objects for questions with answers during
   * builder-pattern
   *
   * @param br current file
   * @return list of valid imported questions with possible answers
   * @throws IOException io exceptions
   */
  private List<Question> processLine(final BufferedReader br) throws IOException {
    List<Question> questions = new ArrayList<>();
    var qb = new Question.Builder();
    String s;
    while ((s = br.readLine()) != null) {
      if (isLineAQuestion(s)) {
        if (qb.isQuestionadded()) {
          try {
            questions.add(qb.build());
          } catch (IllegalArgumentException e) {
            logger.warning("Question not imported, " + e.getMessage());
          }
          qb = new Question.Builder();
        }
        qb.question(s);
      } else if (isLineValid(s)) {
        qb.answer(createAnswer(s));
      }
    }
    questions.add(appendLastQuestion(qb));
    return questions;
  }

  /**
   * last questions have to add manually
   *
   * @param qb question.builder
   * @return object of Question or null
   */
  private Question appendLastQuestion(final Question.Builder qb) {
    return (qb.isQuestionadded()) ? qb.build() : null;
  }

  private Answer createAnswer(final String s) {
    var correctAnswer = false;
    String answer;
    if ('*' == s.charAt(1)) {
      correctAnswer = true;
      answer = s.substring(3);
    } else {
      answer = s.substring(2);
    }
    return new Answer(answer, correctAnswer);
  }
}
