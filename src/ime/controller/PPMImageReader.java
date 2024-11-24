package ime.controller;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

/**
 * Implementation of the {@link ImageReader} interface for reading
 * images in the PPM (Portable Pixmap) format.
 * This class provides the functionality to read PPM files and
 * convert them into {@link BufferedImage} objects.
 */
public class PPMImageReader implements ImageReader {

  /**
   * Reads a PPM image from the specified file path.
   *
   * @param filePath the path to the PPM file to read.
   * @return a {@link BufferedImage} representation of the read PPM image.
   * @throws RuntimeException if an error occurs while reading the image,
   *                          such as invalid format or I/O issues.
   */
  @Override
  public BufferedImage read(String filePath) {
    return readPPM(filePath);
  }

  /**
   * Reads a PPM image from the specified file path and converts it
   * into a {@link BufferedImage}.
   *
   * @param filePath the path to the PPM file to read.
   * @return a {@link BufferedImage} representation of the read PPM image.
   * @throws RuntimeException if an error occurs while reading the image,
   *                          such as invalid format or I/O issues.
   */
  private BufferedImage readPPM(String filePath) {
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
      String line;

      while (true) {
        if ((line = br.readLine()) == null || !line.trim().startsWith("#")) {
          break;
        }
      }
      String magicNumber = line;
      if (!magicNumber.equals("P3")) {
        throw new IOException("Invalid PPM file format");
      }

      while (true) {
        if ((line = br.readLine()) == null || !line.trim().startsWith("#")) {
          break;
        }
      }
      String[] dimensions = Objects.requireNonNull(line).split("\\s+");
      int width = Integer.parseInt(dimensions[0]);
      int height = Integer.parseInt(dimensions[1]);

      while (true) {
        if ((line = br.readLine()) == null || !line.trim().startsWith("#")) {
          break;
        }
      }

      BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
      for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
          int r = Integer.parseInt(br.readLine());
          int g = Integer.parseInt(br.readLine());
          int b = Integer.parseInt(br.readLine());
          int rgb = (r << 16) | (g << 8) | b;
          image.setRGB(x, y, rgb);
        }
      }

      return image;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
