package ime.model;

/**
 * Represents a pixel in an image with red, green, blue, and alpha components.
 * This class implements the PixelInterface and provides methods to access
 * individual color components of a pixel. It also overrides the equals and
 * hashCode methods for proper comparison and hashing based on pixel values.
 */
class Pixel implements PixelInterface {
  private final int R;
  private final int G;
  private final int B;
  private final int A;

  /**
   * Constructs a Pixel with specified red, green, blue, and alpha values.
   *
   * @param red   the red component of the pixel
   * @param green the green component of the pixel
   * @param blue  the blue component of the pixel
   * @param alpha the alpha (transparency) component of the pixel
   */
  Pixel(int red, int green, int blue, int alpha) {
    this.R = red;
    this.G = green;
    this.B = blue;
    this.A = alpha;
  }

  @Override
  public int getR() {
    return this.R;
  }

  @Override
  public int getG() {
    return this.G;
  }

  @Override
  public int getB() {
    return this.B;
  }

  @Override
  public int getA() {
    return this.A;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Pixel pixel = (Pixel) obj;
    return R == pixel.R && G == pixel.G && B == pixel.B && A == pixel.A;
  }

  @Override
  public int hashCode() {
    return java.util.Objects.hash(R, G, B, A);
  }

}
