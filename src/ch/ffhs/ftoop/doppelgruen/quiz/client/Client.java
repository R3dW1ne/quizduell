package ch.ffhs.ftoop.doppelgruen.quiz.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Objects;
import java.util.logging.Logger;

/** Simulates a client connection instead of a telnet connection. */
public class Client {

  private final String serverName;
  private final int serverPort;
  private final Logger logger;
  private OutputStream serverOut;

  private BufferedReader bufferedIn;
  private Socket socket;

  /**
   * Creates a new client and connect it to the serversocket. Also starts the method
   * readMessageLoop() in a separate thread for listening to the server output.
   *
   * @param serverName Name of the Server
   * @param serverPort Port of the ServerSocket
   * @throws NullPointerException If {@param serverName} is null
   */
  private Client(final String serverName, final int serverPort) {
    this.serverName = serverName;
    this.serverPort = serverPort;
    this.logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
  }

  public static Client createClient(final String serverName, final int serverPort) {
    Objects.requireNonNull(serverName, "serverName should not be null");
    if (serverName.isEmpty()) {
      throw new IllegalArgumentException("serverName should not be empty");
    }

    var client = new Client(serverName, serverPort);

    if (client.connect()) {
      client.startMessageReader();
      return client;
    } else {
      return null;
    }
  }

  private boolean connect() {
    try {
      socket = new Socket(serverName, serverPort);
      this.serverOut = socket.getOutputStream();
      var serverIn = socket.getInputStream();
      this.bufferedIn = new BufferedReader(new InputStreamReader(serverIn));
      return true;
    } catch (IOException e) {
      logger.warning(e.getMessage());
      return false;
    }
  }

  public BufferedReader getBufferedIn() {
    return bufferedIn;
  }

  public Socket getSocket() {
    return socket;
  }

  public void sendMsgToServer(final String aString) throws IOException {
    serverOut.write((aString + System.getProperty("line.separator")).getBytes());
  }

  private void sendMsgToClient(final String aString) {
    System.out.println(aString);
  }

  private void startMessageReader() {
    var t = new Thread(this::readMessageLoop);
    t.start();
  }

  /** Reads messages from the serverSocket in a while loop. */
  private void readMessageLoop() {
    try {
      String line;
      while ((line = bufferedIn.readLine()) != null) {
        sendMsgToClient(line);
      }
    } catch (IOException e) {
      logger.warning(e.getMessage());
    }
  }
}
