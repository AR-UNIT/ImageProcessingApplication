package ime.model;

/**
 * The PixelTransformation interface defines a contract for transforming individual pixels.
 * Implementations of this interface provide specific algorithms for modifying the RGBA values
 * of a pixel.
 */
interface PixelTransformation {
  /**
   * Applies a transformation to the pixel's RGBA values.
   *
   * @param red   the red component
   * @param green the green component
   * @param blue  the blue component
   * @param alpha the alpha component
   * @return a new Pixel with transformed values
   */
  Pixel apply(int red, int green, int blue, int alpha);
}
