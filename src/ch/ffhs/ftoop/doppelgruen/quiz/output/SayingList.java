package ch.ffhs.ftoop.doppelgruen.quiz.output;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SayingList {

  private static final String OPPONENT_FOUND = "Worthy opponent found!";

  private static final List<String> sayings = new ArrayList<>();
  private static final Random rnd = new Random();

  private SayingList(){}

  static {
    sayings.add("Easy Pickings");
    sayings.add("Your Worst Nightmare");
    sayings.add("World class paste eater");
    sayings.add("Gourd critic");
    sayings.add("Nose and mouth breather");
    sayings.add("The Worst Player");
    sayings.add("Who Wants to Be a Millionaire Expert");
    sayings.add("Your old Roommate");
    sayings.add("Fiscally responsible mime");
    sayings.add("Your Guild Leader");
    sayings.add("Cheater McCheaterson");
    sayings.add("Gum Pusher");
    sayings.add("Really slow guy");
    sayings.add("Roach Boy");
    sayings.add("Coffee Addict");
    sayings.add("Inward Talker");
    sayings.add("Double Green Developer");
    sayings.add("Grand Master");
    sayings.add("A Little Puppy");
    sayings.add("Guenther Jauch");
  }

  public static String getRandomString() {
    return sayings.get(rnd.nextInt(sayings.size()));
  }

  public static String getOpponentFound() {
    return OPPONENT_FOUND;
  }
}
