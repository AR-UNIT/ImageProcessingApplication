package ime.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.Objects;

import ime.controller.gui.GUIControllerInterface;
import ime.controller.gui.GUIFeaturesInterface;
import ime.view.gui.GraphicalIMEInterface;

/**
 * This is the mock class for the gui view.
 */
public class MockGuiView implements GraphicalIMEInterface {

  private final List<String> log; /* List for recording method invocations */
  File selectedFile;
  String actionResponse;

  /**
   * Constructor for the mock gui view.
   * @param log                   the log file to log function calls tp
   * @param selectedFile          the file to load at view
   * @param actionResponse        the action response to invoke in modelview
   */
  public MockGuiView(List<String> log, File selectedFile, String actionResponse) {
    this.log = log;
    this.selectedFile = selectedFile;
    this.actionResponse = actionResponse;
  }

  @Override
  public void displayMessage(String message) {
    log.add("displayMessage: " + message);
  }

  /**
   * Logs an error message to the provided log list, prefixed with "displayError:".
   *
   * @param error The error message to be displayed.
   */
  @Override
  public void displayError(String error) {
    log.add("displayError: " + error);
  }

  @Override
  public void view(GUIFeaturesInterface guiControl) {
    log.add("view()");
  }

  @Override
  public void splitView(GUIFeaturesInterface controller, String filterName, List<String> args,
                        String destImg) {
    String argsString = args != null ? args.toString() : "null";
    log.add("splitView(" + filterName + ", " + argsString + ", " + destImg + ")");
  }


  @Override
  public void updateImageDisplay(BufferedImage img) {
    log.add("updateImageDisplay: ");
  }


  @Override
  public void updateHistogram(BufferedImage img) {
    return;
  }

  @Override
  public File loadImage() {
    log.add("loadImage()");
    if (Objects.equals(actionResponse, "failLoading")) {
      return null;
    }
    return this.selectedFile;
  }

  @Override
  public void offerToSaveImage(GUIControllerInterface control, String imageName,
                               String fileExtension) {
    log.add("offerToSaveImage(" + imageName + ", " + fileExtension + ")");
  }

  @Override
  public String saveWithFileChooser(GUIControllerInterface control, String imageName,
                                    String fileExtension) {
    log.add("saveWithFileChooser(" + imageName + ", " + fileExtension + ")");
    if (Objects.equals(actionResponse, "failSaving")) {
      throw new RuntimeException("unable to save file");
    }
    return this.selectedFile.getAbsolutePath();
  }

  @Override
  public String offerPromptToGetOperationParameters(String promptMessage) {
    /* possible cases of operationParamsPromptResponse = null-null-null, black-null-null,
     black-mid-null, black-mid-white */
    log.add("offerPromptToGetOperationParameters(" + promptMessage + ")");

    if (Objects.equals(promptMessage, "Enter target width: ")) {
      if (Objects.equals(actionResponse, "null-null")) {
        return null;
      } else {
        return "50";
      }
    }

    if (Objects.equals(promptMessage, "Enter target height: ")) {
      if (Objects.equals(actionResponse, "width-height")) {
        return "50";
      } else {
        return null;
      }
    }

    if (Objects.equals(promptMessage, "Enter black point (0-255):")) {
      if (Objects.equals(actionResponse, "null-null-null")) {
        return null;
      } else {
        return "50";
      }
    }

    if (Objects.equals(promptMessage, "Enter mid point (0-255):")) {
      if (Objects.equals(actionResponse, "black-mid-null") || Objects.equals(actionResponse,
              "black-mid-white")) {
        return "50";
      } else {
        return null;
      }
    }
    if (Objects.equals(promptMessage, "Enter white point (0-255):")) {
      if (Objects.equals(actionResponse, "black-mid-white")) {
        return "50";
      } else {
        return null;
      }
    }

    return this.actionResponse;
  }


}
