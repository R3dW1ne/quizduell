package ch.ffhs.ftoop.doppelgruen.quiz.game;

public class ServerMain {
  private static final int SERVER_PORT = 60000;

  public static void main(final String[] args) {

    var serverThread = new Thread(new Server(SERVER_PORT));
    serverThread.start();
    // create connection via telnet - shortcut win + r and type "telnet localhost 60000"
    // Or execute class ClientMain
  }
}
