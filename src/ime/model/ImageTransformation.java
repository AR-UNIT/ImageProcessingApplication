package ime.model;

import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

/**
 * Provides various image transformation operations, including pixel transformations,
 * matrix multiplications, kernel applications, and pixel swapping. This class defines
 * functional interfaces to facilitate flexible and reusable image processing logic.
 * It supports operations such as applying transformations to each pixel, creating
 * transformations using matrices, and applying kernel operations for effects like
 * blurring and sharpening.
 */
class ImageTransformation {

  private final UnaryOperator<Integer> clamp = value -> Math.min(Math.max(value, 0), 255);

  /**
   * Applies a transformation to each pixel in an image.
   *
   * @param image          the original image to transform
   * @param transformation a functional interface to apply to each pixel
   * @return a new Image with transformed pixels
   */
  ImageInterface applyTransformation(ImageInterface image, PixelTransformation transformation,
                                     int splitPercent) {
    int height = image.getHeight();
    int width = image.getWidth();
    ImageInterface result = new Image(height, width);

    int splitPosition = ((splitPercent * width) / 100);

    // Traverse through all pixels
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        if (j < splitPosition) {
          // Apply transformation on the left side (up to splitPosition)
          PixelInterface originalPixel = image.getPixel(i, j);
          PixelInterface transformedPixel = transformation.apply(
                  originalPixel.getR(),
                  originalPixel.getG(),
                  originalPixel.getB(),
                  originalPixel.getA()
          );
          PixelInterface resultPixel = new Pixel(
                  clamp.apply(transformedPixel.getR()),
                  clamp.apply(transformedPixel.getG()),
                  clamp.apply(transformedPixel.getB()),
                  originalPixel.getA()
          );
          result.updatePixel(i, j, resultPixel);
        } else if (j == splitPosition) {
          // Insert black pixels for the split line
          // Black pixel with full opacity
          PixelInterface blackPixel = new Pixel(0, 0, 0, 255);
          result.updatePixel(i, j, blackPixel);
        } else {
          // Copy original pixel on the right side of the split
          result.updatePixel(i, j, image.getPixel(i, j));
        }
      }
    }

    return result;
  }


  /**
   * Creates a matrix multiplication operation for transforming RGB values.
   *
   * @param matrix a 3x3 transformation matrix
   * @return a MatrixMultiplication functional interface for applying transformations
   */
  public MatrixMultiplication createMatrixMultiplication(double[][] matrix) {
    UnaryOperator<Integer> clamp = value -> Math.min(Math.max(value, 0), 255);

    return (r, g, b) -> new int[]{
            clamp.apply((int) (r * matrix[0][0] + g * matrix[0][1] + b * matrix[0][2])),
            clamp.apply((int) (r * matrix[1][0] + g * matrix[1][1] + b * matrix[1][2])),
            clamp.apply((int) (r * matrix[2][0] + g * matrix[2][1] + b * matrix[2][2]))
    };
  }

  /**
   * Creates a pixel transformation using a given matrix.
   *
   * @param matrix a 3x3 transformation matrix
   * @return a PixelTransformationInterface for applying transformations to pixels
   */
  public PixelTransformation createTransformation(double[][] matrix) {
    MatrixMultiplication matrixMultiplication = createMatrixMultiplication(matrix);
    return (r, g, b, a) -> {
      int[] newColors = matrixMultiplication.apply(r, g, b);
      return new Pixel(newColors[0], newColors[1], newColors[2], a);
    };
  }

  /**
   * Applies pixel swapping logic to an image.
   *
   * @param image         the original image to transform
   * @param swappingLogic a functional interface defining how pixels are swapped
   * @return a new Image with swapped pixels
   */
  public ImageInterface applyPixelSwapping(ImageInterface image, PixelSwapping swappingLogic) {
    int width = image.getWidth();
    int height = image.getHeight();
    ImageInterface result = new Image(height, width);

    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        PixelInterface swappedPixel = swappingLogic.swap(x, y, image);
        result.updatePixel(y, x, swappedPixel);
      }
    }

    return result;
  }

  /**
   * Applies a kernel operation to an image.
   *
   * @param image     the original image to transform
   * @param operation a KernelOperation defining how kernels are applied
   * @return a new Image with applied kernel effects
   */
  public ImageInterface applyKernel(ImageInterface image, KernelOperation operation,
                                    int splitPercent) {
    int height = image.getHeight();
    int width = image.getWidth();

    // Calculate the split position based on the percentage
    int splitPosition = (splitPercent * width) / 100;

    ImageInterface resultImage = new Image(height, width);
    float[][] kernel = operation.getKernel();
    int padding = operation.getPadding();

    // Clamping function to ensure values are within [0, 255]
    BiFunction<Float, Float, Integer> clamp = (sum, weight) ->
            Math.min(Math.max(Math.round(sum / weight), 0), 255);

    // Traverse through all pixels
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        // Only apply the kernel operation if we're on the left side of the split
        if (x < splitPosition) {
          float sumR = 0;
          float sumG = 0;
          float sumB = 0;
          float sumWeights = 0;

          // Apply kernel operation (e.g., blur or sharpen)
          for (int ky = -padding; ky <= padding; ky++) {
            for (int kx = -padding; kx <= padding; kx++) {
              int nx = Math.min(Math.max(x + kx, 0), width - 1);
              int ny = Math.min(Math.max(y + ky, 0), height - 1);
              float weight = kernel[ky + padding][kx + padding];

              sumR += image.getPixel(ny, nx).getR() * weight;
              sumG += image.getPixel(ny, nx).getG() * weight;
              sumB += image.getPixel(ny, nx).getB() * weight;
              sumWeights += weight;
            }
          }

          // Apply clamping to ensure values are within [0, 255]
          int newR = clamp.apply(sumR, sumWeights);
          int newG = clamp.apply(sumG, sumWeights);
          int newB = clamp.apply(sumB, sumWeights);
          int alpha = image.getPixel(y, x).getA();

          PixelInterface resultPixel = new Pixel(newR, newG, newB, alpha);
          resultImage.updatePixel(y, x, resultPixel);
        } else if (x == splitPosition) {
          // Insert black pixels for the split line
          PixelInterface blackPixel = new Pixel(0, 0, 0, 255);
          resultImage.updatePixel(y, x, blackPixel);
        } else {
          // Copy original pixel on the right side of the split
          resultImage.updatePixel(y, x, image.getPixel(y, x));
        }
      }
    }

    return resultImage;
  }

  /**
   * Applies color correction by shifting pixel values based on calculated offsets for red,
   * green, and blue channels. The operation only applies to the left side of the image
   * up to a specified split position, while the right side remains unchanged.
   *
   * @param image        The original image to be corrected.
   * @param redOffset    The offset to apply to red channel values.
   * @param greenOffset  The offset to apply to green channel values.
   * @param blueOffset   The offset to apply to blue channel values.
   * @param splitPercent The percentage of the width where the split occurs. If -1, apply color
   *                     correction to the entire image.
   * @return A new Image object with adjusted pixel values.
   */
  public ImageInterface applyColorCorrection(ImageInterface image, int redOffset,
                                             int greenOffset, int blueOffset, int splitPercent) {
    int width = image.getWidth();
    int height = image.getHeight();

    // Calculate the split position based on the percentage
    int splitPosition = (splitPercent * width) / 100;

    PixelInterface[][] correctedPixels = new Pixel[height][width];

    // Traverse through all pixels and adjust RGB values based on offsets
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        PixelInterface pixel = image.getPixel(y, x);

        if (x < splitPosition) {
          // Apply color correction only on the left side (up to splitPosition)
          int newRed = clamp.apply(pixel.getR() + redOffset);
          int newGreen = clamp.apply(pixel.getG() + greenOffset);
          int newBlue = clamp.apply(pixel.getB() + blueOffset);

          correctedPixels[y][x] = new Pixel(newRed, newGreen, newBlue, pixel.getA());
        } else if (x == splitPosition) {
          // Insert a black pixel at the split position
          correctedPixels[y][x] = new Pixel(0, 0, 0, 255);
        } else {
          // Copy original pixel on the right side of the split
          correctedPixels[y][x] = pixel;
        }
      }
    }

    // Create a new Image with corrected pixels
    ImageInterface correctedImage = new Image(height, width);
    correctedImage.imageFill(correctedPixels);

    return correctedImage;
  }

  /**
   * Applies levels adjustment using a quadratic function with coefficients A, B, and C.
   * The operation only applies to the left side of the image up to a specified split position,
   * while the right side remains unchanged.
   *
   * @param image        The original image to be adjusted.
   * @param a            The coefficient for x^2 in the quadratic equation.
   * @param b            The coefficient for x in the quadratic equation.
   * @param c            The constant term in the quadratic equation.
   * @param splitPercent The percentage of the width where the split occurs. If -1, apply levels
   *                     adjustment to the entire image.
   * @return A new Image object with adjusted pixel values.
   */
  public ImageInterface applyLevelsAdjustment(ImageInterface image, double a, double b,
                                              double c, int splitPercent) {
    int width = image.getWidth();
    int height = image.getHeight();

    // Calculate the split position based on the percentage
    int splitPosition = (splitPercent * width) / 100;

    PixelInterface[][] adjustedPixels = new Pixel[height][width];

    // Traverse through all pixels and adjust RGB values based on levels adjustment curve
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        PixelInterface pixel = image.getPixel(y, x);

        if (x < splitPosition) {
          // Apply levels adjustment only on the left side (up to splitPosition)
          int newRed = clamp.apply((int) (a * Math.pow(pixel.getR(), 2) + b * pixel.getR() + c));
          int newGreen = clamp.apply((int) (a * Math.pow(pixel.getG(), 2) + b * pixel.getG() + c));
          int newBlue = clamp.apply((int) (a * Math.pow(pixel.getB(), 2) + b * pixel.getB() + c));

          adjustedPixels[y][x] = new Pixel(newRed, newGreen, newBlue, pixel.getA());
        } else if (x == splitPosition) {
          // Insert a black pixel at the split position
          adjustedPixels[y][x] = new Pixel(0, 0, 0, 255);
        } else {
          // Copy original pixel on the right side of the split
          adjustedPixels[y][x] = pixel;
        }
      }
    }

    // Create a new Image with adjusted pixels
    ImageInterface adjustedImage = new Image(height, width);
    adjustedImage.imageFill(adjustedPixels);

    return adjustedImage;
  }
}
