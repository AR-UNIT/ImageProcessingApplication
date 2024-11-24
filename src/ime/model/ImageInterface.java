package ime.model;

/**
 * The ImageInterface defines the contract for various image processing operations.
 * It provides methods for image storage, retrieval, and transformations such as
 * getting and updating pixels, retrieving image dimensions, and filling the image
 * with pixel data.
 */
interface ImageInterface {

  /**
   * Retrieves the pixel at the specified coordinates in the image.
   *
   * @param idx1 the row index of the pixel
   * @param idx2 the column index of the pixel
   * @return the PixelInterface object representing the pixel at the given coordinates
   * @throws IndexOutOfBoundsException if the indices are out of the image bounds
   */
  PixelInterface getPixel(int idx1, int idx2);

  /**
   * Updates the pixel at the specified coordinates with a new pixel value.
   *
   * @param idx1          the row index of the pixel to update
   * @param idx2          the column index of the pixel to update
   * @param newPixelValue the new PixelInterface object to set at the specified position
   * @throws IndexOutOfBoundsException if the indices are out of the image bounds
   */
  void updatePixel(int idx1, int idx2, PixelInterface newPixelValue);

  /**
   * Retrieves the height of the image in pixels.
   *
   * @return the height of the image as an integer
   */
  int getHeight();

  /**
   * Retrieves the width of the image in pixels.
   *
   * @return the width of the image as an integer
   */
  int getWidth();

  /**
   * Fills the entire image with the provided pixel array.
   * This method replaces all existing pixel data in the image.
   *
   * @param pixelArray a 2D array of PixelInterface objects to fill the image
   * @throws IllegalArgumentException if the dimensions of pixelArray do not match the image
   *                                  dimensions
   */
  void imageFill(PixelInterface[][] pixelArray);

}