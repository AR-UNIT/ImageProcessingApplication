package ime.model;

import java.util.HashMap;
import java.util.Map;

/**
 * This class provides the expected pixels for split test in model, which are stored as
 * constants to be used in model tests.
 */
public class SplitTransformExpectedOutputs {

  public static final PixelInterface[][] EXPECTED_BLUR_PIXELS_WITH_SPLIT_LINE = {
          {new Pixel(125, 125, 125, 255),
          new Pixel(125, 125, 125, 255)},
          {new Pixel(150, 150, 150, 255),
          new Pixel(100, 100, 100, 255)}
  };

  public static final PixelInterface[][] EXPECTED_SHARPEN_PIXELS_WITH_SPLIT_LINE = {
          {new Pixel(106, 106, 106, 255),
          new Pixel(144, 144, 144, 255)},
          {new Pixel(206, 206, 206, 255),
          new Pixel(44, 44, 44, 255)}
  };

  public static final PixelInterface[][] EXPECTED_SEPIA_PIXELS_WITH_SPLIT_LINE = {
          {new Pixel(135, 120, 93, 255),
          new Pixel(202, 180, 140, 255)},
          {new Pixel(255, 240, 187, 255),
          new Pixel(67, 60, 46, 255)}
  };

  public static final PixelInterface[][] EXPECTED_LUMA_COMPONENT_PIXELS_WITH_SPLIT_LINE = {
          {new Pixel(100, 100, 100, 255),
          new Pixel(150, 150, 150, 255)},
          {new Pixel(200, 200, 200, 255),
          new Pixel(50, 50, 50, 255)}
  };

  public static final PixelInterface[][] EXPECTED_COLOR_CORRECT_PIXELS_WITH_SPLIT_LINE = {
          {new Pixel(100, 100, 100, 255),
          new Pixel(150, 150, 150, 255)},
          {new Pixel(200, 200, 200, 255),
          new Pixel(50, 50, 50, 255)}
  };

  public static final PixelInterface[][] EXPECTED_LEVELS_ADJUST_PIXELS_WITH_SPLIT_LINE = {
          {new Pixel(92, 92, 92, 255),
          new Pixel(135, 135, 135, 255)},
          {new Pixel(165, 165, 165, 255),
          new Pixel(38, 38, 38, 255)}
  };

  /**
   * Function to fetch all the expected pixels of images with a split line in them.
   * @return  map of string to 2d pixel arrays
   */
  public static Map<String, PixelInterface[][]> getExpectedPixelsWithSplitLine() {
    Map<String, PixelInterface[][]> operationToOutputImages;
    operationToOutputImages = new HashMap<>();
    operationToOutputImages.put("blur", EXPECTED_BLUR_PIXELS_WITH_SPLIT_LINE);
    operationToOutputImages.put("sharpen", EXPECTED_SHARPEN_PIXELS_WITH_SPLIT_LINE);
    operationToOutputImages.put("sepia", EXPECTED_SEPIA_PIXELS_WITH_SPLIT_LINE);
    operationToOutputImages.put("luma-component", EXPECTED_LUMA_COMPONENT_PIXELS_WITH_SPLIT_LINE);
    operationToOutputImages.put("color-correct", EXPECTED_COLOR_CORRECT_PIXELS_WITH_SPLIT_LINE);
    operationToOutputImages.put("levels-adjust", EXPECTED_LEVELS_ADJUST_PIXELS_WITH_SPLIT_LINE);
    return operationToOutputImages;
  }


}
