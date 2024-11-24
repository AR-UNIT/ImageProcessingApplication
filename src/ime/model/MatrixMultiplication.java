package ime.model;

/**
 * The {@code MatrixMultiplication} interface defines a structure for applying
 * matrix transformations to RGB color values. Implementing classes are expected
 * to perform operations that transform these values based on a specific matrix.
 */
interface MatrixMultiplication {
  /**
   * Applies matrix multiplication to RGB values.
   *
   * @param red   the red component
   * @param green the green component
   * @param blue  the blue component
   * @return an array of transformed RGB values
   */
  int[] apply(int red, int green, int blue);
}
