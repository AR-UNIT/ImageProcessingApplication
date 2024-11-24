package ime.view.gui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import ime.controller.gui.GUIControllerInterface;
import ime.controller.gui.GUIFeaturesInterface;
import ime.view.ViewInterface;

/**
 * The {@code GraphicalIMEInterface} extends the {@code ViewInterface} to provide
 * additional functionalities specific to the graphical user interface (GUI) of
 * the image manipulation application.
 */
public interface GraphicalIMEInterface extends ViewInterface {

  /**
   * Displays the graphical user interface.
   * This method initializes and makes the GUI visible to the user.
   *
   * @param guiControl the {@code GUIFeaturesInterface} controller to handle user actions and
   *                   interactions
   */
  void view(GUIFeaturesInterface guiControl);

  /**
   * Displays a split view of the original image alongside an image with the applied filter.
   *
   * @param controller the {@code GUIFeaturesInterface} controller to manage the filtering operation
   * @param filterName the name of the filter to apply
   * @param args       the arguments required for the filter operation
   * @param destImg    the name of the destination image where the filter result will be displayed
   */
  void splitView(GUIFeaturesInterface controller,
                 String filterName, List<String> args, String destImg);

  /**
   * Updates the displayed image in the GUI with the specified {@code BufferedImage}.
   *
   * @param img the {@code BufferedImage} to display
   */
  void updateImageDisplay(BufferedImage img);

  /**
   * Updates the histogram display in the GUI based on the specified {@code BufferedImage}.
   *
   * @param img the {@code BufferedImage} from which the histogram is generated
   */
  void updateHistogram(BufferedImage img);

  /**
   * Opens a file chooser dialog to allow the user to select an image file to load.
   *
   * @return a {@code File} representing the selected image file
   */
  File loadImage();

  /**
   * Offers the user the option to save the current image through the GUI.
   *
   * @param control       the {@code GUIControllerInterface} to handle the save operation
   * @param imageName     the name of the image to save
   * @param fileExtension the desired file extension (e.g., "jpg", "png")
   */
  void offerToSaveImage(GUIControllerInterface control, String imageName,
                        String fileExtension) throws IOException;

  /**
   * Saves the current image using a file chooser dialog to allow the user to specify the save
   * location.
   *
   * @param control       the {@code GUIControllerInterface} to handle the save operation
   * @param imageName     the name of the image to save
   * @param fileExtension the desired file extension (e.g., "jpg", "png")
   */
  String saveWithFileChooser(GUIControllerInterface control,
                             String imageName, String fileExtension) throws IOException;


  String offerPromptToGetOperationParameters(String promptMessage);
}
