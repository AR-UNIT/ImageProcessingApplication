package ime.model;

/**
 * Represents an image composed of pixels. This class provides methods for
 * manipulating pixel data within an image, such as filling the image with
 * pixel data, updating individual pixels, and retrieving pixel information.
 * It supports operations to determine the dimensions of the image and
 * implements equality and hashing based on pixel data.
 */
class Image implements ImageInterface {

  private PixelInterface[][] image;

  /**
   * Constructs an Image with the specified dimensions.
   *
   * @param height the height of the image in pixels
   * @param width  the width of the image in pixels
   */
  Image(int height, int width) {
    this.image = new Pixel[height][width];
  }

  /**
   * Fills the image with the provided pixel array.
   *
   * @param pixelArray a 2D array of Pixel objects to fill the image
   */
  public void imageFill(PixelInterface[][] pixelArray) {
    this.image = pixelArray;
  }

  /**
   * Updates a specific pixel in the image.
   *
   * @param idx1          the row index of the pixel
   * @param idx2          the column index of the pixel
   * @param newPixelValue the new Pixel value to set at the specified position
   */
  public void updatePixel(int idx1, int idx2, PixelInterface newPixelValue) {
    this.image[idx1][idx2] = newPixelValue;
  }

  @Override
  public PixelInterface getPixel(int idx1, int idx2) {
    return this.image[idx1][idx2];
  }

  @Override
  public int getWidth() {
    return this.image[0].length;
  }

  @Override
  public int getHeight() {
    return this.image.length;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    ImageInterface imageObj = (Image) obj;

    if (this.getHeight() != imageObj.getHeight() || this.getWidth() != imageObj.getWidth()) {
      return false;
    }

    for (int y = 0; y < this.getHeight(); y++) {
      for (int x = 0; x < this.getWidth(); x++) {
        if (!this.getPixel(y, x).equals(imageObj.getPixel(y, x))) {
          return false;
        }
      }
    }

    return true;
  }

  @Override
  public int hashCode() {
    return java.util.Arrays.deepHashCode(image);
  }


}