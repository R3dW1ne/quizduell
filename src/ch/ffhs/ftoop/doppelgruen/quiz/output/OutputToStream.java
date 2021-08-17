package ch.ffhs.ftoop.doppelgruen.quiz.output;

import ch.ffhs.ftoop.doppelgruen.quiz.commands.CommandWord;
import ch.ffhs.ftoop.doppelgruen.quiz.game.Player;
import ch.ffhs.ftoop.doppelgruen.quiz.game.Stats;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class OutputToStream implements IOutput {

  private static final String STRING_NULL = "aString should not be null";
  private static final String STRING_EMPTY = "aString should not be empty";
  private static final String PLAYER_NULL = "aPlayer should not be null";
  private final OutputStream outputStream;
  private final String newLine = System.getProperty("line.separator");

  public OutputToStream(final OutputStream outputStream) {
    Objects.requireNonNull(outputStream, "outputStream should not be null");

    this.outputStream = outputStream;
  }

  @Override
  public void outputHeadline() throws IOException {

    sendToStream(
        ("   _____        .__   __  .__       .__                                        ________"
            + newLine
            + "  /     \\  __ __|  |_/  |_|__|_____ |  | _____  ___.__. ___________           /  _____/_____    _____   ____"
            + newLine
            + " /  \\ /  \\|  |  \\  |\\   __\\  \\____ \\|  | \\__  \\<   |  |/ __ \\_  __ \\  ______ /   \\  ___\\__  \\  /     \\_/ __ \\"
            + newLine
            + "/    Y    \\  |  /  |_|  | |  |  |_> >  |__/ __ \\\\___  \\  ___/|  | \\/ /_____/ \\    \\_\\  \\/ __ \\|  Y Y  \\  ___/"
            + newLine
            + "\\____|__  /____/|____/__| |__|   __/|____(____  / ____|\\___  >__|             \\______  (____  /__|_|  /\\___  >"
            + newLine
            + "        \\/                   |__|             \\/\\/         \\/                        \\/     \\/      \\/     \\/"
            + newLine
            + ColorCommandLine.colorfulLetters(ColorCommandLine.GREEN,
            "                                                                                               by double green")
            + newLine + newLine));
  }

  @Override
  public void welcomeMessage() throws IOException {
    sendToStream(
        ("Welcome to the Multiplayer-Quiz!" + newLine
            + "Tell us your name to start playing." + newLine
            + "Type \"" + CommandWord.LOGIN + " \" and your name." + newLine));
  }

  @Override
  public void loginError() throws IOException {
    sendToStream(ColorCommandLine.colorfulLetters(ColorCommandLine.RED,
        "Login error. Name already in use. Please try another name!" + newLine));
  }

  @Override
  public void loginErrorNameMissing() throws IOException {
    sendToStream(ColorCommandLine.colorfulLetters(ColorCommandLine.RED,
        "Login error. Please type in your name!" + newLine));
  }

  @Override
  public void loginSuccessful() throws IOException {
    sendToStream(ColorCommandLine.colorfulLetters(ColorCommandLine.GREEN,
        "Login successful!" + newLine));
  }

  @Override
  public void goodbyeMessage() throws IOException {
    sendToStream(ColorCommandLine.colorfulLetters(ColorCommandLine.YELLOW,
        "Thank you for playing. You can close the window. Good bye." + newLine));
  }

  @Override
  public void help(final String aString) throws IOException {
    Objects.requireNonNull(aString, STRING_NULL);
    if (aString.isEmpty()) {
      throw new IllegalArgumentException(STRING_EMPTY);
    }

    sendToStream(ColorCommandLine.colorfulLetters(ColorCommandLine.YELLOW,
        "Your command words are: " + newLine
            + aString + newLine));
  }

  @Override
  public void unknownCommand() throws IOException {
    sendToStream(ColorCommandLine.colorfulLetters(ColorCommandLine.YELLOW,
        "Wrong command.. Type \"" + CommandWord.HELP + "\" if you need some" + newLine));
  }

  @Override
  public void saying(final String aString) throws IOException {
    Objects.requireNonNull(aString, STRING_NULL);
    if (aString.isEmpty()) {
      throw new IllegalArgumentException(STRING_EMPTY);
    }

    sendToStream(ColorCommandLine.colorfulLetters(ColorCommandLine.CYAN,
        aString + newLine));
  }

  @Override
  public void findingOpponent() throws IOException {
    sendToStream(ColorCommandLine.colorfulLetters(ColorCommandLine.CYAN,
        "Finding Opponent" + newLine));
  }

  @Override
  public void currentAmountOfPlayersOnline(final int anInt) throws IOException {
    if (anInt <= 0) {
      throw new IllegalArgumentException("anInt should not <= 0");
    }

    sendToStream(ColorCommandLine.colorfulLetters(ColorCommandLine.CYAN, "Players online: " +
        anInt + newLine));
  }


  @Override
  public void questionWithPossibleAnswers(final String aString) throws IOException {
    Objects.requireNonNull(aString, STRING_NULL);
    if (aString.isEmpty()) {
      throw new IllegalArgumentException(STRING_EMPTY);
    }

    sendToStream(ColorCommandLine.colorfulLetters(ColorCommandLine.GREEN,
        aString + newLine));
  }

  @Override
  public void currentPlayersOnline(final Player aPlayer) throws IOException {
    Objects.requireNonNull(aPlayer, PLAYER_NULL);

    var color = switch (aPlayer.getLocation()) {
      case LOOKING_FOR_GAME -> ColorCommandLine.CYAN;
      case INGAME -> ColorCommandLine.GREEN;
      default -> ColorCommandLine.WHITE;
    };

    sendToStream((aPlayer.getName() + " (" + ColorCommandLine
        .colorfulLetters(color, aPlayer.getLocation().toString()) + ")" + newLine));
  }

  @Override
  public void stats(final Stats stats) throws IOException {
    Objects.requireNonNull(stats, "stats should not be null");

    sendToStream(ColorCommandLine.colorfulLetters(ColorCommandLine.CYAN,
        "Overall Game Stats:" + newLine +
            stats.getWon() + " game(s) won" + newLine +
            stats.getDrawn() + " game(s) drawn" + newLine +
            stats.getLost() + " game(s) lost") + newLine);
  }

  @Override
  public void quizWon(final Player aPlayer) throws IOException {
    if (aPlayer != null) {
      sendToStream(ColorCommandLine.colorfulLetters(ColorCommandLine.YELLOW,
          aPlayer.getName() + " won the game!" + newLine));
    } else {
      sendToStream(ColorCommandLine.colorfulLetters(ColorCommandLine.YELLOW,
          "Draw, nobody won!" + newLine));
    }

  }

  @Override
  public void chat(final Player aPlayer, final String chatMessage) throws IOException {
    Objects.requireNonNull(aPlayer, PLAYER_NULL);
    Objects.requireNonNull(chatMessage, "chatMessage should not be null");

    if (chatMessage.isEmpty()) {
      throw new IllegalArgumentException(STRING_EMPTY);
    }

    sendToStream(aPlayer.getName() + ": " + chatMessage + newLine);
  }

  @Override
  public void loginErrorNameNotValid() throws IOException {
    sendToStream(ColorCommandLine.colorfulLetters(ColorCommandLine.RED,
        "Login error. Name not valid. Names are not allowed to start with space and have to be between 3 and 20 chars long."
            + newLine));
  }


  @Override
  public void quizStart() throws IOException {
    sendToStream(ColorCommandLine.colorfulLetters(ColorCommandLine.YELLOW,
        "Game starts in 5 Seconds! Prepare yourself!" + newLine
            + "Use " + CommandWord.KEY_1 + ", " + CommandWord.KEY_2 + " or "
            + CommandWord.KEY_3 + " followed by enter to give an answers." + newLine));
  }

  @Override
  public void quizEnd() throws IOException {
    sendToStream(ColorCommandLine.colorfulLetters(ColorCommandLine.YELLOW,
        "The Quiz is over. You are back in the Lobby." + newLine));
  }

  @Override
  public void rightAnswer() throws IOException {
    sendToStream(ColorCommandLine.colorfulLetters(ColorCommandLine.CYAN,
        "Great! Your answer was right. One Point for you!" + newLine));
  }

  @Override
  public void wrongAnswer() throws IOException {
    sendToStream(ColorCommandLine.colorfulLetters(ColorCommandLine.CYAN,
        "You know nothing, Jon Snow! More luck next question!" + newLine));
  }

  @Override
  public void fasterAnswer() throws IOException {
    sendToStream(ColorCommandLine.colorfulLetters(ColorCommandLine.CYAN,
        "What a pity! Your opponent gave the correct answer faster, no points for you!" + newLine));
  }

  @Override
  public void justOneAnswer() throws IOException {
    sendToStream(ColorCommandLine.colorfulLetters(ColorCommandLine.CYAN,
        "You already had your chance. Only one answer per question allowed!" + newLine));
  }

  @Override
  public void alreadyGaveFastestCorrectAnswer() throws IOException {
    sendToStream(ColorCommandLine.colorfulLetters(ColorCommandLine.CYAN,
        "You already gave the correct answer or do you also draw a card at 21 in black jack!?"
            + newLine));
  }

  @Override
  public void noAnswer(final int correctAnswer) throws IOException {
    sendToStream(ColorCommandLine.colorfulLetters(ColorCommandLine.CYAN,
        "No correct answers, no points. The correct answer was: " + correctAnswer + newLine));
  }

  @Override
  public void pointForOpponent(final Player player, final int answer) throws IOException {
    sendToStream(ColorCommandLine.colorfulLetters(ColorCommandLine.CYAN,
        player.getName() + " gave the correct answer: " + answer + newLine));
  }

  @Override
  public void quizResultHeadline() throws IOException {
    sendToStream(ColorCommandLine.colorfulLetters(ColorCommandLine.YELLOW,
        "The result:" + newLine));
  }

  @Override
  public void quizResult(final Player aPlayer, final int points) throws IOException {
    Objects.requireNonNull(aPlayer, PLAYER_NULL);

    sendToStream(ColorCommandLine.colorfulLetters(ColorCommandLine.YELLOW,
        aPlayer.getName() + ": " + points + newLine));
  }

  @Override
  public void timeout() throws IOException {
    var dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    var now = LocalDateTime.now();
    sendToStream(ColorCommandLine.colorfulLetters(ColorCommandLine.YELLOW,
            "Timeout at " + dtf.format(now) + "!" + newLine));
  }


  private void sendToStream(final String aString) throws IOException {
    Objects.requireNonNull(aString, STRING_NULL);
    if (aString.isEmpty()) {
      throw new IllegalArgumentException(STRING_EMPTY);
    }
    outputStream.write(aString.getBytes());
  }


}
