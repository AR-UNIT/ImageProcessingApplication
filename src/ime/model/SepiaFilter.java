package ime.model;

import java.util.List;


/**
 * The Sepia class represents a filter that applies a sepia tone effect to an image.
 * It extends the AbstractFilters class and implements the execute method to perform
 * the sepia transformation operation.
 */
public class SepiaFilter extends AbstractFilters {
  private final ModelInterface model;
  private final ImageTransformation transformation;

  /**
   * Constructs a Sepia filter with the given model.
   *
   * @param model the ImageInterface model to be used for image operations
   */
  public SepiaFilter(ModelInterface model) {
    this.model = model;
    this.transformation = new ImageTransformation();
  }

  @Override
  public void execute(List<String> commandTokens) {
    /* sepia image-name dest-image-name */
    String originalImageName = commandTokens.get(1);
    String destImageName = commandTokens.get(2);

    int splitPercent = extractSplitPercent(commandTokens);

    double[][] sepiaMatrix = {
            {0.393, 0.769, 0.189},
            {0.349, 0.686, 0.168},
            {0.272, 0.534, 0.131}
    };
    ImageInterface originalImage = model.getImage(originalImageName);
    PixelTransformation sepiaTransformation
            = transformation.createTransformation(sepiaMatrix);
    ImageInterface sepiaImage = transformation.applyTransformation(originalImage,
            sepiaTransformation, splitPercent);
    model.storeImage(destImageName, sepiaImage);

  }
}
