package ime.model;

import java.util.List;

/**
 * The RGBSplit class represents a filter that splits an image into its red, green, and blue
 * components.
 * It extends the AbstractFilters class and implements the execute method to perform
 * the RGB split operation.
 */
public class RGBSplitFilter extends AbstractFilters {
  private final ModelInterface model;
  private final ImageTransformation transformation;

  /**
   * Constructs an RGBSplit filter with the given model.
   *
   * @param model the ImageInterface model to be used for image operations
   */
  public RGBSplitFilter(ModelInterface model) {
    this.model = model;
    this.transformation = new ImageTransformation();
  }

  /**
   * Creates a grayscale version of an image using its red values.
   *
   * @param sourceImageName the name of the source image stored in the model
   * @param destImageName   the name to store the resultant grayscale image
   */
  private void getRedSplit(String sourceImageName, String destImageName) {
    ImageInterface sourceImage = model.getImage(sourceImageName);
    ImageInterface redSplit = transformation.applyTransformation(sourceImage,
        (red, green, blue, alpha) -> new Pixel(red, red, red, alpha), 100);
    model.storeImage(destImageName, redSplit);
  }

  /**
   * Creates a grayscale version of an image using its green values.
   *
   * @param sourceImageName the name of the source image stored in the model
   * @param destImageName   the name to store the resultant grayscale image
   */
  private void getGreenSplit(String sourceImageName, String destImageName) {
    ImageInterface sourceImage = model.getImage(sourceImageName);
    ImageInterface greenSplit = transformation.applyTransformation(sourceImage,
        (red, green, blue, alpha) -> new Pixel(green, green, green, alpha), 100);
    model.storeImage(destImageName, greenSplit);
  }

  /**
   * Creates a grayscale version of an image using its blue values.
   *
   * @param sourceImageName the name of the source image stored in the model
   * @param destImageName   the name to store the resultant grayscale image
   */
  private void getBlueSplit(String sourceImageName, String destImageName) {
    ImageInterface sourceImage = model.getImage(sourceImageName);
    ImageInterface blueSplit = transformation.applyTransformation(sourceImage,
        (red, green, blue, alpha) -> new Pixel(blue, blue, blue, alpha), 100);
    model.storeImage(destImageName, blueSplit);
  }


  @Override
  public void execute(List<String> commandTokens) {
    /* rgb-split image-name dest-image-name-red dest-image-name-green dest-image-name-blue */
    String originalImage = commandTokens.get(1);
    String redSplit = commandTokens.get(2);
    String greenSplit = commandTokens.get(3);
    String blueSplit = commandTokens.get(4);
    this.getRedSplit(originalImage, redSplit);
    this.getGreenSplit(originalImage, greenSplit);
    this.getBlueSplit(originalImage, blueSplit);

  }
}
