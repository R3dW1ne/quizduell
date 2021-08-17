package ch.ffhs.ftoop.doppelgruen.quiz.game;

public class Stats {

  private int won;
  private int drawn;
  private int lost;

  public Stats() {
    won = 0;
    drawn = 0;
    lost = 0;
  }

  public void incrementWon() {
    won++;
  }

  public void incrementDrawn() {
    drawn++;
  }

  public void incrementLost() {
    lost++;
  }

  public int getWon() {
    return won;
  }

  public int getDrawn() {
    return drawn;
  }

  public int getLost() {
    return lost;
  }
}
