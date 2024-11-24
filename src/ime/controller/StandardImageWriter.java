package ime.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Implementation of the {@link ImageWriter} interface for writing images using the standard Java
 * ImageIO library. This class provides functionality to save images in various formats supported
 * by ImageIO.
 */
public class StandardImageWriter implements ImageWriter {

  /**
   * Writes the specified image to a file in the given format.
   *
   * @param image      the {@link BufferedImage} to write to the file
   * @param format     the format of the image (e.g., "png", "jpg")
   * @param outputFile the {@link File} where the image will be written
   * @throws IOException if an error occurs during writing the image,
   *                     such as issues with file access or unsupported format
   */
  @Override
  public void write(BufferedImage image, String format, File outputFile) throws IOException {
    ImageIO.write(image, format, outputFile);
  }
}
