package ch.ffhs.ftoop.doppelgruen.quiz.game;

import ch.ffhs.ftoop.doppelgruen.quiz.answer_question_handling.QuestionList;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Server runs in a separate thread for accepting client sockets. */
public class Server implements Runnable {

  private final int serverPort;
  private final Logger logger;
  private final Players players;
  private final QuestionList questionList;
  private final LookingForGame gameMatcher;

  /**
   * Creates a new server. Create instances for some classes that needs only once like Logger,
   * PlayerController, SayingList, QuestionList and LookingForGame.
   *
   * @param serverPort Port of the ServerSocket
   * @throws IllegalArgumentException If 49152 > {@param serverPort} > 65535
   */
  public Server(final int serverPort) {
    if (serverPort < 49152 || serverPort > 65535) {
      throw new IllegalArgumentException("serverPort should not be between 49152 and 65535");
    }

    this.serverPort = serverPort;
    this.logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    this.players = new Players();
    this.questionList = new QuestionList(logger);

    gameMatcher = new LookingForGame(this);
  }

  public QuestionList getQuestionList() {
    return questionList;
  }

  public Logger getLogger() {
    return logger;
  }

  public Players getPlayerController() {
    return players;
  }

  public LookingForGame getGameMatcher() {
    return gameMatcher;
  }

  /**
   * Accept new client connections. Create a worker thread for every new client and starts the
   * thread.
   */
  @Override
  public void run() {
    Socket clientSocket = null;
    try (var serverSocket = new ServerSocket(serverPort)) {
      while (!Thread.currentThread().isInterrupted()) {
        logger.log(Level.INFO, "Ready to accept next client connection...");
        clientSocket = serverSocket.accept();
        logger.log(Level.INFO, "Accepted connection from: {0}", clientSocket);

        var worker = new ServerWorker(this, clientSocket);
        var workerThread = new Thread(worker);
        workerThread.start();
      }
    } catch (IOException e) {
      logger.warning(e.getMessage());
    }
  }
}
