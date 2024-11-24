package ime.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;


import static ime.model.ImageChannelConstants.ALPHA;
import static ime.model.ImageChannelConstants.BLUE;
import static ime.model.ImageChannelConstants.GREEN;
import static ime.model.ImageChannelConstants.RED;
import static java.lang.Integer.max;
import static java.lang.Integer.parseInt;

/**
 * Class to compress an image using 2D haar wavelet transformation, and store result image in model.
 */
public class Compress extends AbstractFilters {

  private final ModelInterface model;

  public Compress(ModelInterface model) {
    this.model = model;
  }

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

  private Image padImageToPowerTwo(ImageInterface image) {
    int originalHeight = image.getHeight();
    int originalWidth = image.getWidth();

    int newHeight = dimensionPowerOfTwo(originalHeight);
    int newWidth = dimensionPowerOfTwo(originalWidth);
    int finalDimensions = max(newWidth, newHeight);

    Image paddedImage = new Image(finalDimensions, finalDimensions);

    for (int y = 0; y < originalHeight; y++) {
      for (int x = 0; x < originalWidth; x++) {
        PixelInterface pixel = image.getPixel(y, x);
        paddedImage.updatePixel(y, x, pixel);
      }
    }

    for (int y = 0; y < finalDimensions; y++) {
      for (int x = 0; x < finalDimensions; x++) {
        if (y >= originalHeight || x >= originalWidth) {
          paddedImage.updatePixel(y, x, new Pixel(0, 0, 0, 0));
        }
      }
    }

    return paddedImage;
  }

  private Map<String, double[][]> getImageSplitByChannelMatrix(ImageInterface image, int size) {
    double[][] redChannel = new double[size][size];
    double[][] greenChannel = new double[size][size];
    double[][] blueChannel = new double[size][size];
    double[][] alphaChannel = new double[size][size];

    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
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
   * Creates a compressed image output, using Haar wavelet 2D compression.
   *
   * @param image                 image to apply compression on
   * @param compressionPercentage percentage to compress image by
   * @return compressed ImageInterface type object representing image
   * @throws IllegalArgumentException if the compression percentage not between 0-100(inclusive)
   */
  private ImageInterface haarCompression(
          ImageInterface image, int compressionPercentage) throws IllegalArgumentException {
    if (compressionPercentage < 0 || compressionPercentage > 100) {
      throw new IllegalArgumentException("Compression percentage must be between 0 and 100");
    }
    int width = image.getWidth();

    int height = image.getHeight();

    image = padImageToPowerTwo(image);
    int size = image.getHeight();
    Map<String, double[][]> imageChannelMatrices = getImageSplitByChannelMatrix(image, size);

    for (String channel : ImageChannelConstants.getAllChannels()) {
      if (!Objects.equals(channel, ALPHA)) {
        haarTransform(imageChannelMatrices.get(channel), size);
        compressChannelByPercentage(imageChannelMatrices.get(channel), compressionPercentage);
        inverseHaarTransform(imageChannelMatrices.get(channel), size);
      }
    }

    Image res = new Image(height, width);
    PixelInterface[][] compressedPixels = new PixelInterface[height][width];
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        int red = clamp((int) Math.round(imageChannelMatrices.get(RED)[i][j]));
        int green = clamp((int) Math.round(imageChannelMatrices.get(GREEN)[i][j]));
        int blue = clamp((int) Math.round(imageChannelMatrices.get(BLUE)[i][j]));
        int alpha = (int) imageChannelMatrices.get(ALPHA)[i][j];
        PixelInterface compressedPixel = new Pixel(red, green, blue, alpha);
        compressedPixels[i][j] = compressedPixel;
      }
    }
    res.imageFill(compressedPixels);

    return res;
  }


  private static void haarTransform(double[][] data, int size) {
    int c = size;
    while (c > 1) {
      for (int i = 0; i < c; i++) {
        double[] row = new double[c];
        for (int j = 0; j < c; j++) {
          row[j] = data[i][j];
        }
        row = haarRowWise(row);
        for (int j = 0; j < c; j++) {
          data[i][j] = row[j];
        }
      }

      for (int j = 0; j < c; j++) {
        double[] col = new double[c];
        for (int i = 0; i < c; i++) {
          col[i] = data[i][j];
        }
        col = haarRowWise(col);
        for (int i = 0; i < c; i++) {
          data[i][j] = col[i];
        }
      }

      c = c / 2;
    }
  }

  private static void inverseHaarTransform(double[][] data, int size) {
    int c = 2;
    while (c <= size) {
      for (int j = 0; j < c; j++) {
        double[] col = new double[c];
        for (int i = 0; i < c; i++) {
          col[i] = data[i][j];
        }
        col = inverseHaarRowWise(col);
        for (int i = 0; i < c; i++) {
          data[i][j] = col[i];
        }
      }

      for (int i = 0; i < c; i++) {
        double[] row = new double[c];
        for (int j = 0; j < c; j++) {
          row[j] = data[i][j];
        }
        row = inverseHaarRowWise(row);
        for (int j = 0; j < c; j++) {
          data[i][j] = row[j];
        }
      }

      c = c * 2;
    }
  }


  private static double[] haarRowWise(double[] data) {
    int length = data.length;
    double[] result = new double[length];

    for (int i = 0; i < length / 2; i++) {
      result[i] = (data[2 * i] + data[2 * i + 1]) / Math.sqrt(2);
      result[i + length / 2] = (data[2 * i] - data[2 * i + 1]) / Math.sqrt(2);
    }

    return result;
  }


  private static double[] inverseHaarRowWise(double[] data) {
    int length = data.length;
    double[] result = new double[length];

    for (int i = 0; i < length / 2; i++) {
      result[2 * i] = (data[i] + data[i + length / 2]) / Math.sqrt(2);
      result[2 * i + 1] = (data[i] - data[i + length / 2]) / Math.sqrt(2);
    }

    return result;
  }


  private static int dimensionPowerOfTwo(int n) {
    int power = 1;
    while (power < n) {
      power *= 2;
    }
    return power;
  }

  private static void compressChannelByPercentage(double[][] channel, int compressionPercentage) {
    int size = channel.length;
    double compressionRatio = compressionPercentage / 100.00;

    Set<Double> distinctNonZeroValues = new HashSet<>();
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        if (channel[i][j] != 0) {
          distinctNonZeroValues.add(Math.abs(channel[i][j]));
        }
      }
    }

    List<Double> sortedDistinctValues = new ArrayList<>(distinctNonZeroValues);
    Collections.sort(sortedDistinctValues);

    int valuesToZeroOut = (int) (sortedDistinctValues.size() * compressionRatio);
    if (valuesToZeroOut > 0) {
      double cutoffMagnitude = sortedDistinctValues.get(valuesToZeroOut - 1);
      for (int i = 0; i < size; i++) {
        for (int j = 0; j < size; j++) {
          if (Math.abs(channel[i][j]) <= cutoffMagnitude) {
            channel[i][j] = 0;
          }
        }
      }
    }
  }


  private static int clamp(int value) {
    return Math.max(0, Math.min(255, value));
  }

  @Override
  public void execute(List<String> commandTokens) {
    /* compress increment image-name dest-image-name */

    int compressionPercentage = parseInt(commandTokens.get(1));
    String imageName = commandTokens.get(2);
    String destName = commandTokens.get(3);
    if (!(compressionPercentage <= 100 && compressionPercentage >= 0)) {
      throw new IllegalArgumentException("Compression percentage must be between 0 and 100");
    }

    ImageInterface originalImage = model.getImage(imageName);
    ImageInterface compressedImage = haarCompression(originalImage, compressionPercentage);
    model.storeImage(destName, compressedImage);

  }
}