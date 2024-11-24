package ime.controller;

/**
 * Factory class for creating instances of {ImageWriter}.
 * This class provides a method to obtain an appropriate image writer
 * based on the specified file type.
 */
public class ImageWriterFactory {

  /**
   * Retrieves an {ImageWriter} instance based on the specified file type.
   *
   * @param fileType the type of the image file (e.g., "ppm")
   * @return an instance of {ImageWriter} for the specified file type
   */
  public static ImageWriter getWriter(String fileType) {
    if (fileType.equalsIgnoreCase("ppm")) {
      return new PPMImageWriter();
    }
    return new StandardImageWriter();
  }
}
