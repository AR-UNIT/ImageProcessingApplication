package ime.model;

import java.util.List;


/**
 * The Sharpen class represents a filter that applies a sharpening effect to an image.
 * It extends the AbstractFilters class and implements the execute method to perform
 * the sharpening operation.
 */
public class SharpenFilter extends AbstractFilters {
  private final ModelInterface model;
  private final ImageTransformation transformation;

  /**
   * Constructs a Sharpen filter with the given model.
   *
   * @param model the ImageInterface model to be used for image operations
   */
  public SharpenFilter(ModelInterface model) {
    this.model = model;
    this.transformation = new ImageTransformation();
  }

  @Override
  public void execute(List<String> commandTokens) {
    String img = commandTokens.get(1);
    String destImg = commandTokens.get(2);
    ImageInterface originalImage = model.getImage(img);

    int splitPercent = extractSplitPercent(commandTokens);

    KernelOperation sharpenKernel = () -> new float[][]{
            {-1 / 8f, -1 / 8f, -1 / 8f, -1 / 8f, -1 / 8f},
            {-1 / 8f, 1 / 4f, 1 / 4f, 1 / 4f, -1 / 8f},
            {-1 / 8f, 1 / 4f, 1, 1 / 4f, -1 / 8f},
            {-1 / 8f, 1 / 4f, 1 / 4f, 1 / 4f, -1 / 8f},
            {-1 / 8f, -1 / 8f, -1 / 8f, -1 / 8f, -1 / 8f}};

    ImageInterface sharpenedPixels = transformation.applyKernel(originalImage,
            sharpenKernel, splitPercent);
    model.storeImage(destImg, sharpenedPixels);

  }
}
