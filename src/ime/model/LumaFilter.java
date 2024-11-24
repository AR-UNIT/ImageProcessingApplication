package ime.model;

import java.util.List;


/**
 * The Luma class represents a filter that extracts the luma component of an image.
 * It extends the AbstractFilters class and implements the execute method to perform
 * the luma component extraction operation.
 */
public class LumaFilter extends AbstractFilters {
  private final ModelInterface model;
  private final ImageTransformation transformation;

  /**
   * Constructs a Luma filter with the given model.
   *
   * @param model the ImageInterface model to be used for image operations
   */
  public LumaFilter(ModelInterface model) {
    this.model = model;
    this.transformation = new ImageTransformation();
  }

  @Override
  public void execute(List<String> commandTokens) throws IllegalArgumentException {
    String imageName = commandTokens.get(1);
    String destName = commandTokens.get(2);

    int splitPercent = extractSplitPercent(commandTokens);

    ImageInterface originalImage = model.getImage(imageName);
    ImageInterface lumaComponent = transformation.applyTransformation(originalImage,
        (red, green, blue, alpha) -> {
          int luma = Math.round(red * 0.2126f + green * 0.7152f + blue * 0.0722f);
          return new Pixel(luma, luma, luma, alpha);
        }, splitPercent);
    model.storeImage(destName, lumaComponent);

  }
}
