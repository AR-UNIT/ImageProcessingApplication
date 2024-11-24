package ime.model;

import java.util.List;

/**
 * The RGBCombine class represents a filter that combines separate red, green, and blue
 * component images into a single RGB image.
 * It extends the AbstractFilters class and implements the execute method to perform
 * the RGB combination operation.
 */
public class RGBCombineFilter extends AbstractFilters {
  private final ModelInterface model;

  /**
   * Constructs an RGBCombine filter with the given model.
   *
   * @param model the ImageInterface model to be used for image operations
   */
  public RGBCombineFilter(ModelInterface model) {
    this.model = model;

  }

  @Override
  public void execute(List<String> commandTokens) {
    String redImageName = commandTokens.get(2);
    String greenImageName = commandTokens.get(3);
    String blueImageName = commandTokens.get(4);

    ImageInterface redImage = model.getImage(redImageName);
    ImageInterface greenImage = model.getImage(greenImageName);
    ImageInterface blueImage = model.getImage(blueImageName);

    int height = redImage.getHeight();
    int width = redImage.getWidth();
    ImageInterface resultImage = new Image(height, width);
    String destImageName = commandTokens.get(1);
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        Pixel updatedPixel = new Pixel(redImage.getPixel(i, j).getR(),
                greenImage.getPixel(i, j).getG(),
                blueImage.getPixel(i, j).getB(), redImage.getPixel(i, j).getA());
        resultImage.updatePixel(i, j, updatedPixel);
      }
    }
    model.storeImage(destImageName, resultImage);

  }
}
