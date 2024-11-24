package ime.model;

import java.util.List;


/**
 * The LevelsAdjust class represents a filter that adjusts the levels of an image.
 * It extends the AbstractFilters class and implements the execute method to perform
 * the levels adjustment operation.
 */
public class LevelsAdjustFilter extends AbstractFilters {
  private final ModelInterface model;
  private final ImageTransformation imageTransformation;

  /**
   * Constructs a LevelsAdjust filter with the given model.
   *
   * @param model the ImageInterface model to be used for image operations
   */
  public LevelsAdjustFilter(ModelInterface model) {
    this.model = model;
    this.imageTransformation = new ImageTransformation();
  }

  @Override
  public void execute(List<String> commandTokens) {
    int b = Integer.parseInt(commandTokens.get(1)); // Black point
    int m = Integer.parseInt(commandTokens.get(2)); // Midtone point
    int w = Integer.parseInt(commandTokens.get(3)); // White point
    if (b < 0 || b >= m || m >= w || w > 255) {
      throw new IllegalArgumentException("Invalid levels adjustment values. " +
              "Ensure 0 <= b < m < w <= 255.");
    }
    String imageName = commandTokens.get(4);        // Source image name
    String destImageName = commandTokens.get(5);    // Destination image name

    int splitPercent = extractSplitPercent(commandTokens);
    // Fetch original image
    ImageInterface originalImage = model.getImage(imageName);

    // Step 1: Compute intermediate values for Aa, Ab, Ac
    double aA = -b * (128 - 255) + 128 * w - 255 * m;
    double ab = b * b * (128 - 255) + 255 * m * m - 128 * w * w;
    double ac = b * b * (255 * m - 128 * w) - b * (255 * m * m - 128 * w * w);

    // Step 2: Compute denominator A
    double a = b * b * (m - w) + w * m * m - m * w * w;

    // Step 3: Compute coefficients a, b, c for the quadratic curve
    double aCoeff = aA / a;
    double bCoeff = ab / a;
    double cCoeff = ac / a;

    // Apply levels adjustment to each pixel in the image using the computed quadratic curve
    ImageInterface adjustedImage = imageTransformation.applyLevelsAdjustment(originalImage, aCoeff,
            bCoeff, cCoeff, splitPercent);

    // Store adjusted image
    model.storeImage(destImageName, adjustedImage);
  }
}
