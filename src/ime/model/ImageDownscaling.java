package ime.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static ime.model.ImageChannelConstants.ALPHA;
import static ime.model.ImageChannelConstants.BLUE;
import static ime.model.ImageChannelConstants.GREEN;
import static ime.model.ImageChannelConstants.RED;
import static java.lang.Integer.parseInt;

/**
 * This class generates downscaled images from source images, and stores result in model.
 * The downscaled images are created by accepting a target width and target height, which are
 * smaller than or equal to the original values, and are positive valued.
 */
public class ImageDownscaling extends AbstractFilters {

  private static int floor(double val) {
    return (int) Math.floor(val);
  }

  private static int ceil(double val, int dimension) {
    return (int) Math.min(Math.ceil(val + 1), dimension - 1);
  }

  private final ModelInterface model;

  /**
   * Constructor for this image filter class.
   *
   * @param model the model to use
   */
  public ImageDownscaling(ModelInterface model) {
    this.model = model;
  }

  /**
   * Clamps the given value to ensure it falls within the range [0, 255].
   *
   * @param value the value to clamp.
   * @return the clamped value within the range [0, 255].
   */
  private static int clamp(int value) {
    return Math.max(0, Math.min(255, value));
  }

  /**
   * Retrieves the value of a specific channel (RED, GREEN, BLUE, or ALPHA) from a pixel.
   *
   * @param p       the pixel to extract the channel value from.
   * @param channel the channel to retrieve (e.g., RED, GREEN, BLUE, ALPHA).
   * @return the value of the specified channel, or -1 if the channel is invalid.
   */
  private int getChannelVal(PixelInterface p, String channel) {
    if (Objects.equals(channel, RED)) {
      return p.getR();
    }
    if (Objects.equals(channel, GREEN)) {
      return p.getG();
    }
    if (Objects.equals(channel, BLUE)) {
      return p.getB();
    }
    if (Objects.equals(channel, ALPHA)) {
      return p.getA();
    }
    return -1;
  }

  /**
   * Splits an image into its channel matrices (RED, GREEN, BLUE, ALPHA).
   *
   * @param image  the image to process.
   * @param width  the width of the image.
   * @param height the height of the image.
   * @return a map containing the channel names as keys and their respective matrices as values.
   */
  private Map<String, double[][]> getImageSplitByChannelMatrix(ImageInterface image, int width,
                                                               int height) {
    double[][] redChannel = new double[height][width];
    double[][] greenChannel = new double[height][width];
    double[][] blueChannel = new double[height][width];
    double[][] alphaChannel = new double[height][width];

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        redChannel[i][j] = getChannelVal(image.getPixel(i, j), RED);
        greenChannel[i][j] = getChannelVal(image.getPixel(i, j), GREEN);
        blueChannel[i][j] = getChannelVal(image.getPixel(i, j), BLUE);
        alphaChannel[i][j] = getChannelVal(image.getPixel(i, j), ALPHA);
      }
    }
    Map<String, double[][]> imageChannelMatrices = new HashMap<>();
    imageChannelMatrices.put(RED, redChannel);
    imageChannelMatrices.put(GREEN, greenChannel);
    imageChannelMatrices.put(BLUE, blueChannel);
    imageChannelMatrices.put(ALPHA, alphaChannel);
    return imageChannelMatrices;
  }

  /**
   * Checks if a given number is an integer, allowing precision up to two decimal places.
   *
   * @param num the number to check.
   * @return {@code true} if the number is effectively an integer; {@code false} otherwise.
   */
  private static boolean checkNoDecimal(double num) {
    /* checking for decimal upto 2 decimal places only */
    double rounded = Math.round(num * 100.0) / 100.0;
    return (rounded % 1 == 0);
  }

  /**
   * Calculates the source coordinate in the original dimension based on scaling.
   *
   * @param sourceDimension  the size of the source dimension.
   * @param currentDimension the size of the target dimension.
   * @param currentDistance  the current position in the target dimension.
   * @return the corresponding coordinate in the source dimension.
   */
  private static double getSourceCoordinate(int sourceDimension, int currentDimension,
                                            int currentDistance) {
    return sourceDimension * ((double) currentDistance / currentDimension);
  }

  /**
   * Retrieves the 'A' pixel value from the given matrix at the specified coordinates.
   *
   * @param originalMatrix the source matrix.
   * @param xX             the x-coordinate.
   * @param yY             the y-coordinate.
   * @return the value at the specified location.
   */
  private static double getA(double[][] originalMatrix, double xX, double yY) {
    int x = floor(xX);
    int y = floor(yY);
    return originalMatrix[x][y];
  }

  /**
   * Retrieves the 'B' pixel value from the given matrix at the specified coordinates.
   *
   * @param originalMatrix the source matrix.
   * @param xX             the x-coordinate.
   * @param yY             the y-coordinate.
   * @return the value at the specified location.
   */
  private static double getB(double[][] originalMatrix, double xX, double yY) {
    int x = ceil(xX, originalMatrix.length);
    int y = floor(yY);
    return originalMatrix[x][y];
  }

  /**
   * Retrieves the 'C' pixel value from the given matrix at the specified coordinates.
   *
   * @param originalMatrix the source matrix.
   * @param xX             the x-coordinate.
   * @param yY             the y-coordinate.
   * @return the value at the specified location.
   */
  private static double getC(double[][] originalMatrix, double xX, double yY) {
    int x = floor(xX);
    int y = ceil(yY, originalMatrix[0].length);
    return originalMatrix[x][y];
  }

  /**
   * Retrieves the 'D' pixel value from the given matrix at the specified coordinates.
   *
   * @param originalMatrix the source matrix.
   * @param xX             the x-coordinate.
   * @param yY             the y-coordinate.
   * @return the value at the specified location.
   */
  private static double getD(double[][] originalMatrix, double xX, double yY) {
    int x = ceil(xX, originalMatrix.length);
    int y = ceil(yY, originalMatrix[0].length);
    return originalMatrix[x][y];
  }

  /**
   * Calculates a weighted average of two pixel values based on the x-coordinate.
   *
   * @param originalMatrix the source matrix.
   * @param pixelA         the first pixel value.
   * @param pixelB         the second pixel value.
   * @param xX             the x-coordinate.
   * @return the calculated weighted average.
   */
  private static double getM(double[][] originalMatrix, double pixelA, double pixelB,
                             double xX) {
    return (pixelB * (xX - floor(xX))) + (pixelA * (ceil(xX, originalMatrix.length) - xX));
  }


  /**
   * Method to get the N.
   *
   * @param originalMatrix original matrix to be downscaled
   * @param pixelC         formula params
   * @param pixelD         formula params
   * @param xX             x value
   * @return double
   */
  private static double getN(double[][] originalMatrix, double pixelC, double pixelD,
                             double xX) {
    return (pixelD * (xX - floor(xX))) + (pixelC * (ceil(xX, originalMatrix.length) - xX));
  }


  /**
   * Method to extract downscaled pixel value from source.
   *
   * @param originalMatrix original matrix to be downscaled
   * @param xX             x value
   * @param yY             y value
   * @return double
   */
  private static double extractDownscaledPixelValueFromSource(
          double[][] originalMatrix, double xX, double yY
  ) {
    if (checkNoDecimal(xX) && checkNoDecimal(yY)) {
      int x = (int) xX;
      int y = (int) yY;

      return originalMatrix[x][y];
    }
    double pixelValue;
    double pixelA = getA(originalMatrix, xX, yY);
    double pixelB = getB(originalMatrix, xX, yY);
    double pixelC = getC(originalMatrix, xX, yY);
    double pixelD = getD(originalMatrix, xX, yY);
    double m = getM(originalMatrix, pixelA, pixelB, xX);
    double n = getN(originalMatrix, pixelC, pixelD, xX);
    pixelValue = n * (yY - floor(yY)) + m * (ceil(yY, originalMatrix[0].length) - yY);
    return pixelValue;
  }

  /**
   * Method to downscale matrix.
   *
   * @param originalMatrix   original matrix to be downscaled
   * @param downscaledHeight downscaled height.
   * @param downscaledWidth  downscaled width.
   * @return double[][]
   */
  private static double[][] downscaleMatrix(double[][] originalMatrix, int downscaledHeight,
                                            int downscaledWidth) {
    double[][] downscaledImageMatrices = new double[downscaledHeight][downscaledWidth];
    int originalMatrixHeight = originalMatrix.length;
    int originalMatrixWidth = originalMatrix[0].length;
    for (int i = 0; i < downscaledHeight; i++) {
      for (int j = 0; j < downscaledWidth; j++) {
        double xX = getSourceCoordinate(originalMatrixWidth, downscaledWidth, j);
        double yY = getSourceCoordinate(originalMatrixHeight, downscaledHeight, i);
        downscaledImageMatrices[i][j] =
                extractDownscaledPixelValueFromSource(originalMatrix, yY, xX);
      }
    }

    return downscaledImageMatrices;
  }

  /**
   * Method to downscale image.
   *
   * @param image        Image to be downscaled
   * @param targetWidth  width to which we downscale
   * @param targetHeight height to which we downscale
   * @return ImageInterface
   */
  private ImageInterface downscaleImage(ImageInterface image, int targetWidth, int targetHeight) {

    int height = image.getHeight();
    int width = image.getWidth();
    Map<String, double[][]> imageChannelMatrices =
            getImageSplitByChannelMatrix(image, width, height);
    ImageInterface downscaledImage = new Image(targetHeight, targetWidth);

    Map<String, double[][]> downscaledImageMatrices = new HashMap<>();
    for (String channel : ImageChannelConstants.getAllChannels()) {
      if (!Objects.equals(channel, ALPHA)) {
        downscaledImageMatrices.put(channel, downscaleMatrix(imageChannelMatrices.get(channel),
                targetHeight, targetWidth));
      }
    }

    PixelInterface[][] downscaledPixels = new PixelInterface[targetHeight][targetWidth];
    for (int i = 0; i < targetHeight; i++) {
      for (int j = 0; j < targetWidth; j++) {
        int red = clamp((int) Math.round(downscaledImageMatrices.get(RED)[i][j]));
        int green = clamp((int) Math.round(downscaledImageMatrices.get(GREEN)[i][j]));
        int blue = clamp((int) Math.round(downscaledImageMatrices.get(BLUE)[i][j]));
        int alpha = 255;
        PixelInterface downscaledPixel = new Pixel(red, green, blue, alpha);
        downscaledPixels[i][j] = downscaledPixel;
      }
    }
    downscaledImage.imageFill(downscaledPixels);

    return downscaledImage;
  }


  @Override
  public void execute(List<String> commandTokens) throws IllegalArgumentException {
    /* downscale width height image-name dest-image-name */

    int targetWidth = parseInt(commandTokens.get(1));
    int targetHeight = parseInt(commandTokens.get(2));
    String imageName = commandTokens.get(3);
    String destName = commandTokens.get(4);
    ImageInterface originalImage = model.getImage(imageName);

    if (targetWidth > originalImage.getWidth() || targetHeight > originalImage.getHeight()) {
      throw new IllegalArgumentException("Target width/height cannot be greater " +
              "than original width/height in downscaling.");
    }

    if (targetWidth <= 0 || targetHeight <= 0) {
      throw new IllegalArgumentException("Target width/height cannot be negative.");
    }

    ImageInterface downscaledImage = downscaleImage(originalImage, targetWidth, targetHeight);
    model.storeImage(destName, downscaledImage);
  }

}
