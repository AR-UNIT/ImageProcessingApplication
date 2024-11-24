package ime.model;

import java.util.List;

import static java.lang.Integer.parseInt;

/**
 * The Brighten class represents a filter that adjusts the brightness of an image.
 * It extends the AbstractFilters class and implements the execute method to perform
 * the brightness adjustment operation.
 */
public class BrightenFilter extends AbstractFilters {
  private final ModelInterface model;
  private final ImageTransformation transformation;

  /**
   * Constructs a Brighten filter with the given model.
   *
   * @param model the ImageInterface model to be used for image operations
   */
  public BrightenFilter(ModelInterface model) {
    this.model = model;
    this.transformation = new ImageTransformation();
  }

  @Override
  public void execute(List<String> commandTokens) {
    /* brighten increment image-name dest-image-name */

    int brightenIncrement = parseInt(commandTokens.get(1));
    String imageName = commandTokens.get(2);
    String destName = commandTokens.get(3);

    if (!(brightenIncrement > -255 && brightenIncrement < 255)) {
      throw new IllegalArgumentException("brightenIncrement must be between 0 and 255");
    }

    ImageInterface originalImage = model.getImage(imageName);

    ImageInterface brightenedImage = transformation.applyTransformation(originalImage,
        (red, green, blue, alpha) -> {
          int newRed = red + brightenIncrement;
          int newGreen = green + brightenIncrement;
          int newBlue = blue + brightenIncrement;

          return new Pixel(newRed, newGreen, newBlue, alpha);
        }, 100);

    model.storeImage(destName, brightenedImage);

  }
}
