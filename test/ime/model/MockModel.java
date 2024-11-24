package ime.model;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static ime.controller.ControllerTestConstants.FETCHED_IMAGE_MATRICES;

/**
 * A mock implementation of the used for testing purposes.
 * This class records method invocations into a provided log and simulates the behavior
 * of an image processing model without performing actual image manipulations.
 *
 * <p>This class is primarily used to verify the correct interactions with image
 * processing methods by storing the parameters passed to each method.</p>
 */
public class MockModel implements ModelInterface {

  private final List<String> log;  // List for recording method invocations
  boolean throwExceptionOperationsFactoryCall;

  /**
   * Constructs a new instance of {@code MockModel}.
   *
   * @param log A list where method invocations will be logged as strings.
   */
  public MockModel(List<String> log, boolean throwExceptionOperationsFactoryCall) {
    this.log = log;
    this.throwExceptionOperationsFactoryCall = throwExceptionOperationsFactoryCall;
  }

  @Override
  public void operationsFactoryCall(String commandName, List<String> commandTokens,
                                    ModelInterface model) {
    String commandTokensStr = commandTokens != null ? commandTokens.toString() : "null";
    String output = "commandFactoryCall(" + commandName + ", " + commandTokensStr + ")";
    if (output.contains("null")) {
      throw new RuntimeException("No image loaded");
    }

    if (throwExceptionOperationsFactoryCall) {
      throw new RuntimeException("Exception trying to process: " + output);
    }
    log.add(output);
  }


  /**
   * Simulates converting and storing an image's pixel data. Logs the method invocation
   * with the image name and pixel array details. Orders output in red,green,blue,alpha order.
   *
   * @param imageName   The name of the image being processed.
   * @param imgPixelArr A map where the key is a component name (e.g., "red", "green", "blue"),
   *                    and the value is a 2D array representing pixel values for that component.
   */
  @Override
  public void convertAndStoreImage(String imageName, Map<String, int[][]> imgPixelArr) {
    String[] componentOrder = {"red", "green", "blue", "alpha"};
    StringBuilder imgPixelArrString = new StringBuilder("{");
    for (String component : componentOrder) {
      if (imgPixelArr.containsKey(component)) {
        int[][] pixelArray = imgPixelArr.get(component);
        String pixelArrayString = Arrays.deepToString(pixelArray);
        imgPixelArrString.append(component).append("=")
                .append(pixelArrayString).append(", ");
      }
    }

    if (imgPixelArrString.length() > 1) {
      imgPixelArrString.setLength(imgPixelArrString.length() - 2);
    }
    imgPixelArrString.append("}");
    log.add("convertAndStoreImage(" + imageName + ", " + imgPixelArrString + ")");
  }

  /**
   * Simulates fetching the pixel data for an image. Logs the method invocation
   * with the image name and returns a predefined image matrix.
   *
   * @param imageName The name of the image to fetch.
   * @return A map of image components to their corresponding pixel data.
   */
  @Override
  public Map<String, int[][]> convertAndFetchImage(String imageName) {
    log.add("convertAndFetchImage(" + imageName + ")");
    return FETCHED_IMAGE_MATRICES;
  }

  @Override
  public ImageInterface getImage(String imageName) {
    log.add("getImage(" + imageName + ")");
    return null;
  }


  /**
   * Simulates storing an {@link ImageInterface} object under a given image name.
   * This method does nothing in this mock implementation.
   *
   * @param imageName The name to associate with the stored image.
   * @param image     The image object to store.
   */
  @Override
  public void storeImage(String imageName, ImageInterface image) {
    log.add("storeImage(" + imageName + ", " + image + ")");
  }


}
