package ime.model;

import java.util.List;
import java.util.Map;

/**
 * The ModelInterface defines the contract for the model component in the image processing
 * application.
 * It provides methods for image operations, storage, retrieval, and conversion between different
 * image representations.
 */
public interface ModelInterface {

  /**
   * Executes an image operation based on the provided command name and parameters.
   *
   * @param commandName   the name of the operation to be executed
   * @param commandTokens a list of string tokens representing the operation parameters
   * @param model         the model instance on which to perform the operation
   * @throws IllegalArgumentException if the command is not recognized or if the parameters are
   *                                  invalid
   */
  void operationsFactoryCall(String commandName, List<String> commandTokens, ModelInterface model);

  /**
   * Converts a pixel map representation of an image to an internal image format and stores it.
   *
   * @param imageName   the name under which to store the converted image
   * @param imgPixelArr a map containing pixel data arrays for red, green, blue, and alpha channels
   * @throws IllegalArgumentException if the pixel arrays are null, empty, or of inconsistent
   *                                  dimensions
   */
  void convertAndStoreImage(String imageName, Map<String, int[][]> imgPixelArr);

  /**
   * Retrieves a stored image and converts it to a pixel map representation.
   *
   * @param imageName the name of the stored image to retrieve and convert
   * @return a map containing pixel data arrays for red, green, blue, and alpha channels
   * @throws IllegalArgumentException if no image is found with the given name
   */
  Map<String, int[][]> convertAndFetchImage(String imageName);

  /**
   * Retrieves a stored image by its name.
   *
   * @param imageName the name of the image to retrieve
   * @return the ImageInterface object representing the stored image
   * @throws IllegalArgumentException if no image is found with the given name
   */
  ImageInterface getImage(String imageName);

  /**
   * Stores an image under a specified name.
   *
   * @param imageName the name under which to store the image
   * @param image     the ImageInterface object to be stored
   * @throws IllegalArgumentException if the image is null or if the name is already in use
   */
  void storeImage(String imageName, ImageInterface image);
}