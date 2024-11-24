package ime.model;


/**
 * Interface defining the methods to access the color components of a pixel.
 * Provides methods to retrieve the red, green, blue, and alpha (transparency) values.
 * These methods return integer values representing the intensity of each component
 * in the range of 0 to 255.
 */
interface PixelInterface {
  /**
   * Get the red component of the pixel.
   *
   * @return the red component value (0-255)
   */
  int getR();

  /**
   * Get the green component of the pixel.
   *
   * @return the green component value (0-255)
   */
  int getG();

  /**
   * Get the blue component of the pixel.
   *
   * @return the blue component value (0-255)
   */
  int getB();

  /**
   * Get the alpha (transparency) component of the pixel.
   *
   * @return the alpha component value (0-255)
   */
  int getA();
}
