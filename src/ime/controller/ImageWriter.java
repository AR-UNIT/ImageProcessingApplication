package ime.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;

/**
 * Interface for writing images to file.
 * This interface provides a method to write a BufferedImage to a specified file
 * in a specified format.
 */
public interface ImageWriter {

  /**
   * Writes the specified image to the given output file in the specified format.
   *
   * @param image      the BufferedImage to write
   * @param format     the format to use for writing the image (e.g., "png", "jpg")
   * @param outputFile the file to which the image should be written
   * @throws IOException if an error occurs while writing the image,
   *                     such as if the output file cannot be created or accessed
   */
  void write(BufferedImage image, String format, File outputFile) throws IOException;
}
