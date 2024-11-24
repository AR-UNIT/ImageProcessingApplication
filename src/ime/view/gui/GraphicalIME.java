package ime.view.gui;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JTextField;

import ime.controller.gui.GUIControllerInterface;
import ime.controller.gui.GUIFeaturesInterface;

/**
 * The GraphicalIME class represents the main graphical user interface for the image processing
 * application.
 * It extends JFrame and implements GraphicalIMEInterface to provide a window-based GUI for image
 * manipulation.
 */
public class GraphicalIME extends JFrame implements GraphicalIMEInterface {
  private ImageDisplayPanel imageDisplayPanel;
  private HistogramPanel histogramPanel;


  /**
   * Initializes the main frame of the application, setting up the layout and adding components.
   *
   * @param guiController Controller object
   */
  private void initializeFrame(GUIFeaturesInterface guiController) {
    setTitle("Image Processing Application");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(1000, 600);
    setLayout(new BorderLayout());

    NavigationPanel navigationPanel = new NavigationPanel(guiController::previousVersion,
            guiController::nextVersion);
    imageDisplayPanel = new ImageDisplayPanel();
    histogramPanel = new HistogramPanel();
    ButtonPanel buttonPanel = new ButtonPanel(createOperationsMap(guiController));

    add(navigationPanel, BorderLayout.NORTH);
    add(imageDisplayPanel, BorderLayout.CENTER);
    add(histogramPanel, BorderLayout.SOUTH);
    add(buttonPanel, BorderLayout.WEST);
  }

  /**
   * Creates and returns a map of operation names to their corresponding Runnable implementations.
   *
   * @param guiController GUI controller object
   * @return A Map containing operation names as keys and Runnable objects as values.
   */
  private Map<String, Runnable> createOperationsMap(GUIFeaturesInterface guiController) {
    Map<String, Runnable> operations = new HashMap<>();

    operations.put("Load Image", guiController::loadImage);
    operations.put("Color Correction", guiController::colorCorrection);
    operations.put("Levels Adjustment", guiController::levelsAdjust);
    operations.put("Blur", guiController::blur);
    operations.put("Sharpen", guiController::sharpen);
    operations.put("Value Component", guiController::valueComp);
    operations.put("Luma Component", guiController::lumaComp);
    operations.put("Intensity Component", guiController::intensityComp);
    operations.put("Red Component", guiController::redComp);
    operations.put("Blue Component", guiController::blueComp);
    operations.put("Green Component", guiController::greenComp);
    operations.put("Vertical Flip", guiController::verticalFlip);
    operations.put("Horizontal Flip", guiController::horizontalFlip);
    operations.put("Sepia Tone", guiController::sepia);
    operations.put("Compress", guiController::compress);
    operations.put("Brighten", guiController::brighten);
    operations.put("Downscale", guiController::downscale);
    operations.put("Save",
        () -> {
          try {
            guiController.save();
          } catch (IOException e) {
            this.displayError("Error saving image: " + e.getMessage());
          }
        });

    return operations;
  }


  /**
   * Makes the frame visible to display the GUI.
   *
   * @param guiController GUI controller object
   */
  public void view(GUIFeaturesInterface guiController) {
    // Set the frame visibility
    setVisible(true);
    initializeFrame(guiController);
  }

  /**
   * Displays a dialog to choose between split view and direct application of a filter.
   *
   * @param controller GUIFeatures controller object
   * @param filterName The name of the filter to be applied.
   * @param args       The list of arguments for the filter operation.
   * @param img        The destination image name.
   */
  public void splitView(GUIFeaturesInterface controller,
                        String filterName, List<String> args, String img) {
    int choice = JOptionPane.showConfirmDialog(
            this,
            "Do you want the split view of " + filterName + " filter?",
            "Split View",
            JOptionPane.YES_NO_OPTION
    );

    if (choice == JOptionPane.YES_OPTION) {
      previewFilter(controller, filterName, args, img);
    } else {
      controller.applyFilter(args.get(0), args);
    }
  }

  /**
   * Displays a preview of the filter application with a split view.
   *
   * @param control    GUI Controller object
   * @param filterName The name of the filter being previewed.
   * @param args       The list of arguments for the filter operation.
   * @param img        The destination image name.
   */
  private void previewFilter(GUIFeaturesInterface control,
                             String filterName, List<String> args, String img) {
    JDialog splitViewDialog = new JDialog(this,
            "Split View: " + filterName, true);
    splitViewDialog.setLayout(new BorderLayout());
    splitViewDialog.setSize(800, 600);
    JLabel splitViewLabel = new JLabel();
    JScrollPane scrollPane = new JScrollPane(splitViewLabel);
    splitViewDialog.add(scrollPane, BorderLayout.CENTER);

    JPanel splitViewPanel = new JPanel();
    JTextField splitPercentageField = new JTextField(5);
    JButton applyButton = new JButton("Apply");
    JButton cancelButton = new JButton("Cancel");
    JButton updatePreviewButton = new JButton("Update Preview");
    splitViewPanel.add(new JLabel("Split %:"));
    splitViewPanel.add(splitPercentageField);
    splitViewPanel.add(updatePreviewButton);
    splitViewPanel.add(applyButton);
    splitViewPanel.add(cancelButton);
    splitViewDialog.add(splitViewPanel, BorderLayout.SOUTH);

    updatePreviewButton.addActionListener(e -> {
      String splitPercentage = splitPercentageField.getText();
      if (isValidPercentage(splitPercentage)) {
        try {
          List<String> splitArgs = new ArrayList<>(args);
          splitArgs.set(splitArgs.size() - 1, img + "-split");
          splitArgs.add("split");
          splitArgs.add(splitPercentage);

          control.splitViewFilter(filterName, splitArgs, img);
          updateSplitViewImage(control, splitViewLabel, img + "-split");
        } catch (IOException ex) {
          JOptionPane.showMessageDialog(splitViewDialog,
                  "Error previewing filter: " + ex.getMessage());
        }
      } else {
        JOptionPane.showMessageDialog(splitViewDialog,
                "Invalid percentage. Please enter a number between 0 and 100.");
      }
    });

    applyButton.addActionListener(e -> {
      control.applyFilter(args.get(0), args);
      splitViewDialog.dispose();
    });

    cancelButton.addActionListener(e -> splitViewDialog.dispose());

    splitViewDialog.setVisible(true);
  }

  /**
   * Validates if the given string represents a valid percentage (0-100).
   *
   * @param percentage The string to be validated.
   * @return true if the string is a valid percentage, false otherwise.
   */
  private boolean isValidPercentage(String percentage) {
    try {
      int value = Integer.parseInt(percentage);
      return value >= 0 && value <= 100;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  /**
   * Updates the split view image display.
   *
   * @param control        GUI Controller object
   * @param splitViewLabel The JLabel to update with the new image.
   * @param imgName        The name of the image to display.
   * @throws IOException If there's an error reading the image.
   */
  private void updateSplitViewImage(GUIFeaturesInterface control,
                                    JLabel splitViewLabel, String imgName) throws IOException {
    BufferedImage img = control.getCurrentImage(imgName);
    if (img != null) {
      ImageIcon icon = new ImageIcon(img);
      splitViewLabel.setIcon(icon);
      splitViewLabel.revalidate();
      splitViewLabel.repaint();
    } else {
      JOptionPane.showMessageDialog(this,
              "Error displaying preview image: Image not found");
    }
  }

  /**
   * Method to update the histogram on bottom of the screen.
   *
   * @param img image whose histogram needs update.
   */
  public void updateHistogram(BufferedImage img) {
    histogramPanel.updateHistogram(img);
  }


  /**
   * Displays a dialog offering to save the current image.
   *
   * @param control       Controller object.
   * @param imageName     image to be saved.
   * @param fileExtension file extension of the image file to be saved.
   */
  public void offerToSaveImage(GUIControllerInterface control, String imageName,
                               String fileExtension) throws IOException {
    int choice = JOptionPane.showConfirmDialog(
            GraphicalIME.this,
            "Do you want to save the image?",
            "Save Image",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
    );

    if (choice == JOptionPane.YES_OPTION) {
      saveWithFileChooser(control, imageName, fileExtension);
    }
  }

  /**
   * Method to offer prompt to users to input values during some filters.
   *
   * @param promptMessage This is the prompt message which is shown to users
   * @return String
   */
  public String offerPromptToGetOperationParameters(String promptMessage) {
    String promptOutput = JOptionPane.showInputDialog(this,
            promptMessage);
    if (promptOutput == null || promptOutput.isEmpty()) {
      return null;
    }
    return promptOutput;
  }

  /**
   * Method to save file at user's choice of location.
   *
   * @param control       Controller object
   * @param imageName     image to be saved
   * @param fileExtension file extension of the image to be saved
   */
  public String saveWithFileChooser(GUIControllerInterface control, String imageName,
                                    String fileExtension) throws IOException {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Save Image");
    fileChooser.setSelectedFile(new File(imageName + "." + fileExtension));

    int userSelection = fileChooser.showSaveDialog(GraphicalIME.this);

    if (userSelection == JFileChooser.APPROVE_OPTION) {
      File fileToSave = fileChooser.getSelectedFile();
      // Ensure the file has the correct extension
      String savePath = fileToSave.getAbsolutePath();
      if (!savePath.endsWith("." + fileExtension)) {
        savePath += "." + fileExtension;
      }

      control.saveImage(savePath, imageName, fileExtension);
      return savePath;
    }
    return null; // Return null if the user cancels the save dialog
  }


  /**
   * Loads an image file selected by the user.
   */
  public File loadImage() {
    JFileChooser fileChooser = new JFileChooser();
    int result = fileChooser.showOpenDialog(this);
    File selectedFile = null;
    if (result == JFileChooser.APPROVE_OPTION) {
      selectedFile = fileChooser.getSelectedFile();

    }
    return selectedFile;
  }


  /**
   * Updates the main image display with the current version of the image.
   *
   * @param img BufferedImage that needs to be displayed
   */
  public void updateImageDisplay(BufferedImage img) {
    try {
      if (img != null) {
        imageDisplayPanel.updateImage(img);
      } else {
        this.displayError("Error: Image not found");
      }
    } catch (Exception ex) {
      this.displayError(
              "Error displaying image: " + ex.getMessage());
    }
  }


  @Override
  public void displayError(String error) {
    JOptionPane.showMessageDialog(this, error,
            "Error", JOptionPane.ERROR_MESSAGE);
  }

  @Override
  public void displayMessage(String message) {
    JOptionPane.showMessageDialog(this, message);
  }

}