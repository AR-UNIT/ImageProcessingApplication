package ime.model;

import java.util.List;

/**
 * The RedComponent class represents a filter that extracts the red component of an image.
 * It extends the AbstractFilters class and implements the execute method to perform
 * the red component extraction operation.
 */
public class RedComponentFilter extends AbstractFilters {
  private final ModelInterface model;
  private final ImageTransformation transformation;

  /**
   * Constructs a RedComponent filter with the given model.
   *
   * @param model the ImageInterface model to be used for image operations
   */
  public RedComponentFilter(ModelInterface model) {
    this.model = model;
    this.transformation = new ImageTransformation();
  }

  @Override
  public void execute(List<String> commandTokens) {
    /* red-component image-name dest-image-name */
    String imageName = commandTokens.get(1);
    String imageOutputRedName = commandTokens.get(2);

    ImageInterface originalImage = model.getImage(imageName);
    ImageInterface redComponent = transformation.applyTransformation(originalImage,
        (red, green, blue, alpha) -> new Pixel(red, red, red, alpha), 100);
    model.storeImage(imageOutputRedName, redComponent);

  }
}
