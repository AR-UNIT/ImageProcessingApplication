package ime.model;

import java.util.List;

import static java.lang.Integer.parseInt;

/**
 * The Intensity class represents a filter that extracts the intensity component of an image.
 * It extends the AbstractFilters class and implements the execute method to perform
 * the intensity component extraction operation.
 */
public class IntensityFilter extends AbstractFilters {
  private final ModelInterface model;
  private final ImageTransformation transformation;

  /**
   * Constructs an Intensity filter with the given model.
   *
   * @param model the ImageInterface model to be used for image operations
   */
  public IntensityFilter(ModelInterface model) {
    this.model = model;
    this.transformation = new ImageTransformation();
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
    ImageInterface intensityComponent = transformation.applyTransformation(originalImage,
        (red, green, blue, alpha) -> {
          int intensity = (red + green + blue) / 3;
          return new Pixel(intensity, intensity, intensity, alpha);
        }, splitPercent);
    model.storeImage(destName, intensityComponent);

  }
}
