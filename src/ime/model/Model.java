package ime.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ime.model.ImageChannelConstants.ALPHA;
import static ime.model.ImageChannelConstants.BLUE;
import static ime.model.ImageChannelConstants.GREEN;
import static ime.model.ImageChannelConstants.RED;

/**
 * The Model class implements the contract for the model component in the image processing
 * application.
 * It implements methods for image operations, storage, retrieval, and conversion between different
 * image representations.
 */
public class Model implements ModelInterface {
  private final Map<String, ImageInterface> storedImages = new HashMap<>();

  @Override
  public void convertAndStoreImage(String imageName, Map<String, int[][]> imgPixelArr) {
    int rows = imgPixelArr.get(RED).length;
    int cols = imgPixelArr.get(RED)[0].length;
    PixelInterface[][] pixelArr = new Pixel[rows][cols];
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        int red = imgPixelArr.get(RED)[i][j];
        int green = imgPixelArr.get(GREEN)[i][j];
        int blue = imgPixelArr.get(BLUE)[i][j];
        int alpha = imgPixelArr.get(ALPHA)[i][j];
        pixelArr[i][j] = new Pixel(red, green, blue, alpha);
      }
    }
    ImageInterface image = new Image(rows, cols);
    image.imageFill(pixelArr);
    this.storeImage(imageName, image);
  }

  @Override
  public Map<String, int[][]> convertAndFetchImage(String imageName) {
    ImageInterface image = this.getImage(imageName);
    // Check if the image was found
    if (image == null) {
      throw new IllegalArgumentException("Image not found: " + imageName);
    }

    int rows = image.getHeight();
    int cols = image.getWidth();

    Map<String, int[][]> imgPixelArr = new HashMap<>();
    try {
      imgPixelArr.put(RED, new int[rows][cols]);
      imgPixelArr.put(GREEN, new int[rows][cols]);
      imgPixelArr.put(BLUE, new int[rows][cols]);
      imgPixelArr.put(ALPHA, new int[rows][cols]);

      for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
          PixelInterface pixel = image.getPixel(i, j);
          if (pixel == null) {
            continue;
          }
          imgPixelArr.get(RED)[i][j] = pixel.getR();
          imgPixelArr.get(GREEN)[i][j] = pixel.getG();
          imgPixelArr.get(BLUE)[i][j] = pixel.getB();
          imgPixelArr.get(ALPHA)[i][j] = pixel.getA();
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return imgPixelArr;
  }

  @Override
  public void operationsFactoryCall(String commandName, List<String> commandTokens,
                                    ModelInterface model) {
    OperationsFactory opFactory = new OperationsFactory(model);
    if (!opFactory.commandExists(commandName)) {
      throw new IllegalArgumentException(commandName + " Command not found");
    }
    opFactory.getCommandFilter(commandName).execute(commandTokens);
  }

  @Override
  public void storeImage(String imageName, ImageInterface image) {
    this.storedImages.put(imageName, image);
  }

  @Override
  public ImageInterface getImage(String imageName) {
    ImageInterface image = this.storedImages.get(imageName);
    if (image == null) {
      throw new IllegalArgumentException("Image not found: " + imageName);
    }
    return image;
  }

}
