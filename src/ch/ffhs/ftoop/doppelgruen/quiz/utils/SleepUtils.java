package ch.ffhs.ftoop.doppelgruen.quiz.utils;


import java.util.concurrent.TimeUnit;

public class SleepUtils {

  private SleepUtils() {
  }

  public static void safeSleep(final TimeUnit timeUnit, final long duration) {
    safeSleep(timeUnit.toMillis(duration));
  }

  public static void safeSleep(final long duration) {
    try {
      Thread.sleep(duration);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

}