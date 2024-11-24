package ime.model;

import java.util.List;

/**
 * The HorizontalFlip class represents a filter that horizontally flips an image.
 * It extends the AbstractFilters class and implements the execute method to perform
 * the horizontal flip operation.
 */
public class HorizontalFlipFilter extends AbstractFilters {
  private final ModelInterface model;
  private final ImageTransformation imageTransformation;

  /**
   * Constructs a HorizontalFlip filter with the given model.
   *
   * @param model the ImageInterface model to be used for image operations
   */
  public HorizontalFlipFilter(ModelInterface model) {
    this.model = model;
    this.imageTransformation = new ImageTransformation();
  }

  @Override
  public void execute(List<String> commandTokens) {
    /* horizontal-flip koala koala-horizontal */
    String imageName = commandTokens.get(1);
    String destName = commandTokens.get(2);

    ImageInterface originalImage = model.getImage(imageName);
    int width = originalImage.getWidth();

    ImageInterface horizontallyFlippedImage = imageTransformation.applyPixelSwapping(originalImage,
        (x, y, img) -> img.getPixel(y, width - 1 - x));

    model.storeImage(destName, horizontallyFlippedImage);

  }
}
