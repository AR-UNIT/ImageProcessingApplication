package ime.controller.gui;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

/**
 * The {@code GUIFeaturesInterface} defines the set of functionalities
 * that the GUI controller provides to manage interactions between the graphical
 * user interface and the underlying model in the image processing application.
 */
public interface GUIFeaturesInterface {

  /**
   * Adjusts the brightness of the current image based on user input.
   * The user specifies the intensity of the adjustment (-255 to 255).
   */
  void brighten();

  /**
   * Compresses the current image based on a user-defined compression ratio.
   * The user specifies the compression ratio (0-100).
   */
  void compress();

  /**
   * Flips the current image horizontally.
   */
  void horizontalFlip();

  /**
   * Flips the current image vertically.
   */
  void verticalFlip();

  /**
   * Extracts the red component of the current image and displays it as a grayscale image.
   */
  void redComp();

  /**
   * Extracts the green component of the current image and displays it as a grayscale image.
   */
  void greenComp();

  /**
   * Extracts the blue component of the current image and displays it as a grayscale image.
   */
  void blueComp();

  /**
   * Calculates and displays the value component (maximum RGB value) of the current image as a
   * grayscale image.
   */
  void valueComp();

  /**
   * Calculates and displays the intensity component (average RGB value) of the current image as a
   * grayscale image.
   */
  void intensityComp();

  /**
   * Calculates and displays the luma component (weighted sum of RGB values) of the current image as
   * a grayscale image.
   */
  void lumaComp();

  /**
   * Applies color correction to the current image.
   */
  void colorCorrection();

  /**
   * Applies a blur filter to the current image.
   */
  void blur();

  /**
   * Applies a sharpening filter to the current image.
   */
  void sharpen();

  /**
   * Applies a sepia tone filter to the current image.
   */
  void sepia();

  /**
   * Adjusts the levels of the current image based on user-defined black, mid, and white points.
   */
  void levelsAdjust();

  /**
   * Downscale current image to a smaller target width and/or height.
   */
  void downscale();

  /**
   * Loads an image selected by the user through the GUI and stores it in the model.
   */
  void loadImage();

  /**
   * Switches to the previous version of the image, if available.
   */
  void previousVersion();

  /**
   * Switches to the next version of the image, if available.
   */
  void nextVersion();

  /**
   * Saves the current image to a user-defined location.
   *
   * @throws IOException if an error occurs while saving the image
   */
  void save() throws IOException;

  /**
   * Applies a specified filter to the current image using the provided arguments.
   *
   * @param filter the name of the filter to apply
   * @param args   the list of arguments required for the filter operation
   */
  void applyFilter(String filter, List<String> args);

  /**
   * Retrieves the current image as a {@code BufferedImage}.
   *
   * @param imageName the name of the image to retrieve
   * @return a {@code BufferedImage} representation of the specified image
   */
  BufferedImage getCurrentImage(String imageName);

  /**
   * Applies a filter to an image and updates the GUI to display the result
   * in a split view with the original image.
   *
   * @param filter the name of the filter to apply
   * @param args   the list of arguments required for the filter operation
   * @param img    the name of the image to apply the filter on
   */
  void splitViewFilter(String filter, List<String> args, String img);
}
