package ch.ffhs.ftoop.doppelgruen.quiz.output;


import java.util.Objects;

@SuppressWarnings("unused")
public class ColorCommandLine {

  public static final String RESET = "\u001B[0m";
  public static final String BLACK = "\u001B[30m";
  public static final String RED = "\u001B[31m";
  public static final String GREEN = "\u001B[32m";
  public static final String YELLOW = "\u001B[33m";
  public static final String BLUE = "\u001B[34m";
  public static final String PURPLE = "\u001B[35m";
  public static final String CYAN = "\u001B[36m";
  public static final String WHITE = "\u001B[37m";
  public static final String BLACK_BACKGROUND = "\u001B[40m";
  public static final String RED_BACKGROUND = "\u001B[41m";
  public static final String GREEN_BACKGROUND = "\u001B[42m";
  public static final String YELLOW_BACKGROUND = "\u001B[43m";
  public static final String BLUE_BACKGROUND = "\u001B[44m";
  public static final String PURPLE_BACKGROUND = "\u001B[45m";
  public static final String CYAN_BACKGROUND = "\u001B[46m";
  public static final String WHITE_BACKGROUND = "\u001B[47m";

  private static final String EXCEPTION_COLOR = "color should not be null";
  private static final String EXCEPTION_BACKGROUND = "background should not be null";
  private static final String EXCEPTION_LETTERS = "letters should not be null";

  private ColorCommandLine() {
  }

  public static String colorfulLetters(final String color, final String letters) {
    Objects.requireNonNull(color, EXCEPTION_COLOR);
    Objects.requireNonNull(letters, EXCEPTION_LETTERS);

    return color + letters + RESET;
  }

  public static String colorfulBackground(final String background, final String letters) {
    Objects.requireNonNull(background, EXCEPTION_BACKGROUND);
    Objects.requireNonNull(letters, EXCEPTION_LETTERS);

    return background + letters + RESET;
  }

  public static String colorfulBackgroundAndLetters(final String background, final String color,
      final String letters) {
    Objects.requireNonNull(background, EXCEPTION_BACKGROUND);
    Objects.requireNonNull(color, EXCEPTION_COLOR);
    Objects.requireNonNull(letters, EXCEPTION_LETTERS);

    return background + color + letters + RESET;
  }

}
