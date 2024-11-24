package ime.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Interface for reading images from file paths.
 * This interface provides a method to read an image and return it as a
 * BufferedImage object.
 */
public interface ImageReader {

  /**
   * Reads an image from the specified file path.
   *
   * @param filePath the path of the image file to read
   * @return a BufferedImage object representing the read image
   * @throws IOException if an error occurs while reading the image,
   *                     such as if the file does not exist or is not accessible
   */
  BufferedImage read(String filePath) throws IOException;
}
