package ch.ffhs.ftoop.doppelgruen.quiz.commands;

import java.util.Objects;

/**
 * This class holds information about a command that was issued by the user. A command currently
 * consists of two parts: a CommandWord and a string (for example, if the command was "login hans",
 * then the two parts are LOGIN and HANS).
 *
 * <p>The way this is used is: Commands are already checked for being valid command words. If the
 * user entered an invalid command (a word that is not known) then the CommandWord is UNKNOWN.
 *
 * <p>If the command had only one word, then the second word is <null>.
 */
public class Command {

  private final CommandWord commandWord;
  private final String secondWord;

  /**
   * Create a command object. First and second words must be supplied, but the second may be null.
   *
   * @param commandWord The CommandWord. UNKNOWN if the command word was not recognised.
   * @param secondWord The second word of the command. May be null.
   * @throws NullPointerException If {@param commandWord} is null
   */
  public Command(final CommandWord commandWord, final String secondWord) {
    Objects.requireNonNull(commandWord, "commandWord should not be null");

    this.commandWord = commandWord;
    this.secondWord = secondWord;
  }

  /** @return The command word (the first word) of this command. */
  public CommandWord getCommandWord() {
    return commandWord;
  }

  public int getCommandInt() {
    return commandWord.toInt();
  }

  /** @return The second word of this command. Returns null if there was no second word. */
  public String getSecondWord() {
    return secondWord;
  }

  /** @return true if the command has a second word. */
  public boolean hasSecondWord() {
    return (secondWord != null);
  }
}
