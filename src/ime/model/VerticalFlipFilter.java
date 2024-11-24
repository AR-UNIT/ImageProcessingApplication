package ime.model;

import java.util.List;

/**
 * The {@code VerticalFlip} filter class performs a vertical flip on an image,
 * reversing the order of rows from top to bottom.
 * The flipped image is stored under a specified destination name.
 */
public class VerticalFlipFilter extends AbstractFilters {
  private final ModelInterface model;
  private final ImageTransformation imageTransformation;

  /**
   * Constructs a {@code VerticalFlip} filter with the provided model.
   *
   * @param model The {@link ImageInterface} instance for image access and storage.
   */
  public VerticalFlipFilter(ModelInterface model) {
    this.model = model;
    this.imageTransformation = new ImageTransformation();
  }

  @Override
  public void execute(List<String> commandTokens) {
    /* vertical-flip koala koala-vertical */
    String imageName = commandTokens.get(1);
    String destName = commandTokens.get(2);

    ImageInterface originalImage = model.getImage(imageName);
    int height = originalImage.getHeight();
    ImageInterface verticallyFlippedImage = imageTransformation.applyPixelSwapping(originalImage,
        (x, y, img) -> img.getPixel(height - 1 - y, x));

    model.storeImage(destName, verticallyFlippedImage);

  }
}
