package ime.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Implementation of the {@link ImageReader} interface for reading
 * images using the standard Java ImageIO library.
 * This class provides functionality to load images from various formats
 * supported by ImageIO.
 */
public class StandardImageReader implements ImageReader {

  /**
   * Reads an image from the specified file path.
   *
   * @param filePath the path of the image file to be read
   * @return a {@link BufferedImage} representation of the image
   * @throws IOException if an error occurs during reading the image,
   *                     such as if the file does not exist or is not in a valid format
   */
  @Override
  public BufferedImage read(String filePath) throws IOException {
    return ImageIO.read(new File(filePath));
  }
}
