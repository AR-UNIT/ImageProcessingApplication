package ime.model;

import java.util.List;

import static java.lang.Integer.parseInt;

/**
 * The {@code Value} filter class calculates the value component of an image, which is the
 * maximum intensity among the red, green, and blue channels. It sets this maximum value to
 * the red channel for each pixel while setting green and blue channels to zero.
 * This transformation can be applied to a specified percentage of pixels, as defined by the
 * {@code splitPercent} parameter.
 */
public class ValueFilter extends AbstractFilters {
  private final ModelInterface model;
  private final ImageTransformation imageTransformation;

  /**
   * Constructs a {@code Value} filter with the provided model.
   *
   * @param model The {@link ModelInterface} instance for image access and storage.
   */
  public ValueFilter(ModelInterface model) {
    this.model = model;
    this.imageTransformation = new ImageTransformation();
  }

  @Override
  public void execute(List<String> commandTokens) {
    String imageName = commandTokens.get(1);
    String destName = commandTokens.get(2);
    int splitPercent = 100;
    if (commandTokens.size() > 3 && commandTokens.get(3).equals("split")) {
      splitPercent = parseInt(commandTokens.get(4));
    }
    ImageInterface originalImage = model.getImage(imageName);
    ImageInterface valueComponent = imageTransformation.applyTransformation(originalImage,
        (red, green, blue, alpha) -> {
          int value = Math.max(Math.max(red, green), blue);
          return new Pixel(value, value, value, alpha);
        }, splitPercent);
    model.storeImage(destName, valueComponent);

  }
}
