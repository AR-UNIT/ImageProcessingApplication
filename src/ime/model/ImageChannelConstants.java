package ime.model;

import java.util.Arrays;
import java.util.List;

/**
 * The ImageChannelConstants class defines constant values representing the different
 * color channels (red, green, blue) and the alpha (transparency) channel in an image.
 * These constants are used throughout the application to reference specific image channels.
 */
public class ImageChannelConstants {

  /**
   * Constant representing the red color channel.
   */
  public final static String RED = "red";

  /**
   * Constant representing the green color channel.
   */
  public final static String GREEN = "green";

  /**
   * Constant representing the blue color channel.
   */
  public final static String BLUE = "blue";

  /**
   * Constant representing the alpha (transparency) channel.
   */
  public final static String ALPHA = "alpha";

  /**
   * Function to get all channels of an image, to iterate over and perform operations.
   *
   * @return array list containing
   */
  public static List<String> getAllChannels() {
    return Arrays.asList(RED, GREEN, BLUE, ALPHA);
  }

}