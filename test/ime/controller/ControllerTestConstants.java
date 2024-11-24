package ime.controller;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Map;

/**
 * A utility class that holds test constants for image matrices, including the red, green, blue,
 * and
 * alpha channels. It also provides functionality to convert matrix string representations to actual
 * 2D integer
 * arrays.
 */
public class ControllerTestConstants {

  /**
   * A string representation of the red channel matrix for testing.
   */
  public static final String RED_MATRIX_READ = Arrays.deepToString(new int[][]{
          {0, 0, 0, 0},
          {48, 0, 0, 244},
          {0, 0, 0, 244},
          {244, 244, 244, 244}
  });

  /**
   * A string representation of the green channel matrix for testing.
   */
  public static final String GREEN_MATRIX_READ = Arrays.deepToString(new int[][]{
          {255, 255, 255, 255},
          {0, 255, 255, 0},
          {255, 255, 255, 0},
          {0, 0, 0, 0}
  });

  /**
   * A string representation of the blue channel matrix for testing.
   */
  public static final String BLUE_MATRIX_READ = Arrays.deepToString(new int[][]{
          {0, 0, 0, 0},
          {246, 0, 0, 0},
          {0, 0, 0, 0},
          {0, 0, 0, 0}
  });

  /**
   * A string representation of the alpha channel matrix for testing.
   */
  public static final String ALPHA_MATRIX_READ = Arrays.deepToString(new int[][]{
          {255, 255, 255, 255},
          {255, 255, 255, 255},
          {255, 255, 255, 255},
          {255, 255, 255, 255}
  });

  /**
   * Converts a string representation of a matrix into a two-dimensional integer array.
   *
   * @param matrixStr The string representation of the matrix to be converted.
   * @return The two-dimensional integer array representing the matrix.
   */
  public static int[][] convertStringToMatrix(String matrixStr) {
    matrixStr = matrixStr.replace("[[", "").replace("]]", "");
    String[] rows = matrixStr.split("],\\s*\\[");

    int[][] matrix = new int[rows.length][];
    for (int i = 0; i < rows.length; i++) {
      String[] values = rows[i].split(",\\s*");
      matrix[i] = new int[values.length];
      for (int j = 0; j < values.length; j++) {
        matrix[i][j] = Integer.parseInt(values[j].trim());
      }
    }
    return matrix;
  }

  /**
   * A map that holds the two-dimensional integer arrays for the red, green,
   * blue and alpha image channels. The keys are the channel names (red,
   * green blue and alpha), and the values are the corresponding matrices.
   */
  public static final Map<String, int[][]> FETCHED_IMAGE_MATRICES = Map.of(
          "red", convertStringToMatrix(RED_MATRIX_READ),
          "green", convertStringToMatrix(GREEN_MATRIX_READ),
          "blue", convertStringToMatrix(BLUE_MATRIX_READ),
          "alpha", convertStringToMatrix(ALPHA_MATRIX_READ)
  );

  static Map<String, int[][]> convertTo2DPixelArray(BufferedImage bufferedImage) {
    int width = bufferedImage.getWidth();
    int height = bufferedImage.getHeight();
    int[][] redMatrix = new int[height][width];
    int[][] greenMatrix = new int[height][width];
    int[][] blueMatrix = new int[height][width];
    int[][] alphaMatrix = new int[height][width];

    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        int argb = bufferedImage.getRGB(x, y);
        redMatrix[y][x] = (argb >> 16) & 0xFF;
        greenMatrix[y][x] = (argb >> 8) & 0xFF;
        blueMatrix[y][x] = argb & 0xFF;
        alphaMatrix[y][x] = (argb >> 24) & 0xFF;
      }
    }

    return Map.of("red", redMatrix, "green", greenMatrix, "blue", blueMatrix,
            "alpha", alphaMatrix);
  }
}
