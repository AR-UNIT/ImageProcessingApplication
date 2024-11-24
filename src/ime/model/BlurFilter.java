package ime.model;

import java.util.List;

/**
 * The Blur class represents a filter that applies a blur effect to an image.
 * It extends the AbstractFilters class and implements the execute method to perform
 * the blur operation.
 */
public class BlurFilter extends AbstractFilters {
  private final ModelInterface model;
  private final ImageTransformation transformation;

  /**
   * Constructs a Blur filter with the given model.
   *
   * @param model the ImageInterface model to be used for image operations
   */
  public BlurFilter(ModelInterface model) {
    this.model = model;
    this.transformation = new ImageTransformation();
  }

  @Override
  public void execute(List<String> commandTokens) throws IllegalArgumentException {
    String img = commandTokens.get(1);
    String destImg = commandTokens.get(2);
    ImageInterface originalImage = model.getImage(img);

    int splitPercent = extractSplitPercent(commandTokens);

    KernelOperation blurKernel = () -> new float[][]{
            {1 / 16f, 2 / 16f, 1 / 16f},
            {2 / 16f, 4 / 16f, 2 / 16f},
            {1 / 16f, 2 / 16f, 1 / 16f}
    };

    ImageInterface blurredPixels = transformation.applyKernel(originalImage,
            blurKernel, splitPercent);
    model.storeImage(destImg, blurredPixels);

  }
}
