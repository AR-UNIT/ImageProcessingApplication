package ime.model;

/**
 * The {@code PixelSwapping} interface defines a contract for implementing pixel
 * transformation operations within an image. Classes that implement this interface
 * are expected to define specific pixel swapping or manipulation logic based on
 * coordinates within the image.
 */
interface PixelSwapping {
  /**
   * Swaps a pixel at given coordinates in an image.
   *
   * @param x     the x-coordinate of the pixel
   * @param y     the y-coordinate of the pixel
   * @param image the image containing the pixel
   * @return a new Pixel after swapping
   */
  PixelInterface swap(int x, int y, ImageInterface image);
}
