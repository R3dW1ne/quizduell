package ch.ffhs.ftoop.doppelgruen.quiz.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * An instance of this class stores an reference to every login in player. It is responsible that
 * every player name is unique
 */
public class Players {

  private final List<Player> playerList;
  private final Set<String> uniquePlayerNamesOfLoginPlayers;

  Players() {
    playerList = new CopyOnWriteArrayList<>();
    uniquePlayerNamesOfLoginPlayers = new CopyOnWriteArraySet<>();
  }

  /**
   * Creates a player object, if the name is not already in use.
   *
   * @param name unique name for a player object
   * @param worker Responsible server worker for the player
   * @return Returns a player object if the creation was successful. null if it was not successful.
   * @throws NullPointerException If {@param name} is null
   * @throws NullPointerException If {@param worker} is null
   * @throws IllegalArgumentException If {@param name} is empty
   */
  public Player createPlayerIfPossible(final String name, final ServerWorker worker) {
    Objects.requireNonNull(name, "name should not be null");
    Objects.requireNonNull(worker, "worker should not be null");
    if (name.isEmpty()) {
      throw new IllegalArgumentException("name should not be empty");
    }

    if (uniquePlayerNamesOfLoginPlayers.add(name)) {
      var player = new Player(name, worker);
      playerList.add(player);
      return player;
    } else {
      return null;
    }
  }

  public List<Player> getAllPlayers() {
    return playerList;
  }

  /**
   * @param location Current Location of a player
   * @return A List of all players in the given location
   * @throws NullPointerException If {@param location} is null
   */
  public List<Player> getPlayersIn(final Location location) {
    Objects.requireNonNull(location, "location should not be null");

    List<Player> playersInLocation = new ArrayList<>();
    for (Player player : playerList) {
      if (player.getLocation() == location) {
        playersInLocation.add(player);
      }
    }
    return playersInLocation;
  }

  public int getAmountOfAllPlayers() {
    return playerList.size();
  }

  /**
   * @param player Valid instance of a player
   * @return true if the removal was successful
   * @throws NullPointerException If {@param player} is null
   */
  public boolean deletePlayer(final Player player) {
    Objects.requireNonNull(player, "player should not be null");
    return (uniquePlayerNamesOfLoginPlayers.remove(player.getName()) && playerList.remove(player));
  }
}
