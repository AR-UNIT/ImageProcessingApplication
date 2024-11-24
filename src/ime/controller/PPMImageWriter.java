package ime.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Implementation of the {@link ImageWriter} interface for writing
 * images in the PPM (Portable Pixmap) format.
 * This class provides functionality to convert a {@link BufferedImage}
 * into a PPM file format and write it to a specified output file.
 */
public class PPMImageWriter implements ImageWriter {

  /**
   * Writes the given image to a file in PPM format.
   *
   * @param image      the {@link BufferedImage} to write to the file.
   * @param format     the format of the image (should be "ppm").
   * @param outputFile the {@link File} where the image will be written.
   * @throws IOException if an error occurs during writing to the file,
   *                     such as issues with file access or format.
   */
  @Override
  public void write(BufferedImage image, String format, File outputFile) throws IOException {
    try (PrintWriter writer = new PrintWriter(new FileWriter(outputFile))) {
      writer.println("P3");
      writer.println(image.getWidth() + " " + image.getHeight());
      writer.println("255");

      for (int y = 0; y < image.getHeight(); y++) {
        for (int x = 0; x < image.getWidth(); x++) {
          int rgb = image.getRGB(x, y);
          int r = (rgb >> 16) & 0xFF;
          int g = (rgb >> 8) & 0xFF;
          int b = rgb & 0xFF;

          writer.write(r + "\n");
          writer.write(g + "\n");
          writer.write(b + "\n");
        }
      }
    }
  }
}
