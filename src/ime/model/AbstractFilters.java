package ime.model;

import java.util.List;

import static java.lang.Integer.parseInt;

/**
 * AbstractFilters is an abstract base class that implements the Filters interface.
 * It provides a default implementation for the execute method and a utility method
 * for generating histograms of images.
 *
 * <p>This class serves as a foundation for specific filter implementations,
 * providing common functionality that can be inherited by subclasses.</p>
 */
public class AbstractFilters implements Filters {
  public void execute(List<String> commandTokens) throws IllegalArgumentException {
    throw new UnsupportedOperationException(commandTokens.get(0) + " is not supported yet!");
  }

  /**
   * Generates a histogram for the red, green, and blue channels of an image.
   * Each channel's histogram is represented as an array of 256 integers, where each index
   * corresponds to a pixel intensity value (0-255), and the value at that index represents
   * the frequency of pixels with that intensity.
   *
   * @param image the Image object from which to generate the histograms
   * @return a 2D array where each row represents a color channel (red, green, blue)
   */
  protected int[][] generateHistogram(ImageInterface image) {
    int width = image.getWidth();
    int height = image.getHeight();
    int[][] histogram = new int[3][256]; // 3 channels, 256 possible values each

    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        PixelInterface pixel = image.getPixel(y, x); // Swapped x and y
        histogram[0][pixel.getR()]++; // Red channel
        histogram[1][pixel.getG()]++; // Green channel
        histogram[2][pixel.getB()]++; // Blue channel
      }
    }

    return histogram;
  }

  /**
   * Helper function used by filters which support split percentage to extract split percentage
   * from commandTokens.
   *
   * @param commandTokens input command string tokens, used to drive model
   * @return integer valued split percentage
   * @throws IllegalArgumentException if split percent is not within 0-100(inclusive)
   */
  protected int extractSplitPercent(List<String> commandTokens) throws IllegalArgumentException {
    int splitPercent = 100;
    if (commandTokens.size() > 3 && commandTokens.get(3).equals("split")) {
      splitPercent = parseInt(commandTokens.get(4));
    } else if (commandTokens.size() > 6 && commandTokens.get(6).equals("split")) {
      splitPercent = parseInt(commandTokens.get(7));
    }

    if (splitPercent > 100 || splitPercent < 0) {
      throw new IllegalArgumentException("Split percent must be between 0 and 100");
    }
    return splitPercent;
  }


}
