package ch.ffhs.ftoop.doppelgruen.quiz.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ClientMain {
  private static final String SERVER_NAME = "localhost"; // "quizduellvonrobertund.dechris.ch"
  private static final int SERVER_PORT = 60000;

  public static void main(final String[] args) throws IOException {
    var client = Client.createClient(SERVER_NAME, SERVER_PORT);

    if (client == null) {
      System.out.println("No client object created");
    } else {
      var reader = new BufferedReader(new InputStreamReader(System.in));
      String lineToServer;

      while ((lineToServer = reader.readLine()) != null) {
        client.sendMsgToServer(lineToServer);
      }
    }
  }
}
