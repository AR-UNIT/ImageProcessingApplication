package ime.controller;

/**
 * Factory class for creating instances of {ImageReader}.
 * This class provides a method to obtain an appropriate image reader
 * based on the specified file type.
 */
public class ImageReaderFactory {

  /**
   * Retrieves an {ImageReader} instance based on the specified file type.
   *
   * @param fileType the type of the image file (e.g., "ppm")
   * @return an instance of {ImageReader} for the specified file type
   */
  public static ImageReader getReader(String fileType) {
    if (fileType.equalsIgnoreCase("ppm")) {
      return new PPMImageReader();
    }
    return new StandardImageReader();
  }
}
