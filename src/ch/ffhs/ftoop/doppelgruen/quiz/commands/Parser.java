package ch.ffhs.ftoop.doppelgruen.quiz.commands;

import ch.ffhs.ftoop.doppelgruen.quiz.game.Location;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

/**
 * This parser reads user input and tries to interpret it as an command. Every time it is called it
 * reads a line from the terminal and tries to interpret the line as a two-word command. It returns
 * the command as an object of class Command.
 *
 * <p>The parser has a set of known command words. It checks user input against the known command,
 * and if the input is not one of the known commands, it returns a command object that is marked as
 * an unknown command.
 */
public class Parser {

  private final CommandWords commands;
  private final BufferedReader reader;

  /**
   * Creates a new parser
   *
   * @param inputStream An instance of the users input stream
   * @throws NullPointerException If {@param inputStream} is null
   */
  public Parser(final InputStream inputStream) {
    Objects.requireNonNull(inputStream, "inputStream should not be null");

    commands = new CommandWords();
    reader = new BufferedReader(new InputStreamReader(inputStream));
  }

  public void switchToLookingForGameCommands() {
    commands.removeAllCommands();
  }

  public void switchToInGameCommands() {
    commands.removeAllCommands();
    for (CommandWord command : CommandWord.values()) {
      if (command.isValidInGame()) {
        commands.addCommand((command));
      }
    }
  }

  public void switchToLobbyCommands() {
    commands.removeAllCommands();
    for (CommandWord command : CommandWord.values()) {
      if (command.isValidInLobby()) {
        commands.addCommand((command));
      }
    }
  }

  /** @return The next command from the user. */
  public Command getCommand(final Location playerLocation) throws IOException {
    String inputLine;
    String word1;

    inputLine = reader.readLine();

    if (inputLine == null) {
      return null;
    } else {
      String[] tokenizer = inputLine.split(" ", 2);
      word1 = tokenizer[0];

      var sb = new StringBuilder();
      // Exception for chatting without using command word in Lobby
      if (playerLocation == Location.LOBBY && !word1.startsWith("/")) {
        sb.append(word1);
        word1 = "/chat";
      }
      if (tokenizer.length == 2) {
        if (sb.length() > 0) {
          sb.append(" ");
        }
        sb.append(tokenizer[1]);
      }
      return new Command(
          commands.getCommandWord(word1), (sb.toString().equals("")) ? null : sb.toString());
    }
  }

  /** Print out a list of valid command words. */
  public String getAllCommandsAsString() {
    return commands.toString();
  }
}
