package ch.ffhs.ftoop.doppelgruen.quiz.commands;

import java.util.HashMap;
import java.util.Objects;

/**
 * This class holds an enumeration of all command words known to the game. It is used to recognise
 * commands as they are typed in.
 */
public class CommandWords {

  private final HashMap<String, CommandWord> validCommands;

  public CommandWords() {
    validCommands = new HashMap<>();
    for (CommandWord command : CommandWord.values()) {
      if (command.isValidOnStartUp()) {
        validCommands.put(command.toString(), command);
      }
    }
  }

  public void removeAllCommands() {
    validCommands.clear();
  }

  /**
   * Add a new command to the valid command list
   *
   * @param command The word to add
   * @throws NullPointerException If {@param command} is null
   */
  public void addCommand(final CommandWord command) {
    Objects.requireNonNull(command, "command should not be null");

    validCommands.put(command.toString(), command);
  }

  /**
   * Find the CommandWord associated with a command word.
   *
   * @param commandWord The word to look up.
   * @return The CommandWord corresponding to commandWord, or UNKNOWN if it is not a valid command
   * @throws NullPointerException If {@param commandWord} is null
   */
  public CommandWord getCommandWord(final String commandWord) {
    Objects.requireNonNull(commandWord, "commandWord should not be null");

    CommandWord command = validCommands.get(commandWord.toLowerCase());
    return Objects.requireNonNullElse(command, CommandWord.UNKNOWN);
  }

  /** All valid commands as a string */
  @Override
  public String toString() {
    var sb = new StringBuilder();
    for (String command : validCommands.keySet()) {
      sb.append(command).append("  ");
    }
    return sb.toString();
  }
}
