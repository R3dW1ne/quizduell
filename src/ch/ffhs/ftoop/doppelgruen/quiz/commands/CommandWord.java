package ch.ffhs.ftoop.doppelgruen.quiz.commands;

/**
 * Representations for all the valid command words for the game along with a string in a particular
 * language.
 */
public enum CommandWord {
  LOGIN("/login", -1, true, false, false),
  QUIT("/quit", -1, true, true, false),
  HELP("/help", -1, true, true, true),
  PLAY("/play", -1, false, true, false),
  WHO("/who", -1, false, true, false),
  KEY_1("1", 1, false, false, true),
  KEY_2("2", 2, false, false, true),
  KEY_3("3", 3, false, false, true),
  STATS("/stats", -1, false, true, false),
  CHAT("/chat", -1, false, true, false),
  UNKNOWN("?", -1, false, false, false);

  private final String commandString;
  private final int commandInt;
  private final boolean validOnStartUp;
  private final boolean validInLobby;
  private final boolean validInGame;

  /**
   * Initialise with the corresponding command string.
   *
   * @param commandString The command string.
   */
  CommandWord(
      final String commandString,
      final int commandInt,
      final boolean validOnStartUp,
      final boolean validInLobby,
      final boolean validInGame) {
    this.commandString = commandString;
    this.commandInt = commandInt;
    this.validOnStartUp = validOnStartUp;
    this.validInLobby = validInLobby;
    this.validInGame = validInGame;
  }

  public boolean isValidOnStartUp() {
    return validOnStartUp;
  }

  public boolean isValidInLobby() {
    return validInLobby;
  }

  public boolean isValidInGame() {
    return validInGame;
  }

  public int toInt() {
    return commandInt;
  }

  /** @return The command word as a string. */
  @Override
  public String toString() {
    return commandString;
  }
}
