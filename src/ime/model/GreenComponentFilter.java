package ime.model;

import java.util.List;

/**
 * The GreenComponent class represents a filter that extracts the green component of an image.
 * It extends the AbstractFilters class and implements the execute method to perform
 * the green component extraction operation.
 */
public class GreenComponentFilter extends AbstractFilters {
  private final ModelInterface model;
  private final ImageTransformation imageTransformation;

  /**
   * Constructs a GreenComponent filter with the given model.
   *
   * @param model the ImageInterface model to be used for image operations
   */
  public GreenComponentFilter(ModelInterface model) {
    this.model = model;
    this.imageTransformation = new ImageTransformation();
  }

  @Override
  public void execute(List<String> commandTokens) {
    /* green-component image-name dest-image-name */
    String imageName = commandTokens.get(1);
    String outputImageName = commandTokens.get(2);

    ImageInterface originalImage = model.getImage(imageName);
    ImageInterface greenComponent = imageTransformation.applyTransformation(originalImage,
        (red, green, blue, alpha) -> new Pixel(green, green, green, alpha), 100);
    model.storeImage(outputImageName, greenComponent);

  }
}
