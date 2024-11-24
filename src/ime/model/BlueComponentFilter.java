package ime.model;

import java.util.List;

/**
 * The BlueComponent class is a filter that extracts the blue component of an image.
 * It extends the AbstractFilters class and implements the execute method to perform
 * the blue component extraction.
 */
public class BlueComponentFilter extends AbstractFilters {
  private final ModelInterface model;
  private final ImageTransformation transformation;

  /**
   * Constructs a BlueComponent filter with the given model.
   *
   * @param model the ImageInterface model to be used for image operations
   */
  public BlueComponentFilter(ModelInterface model) {
    this.model = model;
    this.transformation = new ImageTransformation();
  }

  @Override
  public void execute(List<String> commandTokens) {
    /* blue-component image-name dest-image-name */
    String imageName = commandTokens.get(1);
    String outputImageName = commandTokens.get(2);

    ImageInterface originalImage = model.getImage(imageName);
    ImageInterface blueComponent = transformation.applyTransformation(originalImage,
        (red, green, blue, alpha) -> new Pixel(blue, blue, blue, alpha), 100);
    model.storeImage(outputImageName, blueComponent);

  }
}
