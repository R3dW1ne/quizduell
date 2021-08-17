package ch.ffhs.ftoop.doppelgruen.quiz.game;

import ch.ffhs.ftoop.doppelgruen.quiz.commands.Command;
import ch.ffhs.ftoop.doppelgruen.quiz.commands.Parser;
import ch.ffhs.ftoop.doppelgruen.quiz.output.IOutput;
import ch.ffhs.ftoop.doppelgruen.quiz.output.OutputToStream;
import ch.ffhs.ftoop.doppelgruen.quiz.output.SayingList;
import ch.ffhs.ftoop.doppelgruen.quiz.utils.SleepUtils;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * An instance of this class represents a client
 */
public class ServerWorker implements Runnable {

  private static final String COMMAND_EXCEPTION = "command should not be null";

  private final Socket clientSocket;
  private final Logger logger;
  private final Players players;
  private final Server server;
  private IOutput output;
  private Player player;
  private Parser parser;

  /**
   * @param server       Reference to the responsible server
   * @param clientSocket Used clientSocket for client/server communication
   * @throws NullPointerException If {@param server} is null
   * @throws NullPointerException If {@param clientSocket} is null
   */
  public ServerWorker(final Server server, final Socket clientSocket) {
    Objects.requireNonNull(server, "text should not be null");
    Objects.requireNonNull(clientSocket, "text should not be null");

    this.logger = server.getLogger();
    this.players = server.getPlayerController();
    this.clientSocket = clientSocket;
    this.server = server;

    try {
      this.output = new OutputToStream(clientSocket.getOutputStream());
    } catch (IOException e) {
      logger.log(
          Level.WARNING,
          "InterruptedException was thrown in the Thread of ServerWorker from {0}",
          clientSocket);
      logger.log(Level.WARNING, e.getMessage());
      Thread.currentThread().interrupt();
    }
  }

  public IOutput getOutput() {
    return output;
  }

  public Player getPlayer() { return player; }

  public Parser getParser() {
    return parser;
  }

  /**
   * The "main" while for the client. First, the command will be parsed. Second, the command will be
   * executed.
   */
  @Override
  public void run() {
    try {
      clientSocket.setSoTimeout(600000);
      this.parser = new Parser(clientSocket.getInputStream());

      output.outputHeadline();
      output.welcomeMessage();

      var finished = false;
      while (!finished) {
        var command = parser.getCommand((player == null) ? null : player.getLocation());
        finished = processCommand(command);
      }
      output.goodbyeMessage();
    } catch (SocketTimeoutException e){
      try {
        output.timeout();
        if (player != null) {
          players.deletePlayer(player);
        }
        clientSocket.close();
      } catch (IOException ioException) {
        logger.warning(ioException.getMessage());
      }
    } catch (IOException e) {
      logger.warning(e.getMessage());
    }
   }

  /**
   * Given a command, process (that is: execute) the command.
   *
   * @param command The command to be processed.
   * @return true if the command ends the game, false otherwise.
   * @throws NullPointerException If {@param command} is null
   */
  private boolean processCommand(final Command command) throws IOException {
    Objects.requireNonNull(command, COMMAND_EXCEPTION);

    var wantToQuit = false;

    var commandWord = command.getCommandWord();

    switch (commandWord) {
      case UNKNOWN -> output.unknownCommand();
      case HELP -> output.help(parser.getAllCommandsAsString());
      case PLAY -> handlePlay();
      case LOGIN -> handleLogin(command);
      case WHO -> handleWho();
      case STATS -> handleStats();
      case CHAT -> handleChat(command);
      case KEY_1, KEY_2, KEY_3 -> handleAnswer(command);
      case QUIT -> {
        wantToQuit = true;
        handleQuit();
      }
      default -> System.err
          .println("Unexpected command behavior. Default case should never be called");
    }
    return wantToQuit;
  }

  private void handleQuit(){
    if (player != null) {
      if (!players.deletePlayer(player)) {
        logger.log(Level.WARNING, "Quitting failed for {0}", player.getName());
      }
      logger.log(Level.INFO, "removed  {0}", getPlayer().getName());
    }
  }

  /**
   * Start searching for an opponent. While loop runs until an opponent have been found by the
   * LookingForGame class which updated the player location to ingame.
   *
   * @throws IOException Exception
   */
  private void handlePlay() throws IOException {
    var gameFound = false;
    player.setLocation(Location.LOOKING_FOR_GAME);
    if (!server.getGameMatcher().isMatchMakingActive()){
      server.getGameMatcher().activateMatchMaking();
      new Thread(server.getGameMatcher()).start();
    }
    parser.switchToLookingForGameCommands();
    output.findingOpponent();

    SleepUtils.safeSleep(TimeUnit.SECONDS, 3);

    while (!gameFound) {
      if (player.getLocation() == Location.INGAME) {
        gameFound = true;
      } else {
        output.saying(SayingList.getRandomString());
      }
      SleepUtils.safeSleep(TimeUnit.SECONDS, 3);
    }
  }

  private void handleAnswer(final Command command) throws IOException {
    Objects.requireNonNull(command, COMMAND_EXCEPTION);

    player.getCurrentQuizSession().checkAnswer(player, command.getCommandInt());
  }

  private void handleChat(final Command command) throws IOException {
    Objects.requireNonNull(command, COMMAND_EXCEPTION);

    if (command.getSecondWord() != null){
      for (Player aPlayer : players.getPlayersIn(Location.LOBBY)) {
        aPlayer.getWorker().output.chat(player, command.getSecondWord());
      }
    }
  }

  private void handleWho() throws IOException {
    for (Player aPlayer : players.getAllPlayers()) {
      output.currentPlayersOnline(aPlayer);
    }
  }

  private void handleStats() throws IOException {
    output.stats(player.getStats());
  }

  private void handleLogin(final Command command) throws IOException {
    Objects.requireNonNull(command, COMMAND_EXCEPTION);

    // if there is no second word, we don't know what his name is
    if (!command.hasSecondWord()) {
      output.loginErrorNameMissing();
      logger.log(Level.WARNING, "Login failed. No name");
      return;
    }

    // if the second word is not valid, show error message
    if (command.getSecondWord().startsWith(" ") || command.getSecondWord().length() < 3
        || command.getSecondWord().length() > 20) {
      output.loginErrorNameNotValid();
      logger.log(Level.WARNING, "Login failed. Name not valid {0}", command.getSecondWord());
      return;
    }

    player = players.createPlayerIfPossible(command.getSecondWord(), this);
    if (player == null) {
      output.loginError();
      logger.log(Level.WARNING, "Login failed for {0}", command.getSecondWord());
    } else {
      output.loginSuccessful();
      output.currentAmountOfPlayersOnline(players.getAmountOfAllPlayers());
      parser.switchToLobbyCommands();
    }
  }
}
