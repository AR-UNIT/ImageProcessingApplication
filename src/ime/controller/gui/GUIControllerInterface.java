package ime.controller.gui;

import java.io.IOException;

/**
 * The GUIControllerInterface defines the contract for the GUI controller in the image processing
 * application.
 * It provides methods for loading, saving, and manipulating images, as well as applying filters.
 */
public interface GUIControllerInterface {

  /**
   * Saves the current image to a file.
   *
   * @param filePath         The path where the image should be saved.
   * @param currentImageName The name of the image to be saved.
   * @param fileExtension    The file extension to be used for the saved image.
   * @throws IOException If there's an error saving the image.
   */
  void saveImage(String filePath, String currentImageName, String fileExtension)
          throws IOException;

  /**
   * Starts the controller, enabling the GUI to interact with the user.
   */
  void startMethod();

}
