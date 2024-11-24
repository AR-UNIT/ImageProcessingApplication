package ime.controller.gui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import ime.CommandConstants;
import ime.controller.ImageReader;
import ime.controller.ImageReaderFactory;
import ime.controller.ImageWriter;
import ime.controller.ImageWriterFactory;
import ime.model.ModelInterface;
import ime.view.DisplayMessageConstants;
import ime.view.gui.GraphicalIMEInterface;

/**
 * The GUIController class serves as the controller in the MVC architecture for the image processing
 * application.
 * It connects the graphical user interface (GUI) with the underlying model, handling user
 * interactions,
 * executing commands, and managing image versions.
 */
public class GUIController implements GUIControllerInterface, GUIFeaturesInterface {
  private final ModelInterface model;
  private final GraphicalIMEInterface gui;
  private static final Set<String> ARGB_TYPES = Set.of("png");
  private static String currentImageName;
  private String fileExtension;
  private final List<String> imageVersions;
  private int versionIndex;

  /**
   * Initializes the GUIController with the specified model and GUI interface.
   *
   * @param model The model interface providing the business logic for image processing.
   * @param gui   The graphical interface for interacting with the user.
   */
  public GUIController(ModelInterface model, GraphicalIMEInterface gui) {
    this.model = model;
    this.gui = gui;
    this.imageVersions = new ArrayList<>();
  }

  @Override
  public void startMethod() { /* tested call */
    this.gui.view(this);
  }

  @Override
  public void loadImage() { /* tested positive case call for ppm,jpg,png,
                               need to test negative case call when image not found */
    File selectedFile = gui.loadImage();
    try {
      currentImageName = selectedFile.getName().trim().split("\\.")[0];
      imageVersions.add(currentImageName);
      versionIndex = imageVersions.size() - 1;
      fileExtension = selectedFile.getPath().trim().split("\\.")[1];
      List<String> list = new ArrayList<>();
      list.add(CommandConstants.LOAD);
      list.add(selectedFile.getPath());
      list.add(currentImageName);
      readImage(list);
      gui.displayMessage("Image loaded successfully.");

      updateImageDisplay();
    } catch (Exception e) {
      gui.displayError(e.getMessage());
    }
  }

  /**
   * Manages versioning of images by storing each modified version and updating the display.
   *
   * @param currentImgName The name of the current image version.
   */
  private void imageVersioning(String currentImgName) {
    currentImageName = currentImgName;
    imageVersions.add(currentImageName);
    versionIndex = imageVersions.size() - 1;
    updateImageDisplay(); // Add this line to update the display
    try {
      gui.offerToSaveImage(this, currentImageName, fileExtension);
    } catch (IOException e) {
      gui.displayMessage("Error saving image: " + e.getMessage());
    }
  }

  /**
   * Updates the GUI display to show the current image version.
   */
  public void updateImageDisplay() {
    try {
      String currentVersion = imageVersions.get(versionIndex);
      BufferedImage img = this.getCurrentImage(currentVersion);
      gui.updateImageDisplay(img);
      if (img != null) {
        updateHistogram(); // Make sure to update the histogram as well
      } else {
        gui.displayMessage("Error: Image not found");
      }
    } catch (Exception ex) {
      gui.displayError("Error displaying image: " + ex.getMessage());
    }
  }


  @Override
  public void saveImage(String filePath, String currentImageName, String fileExtension)
          throws IOException {
    BufferedImage image = getCurrentImage(currentImageName);
    writeImageToFile(image, filePath);
    gui.displayMessage(DisplayMessageConstants.SAVED_TO + filePath);
  }

  /**
   * Reads an image from a file and processes it into a format suitable for the model.
   *
   * @param args A list of command arguments, including file path and image name.
   * @throws IOException If an error occurs while reading the file.
   */
  void readImage(List<String> args) throws IOException {
    String imageFilePath = args.get(1);
    String imageName = args.get(2);
    BufferedImage bufferedImage = loadBufferedImage(imageFilePath);
    Map<String, int[][]> imagePixelArr = convertTo2DPixelArray(bufferedImage);
    model.convertAndStoreImage(imageName, imagePixelArr);
  }

  /**
   * Loads a BufferedImage from the specified file path.
   *
   * @param imageFilePath The path of the image file to load.
   * @return A BufferedImage object representing the image.
   * @throws IOException If the file cannot be read.
   */
  private BufferedImage loadBufferedImage(String imageFilePath) throws IOException {
    String fileType = getFileExtension(imageFilePath);
    ImageReader reader = ImageReaderFactory.getReader(fileType);
    return reader.read(imageFilePath);
  }

  /**
   * Converts a BufferedImage into separate 2D pixel arrays for red, green, blue, and alpha
   * channels.
   *
   * @param bufferedImage The image to convert.
   * @return A map of 2D arrays representing color channels.
   */
  private static Map<String, int[][]> convertTo2DPixelArray(BufferedImage bufferedImage) {
    int width = bufferedImage.getWidth();
    int height = bufferedImage.getHeight();
    int[][] redMatrix = new int[height][width];
    int[][] greenMatrix = new int[height][width];
    int[][] blueMatrix = new int[height][width];
    int[][] alphaMatrix = new int[height][width];

    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        int argb = bufferedImage.getRGB(x, y);
        redMatrix[y][x] = (argb >> 16) & 0xFF;
        greenMatrix[y][x] = (argb >> 8) & 0xFF;
        blueMatrix[y][x] = argb & 0xFF;
        alphaMatrix[y][x] = (argb >> 24) & 0xFF;
      }
    }

    return Map.of("red", redMatrix, "green", greenMatrix, "blue", blueMatrix,
            "alpha", alphaMatrix);
  }

  /**
   * Retrieves the current image as a BufferedImage.
   *
   * @param imageName The name of the image to fetch.
   * @return A BufferedImage of the requested image.
   */
  public BufferedImage getCurrentImage(String imageName) {
    String filename = imageName + "." + fileExtension;
    Map<String, int[][]> matrices = fetchImageMatrices(imageName);

    return createBufferedImage(Objects.requireNonNull(matrices), filename);
  }


  /**
   * Fetches image pixel data from the model for the specified image.
   *
   * @param imageName The name of the image to retrieve.
   * @return A map of 2D arrays representing color channels or null if not found.
   */
  private Map<String, int[][]> fetchImageMatrices(String imageName) {
    try {
      return model.convertAndFetchImage(imageName);
    } catch (Exception e) {
      gui.displayError(e.getMessage());
      return null;
    }
  }

  /**
   * Constructs a BufferedImage from pixel data arrays.
   *
   * @param matrices A map of 2D arrays for red, green, blue, and alpha channels.
   * @param filename The filename to determine image type (ARGB or RGB).
   * @return A BufferedImage created from the pixel data.
   */
  private BufferedImage createBufferedImage(Map<String, int[][]> matrices, String filename) {
    int height = matrices.get("red").length;
    int width = matrices.get("red")[0].length;

    boolean isARGB = ARGB_TYPES.contains(getFileExtension(filename));
    BufferedImage image = new BufferedImage(width, height,
            isARGB ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB);


    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        int r = matrices.get("red")[y][x];
        int g = matrices.get("green")[y][x];
        int b = matrices.get("blue")[y][x];
        int a = isARGB ? matrices.get("alpha")[y][x] : 255;
        image.setRGB(x, y, (a << 24) | (r << 16) | (g << 8) | b);
      }
    }
    return image;
  }

  /**
   * Writes a BufferedImage to a file in the specified format.
   *
   * @param image      The image to save.
   * @param outputPath The file path to save the image.
   * @throws IOException If the file cannot be written.
   */
  private void writeImageToFile(BufferedImage image, String outputPath) throws IOException {
    String fileType = getFileExtension(outputPath);
    File outputFile = new File(outputPath);
    ImageWriter writer = ImageWriterFactory.getWriter(fileType);
    writer.write(image, fileType, outputFile);
  }

  /**
   * Extracts the file extension from a given filename.
   *
   * @param filename The filename to parse.
   * @return The file extension or an empty string if none is found.
   */
  private static String getFileExtension(String filename) {
    int lastDot = filename.lastIndexOf('.');
    int lastSeparator = Math.max(filename.lastIndexOf('/'), filename.lastIndexOf('\\'));
    return (lastDot > lastSeparator) ? filename.substring(lastDot + 1) : "";
  }

  @Override
  public void brighten() { /* need to test that the brighten is called correctly */
    String brightenIntensity = gui.offerPromptToGetOperationParameters(
            "Enter brighten intensity (-255 to 255): ");
    if (brightenIntensity == null) {
      gui.displayError("No value passed!");
      return;
    }
    List<String> args = new ArrayList<>();
    args.add(CommandConstants.BRIGHTEN);
    args.add(brightenIntensity);
    args.add(currentImageName);
    args.add(currentImageName + "-" + CommandConstants.BRIGHTEN);
    applyFilter(args.get(0), args);
  }

  @Override
  public void compress() {
    String compressionRatio = gui.offerPromptToGetOperationParameters(
            "Enter compression ratio (0-100): ");
    if (compressionRatio == null) {
      gui.displayError("No value passed!");
      return;
    }
    List<String> args = new ArrayList<>();
    args.add(CommandConstants.COMPRESS);
    args.add(compressionRatio);
    args.add(currentImageName);
    args.add(currentImageName + "-" + CommandConstants.COMPRESS);
    applyFilter(args.get(0), args);
  }

  @Override
  public void horizontalFlip() {
    List<String> args = new ArrayList<>();
    args.add(CommandConstants.HORIZONTAL_FLIP);
    args.add(currentImageName);
    args.add(currentImageName + "-" + CommandConstants.HORIZONTAL_FLIP);

    applyFilter(args.get(0), args);
  }

  @Override
  public void verticalFlip() {
    List<String> args = new ArrayList<>();
    args.add(CommandConstants.VERTICAL_FLIP);
    args.add(currentImageName);
    args.add(currentImageName + "-" + CommandConstants.VERTICAL_FLIP);

    applyFilter(args.get(0), args);
  }

  @Override
  public void redComp() {
    List<String> args = new ArrayList<>();
    args.add(CommandConstants.RED_COMPONENT);
    args.add(currentImageName);
    args.add(currentImageName + "-" + CommandConstants.RED_COMPONENT);

    applyFilter(args.get(0), args);
  }

  @Override
  public void greenComp() {
    List<String> args = new ArrayList<>();
    args.add(CommandConstants.GREEN_COMPONENT);
    args.add(currentImageName);
    args.add(currentImageName + "-" + CommandConstants.GREEN_COMPONENT);

    applyFilter(args.get(0), args);
  }

  @Override
  public void blueComp() {
    List<String> args = new ArrayList<>();
    args.add(CommandConstants.BLUE_COMPONENT);
    args.add(currentImageName);
    args.add(currentImageName + "-" + CommandConstants.BLUE_COMPONENT);

    applyFilter(args.get(0), args);
  }

  @Override
  public void valueComp() {
    List<String> args = new ArrayList<>();
    args.add(CommandConstants.VALUE_COMPONENT);
    args.add(currentImageName);
    args.add(currentImageName + "-" + CommandConstants.VALUE_COMPONENT);

    applyFilter(args.get(0), args);
  }

  @Override
  public void intensityComp() {
    List<String> args = new ArrayList<>();
    args.add(CommandConstants.INTENSITY_COMPONENT);
    args.add(currentImageName);
    args.add(currentImageName + "-" + CommandConstants.INTENSITY_COMPONENT);

    applyFilter(args.get(0), args);
  }

  @Override
  public void lumaComp() {
    List<String> args = new ArrayList<>();
    args.add(CommandConstants.LUMA_COMPONENT);
    args.add(currentImageName);
    args.add(currentImageName + "-" + CommandConstants.LUMA_COMPONENT);

    applyFilter(args.get(0), args);
  }

  @Override
  public void colorCorrection() {
    List<String> args = new ArrayList<>();
    args.add(CommandConstants.COLOR_CORRECT);
    args.add(currentImageName);
    args.add(currentImageName + "-" + CommandConstants.COLOR_CORRECT);

    gui.splitView(this, args.get(0), args,
            currentImageName);
  }

  @Override
  public void blur() {
    List<String> args = new ArrayList<>();
    args.add(CommandConstants.BLUR);
    args.add(currentImageName);
    args.add(currentImageName + "-" + CommandConstants.BLUR);

    gui.splitView(this, args.get(0), args,
            currentImageName);
  }

  @Override
  public void sharpen() {
    List<String> args = new ArrayList<>();
    args.add(CommandConstants.SHARPEN);
    args.add(currentImageName);
    args.add(currentImageName + "-" + CommandConstants.SHARPEN);

    gui.splitView(this, args.get(0), args,
            currentImageName);

  }

  @Override
  public void sepia() {
    List<String> args = new ArrayList<>();
    args.add(CommandConstants.SEPIA);
    args.add(currentImageName);
    args.add(currentImageName + "-" + CommandConstants.SEPIA);

    gui.splitView(this, args.get(0), args,
            currentImageName);
  }

  @Override
  public void levelsAdjust() {
    String blackPoint = gui.offerPromptToGetOperationParameters(
            "Enter black point (0-255):");
    if (blackPoint == null) {
      gui.displayError("No value passed!");
      return;
    }
    String midPoint = gui.offerPromptToGetOperationParameters(
            "Enter mid point (0-255):");
    if (midPoint == null) {
      gui.displayError("No value passed!");
      return;
    }
    String whitePoint = gui.offerPromptToGetOperationParameters(
            "Enter white point (0-255):");
    if (whitePoint == null) {
      gui.displayError("No value passed!");
      return;
    }
    List<String> args = new ArrayList<>();
    args.add(CommandConstants.LEVELS_ADJUST);
    args.add(blackPoint);
    args.add(midPoint);
    args.add(whitePoint);
    args.add(currentImageName);
    args.add(currentImageName + "-" + CommandConstants.LEVELS_ADJUST);
    gui.splitView(this, args.get(0), args,
            currentImageName);
  }

  /**
   * Prompts the user for target width and height, then applies the downscale operation to the
   * current image.
   * This method creates a new downscaled version of the current image.
   *
   * @throws RuntimeException if there's an error during the downscale operation
   */
  public void downscale() {
    String targetWidth = gui.offerPromptToGetOperationParameters(
            "Enter target width: ");
    if (targetWidth == null) {
      gui.displayError("No value passed!");
      return;
    }
    String targetHeight = gui.offerPromptToGetOperationParameters(
            "Enter target height: ");
    if (targetHeight == null) {
      gui.displayError("No value passed!");
      return;
    }
    List<String> args = new ArrayList<>();
    args.add(CommandConstants.DOWNSCALE);
    args.add(targetWidth);
    args.add(targetHeight);
    args.add(currentImageName);
    args.add(currentImageName + "-" + CommandConstants.DOWNSCALE);

    applyFilter(args.get(0), args);
  }


  @Override
  public void applyFilter(String filter, List<String> args) {
    try {
      model.operationsFactoryCall(filter, args, model);
      currentImageName += "-" + filter;
      imageVersioning(currentImageName);
      updateImageDisplay();
    } catch (RuntimeException e) {
      gui.displayError(e.getMessage());
    }
  }

  @Override
  public void splitViewFilter(String filter, List<String> args, String img) {

    try {
      model.operationsFactoryCall(filter, args, model);

    } catch (RuntimeException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Method to update the histogram for current image.
   */
  private void updateHistogram() {
    List<String> args = new ArrayList<>();
    args.add(CommandConstants.HISTOGRAM);
    args.add(currentImageName);
    args.add(currentImageName + "-histogram");
    try {
      model.operationsFactoryCall("histogram", args, model);
    } catch (RuntimeException e) {
      throw new RuntimeException(e);
    }

    BufferedImage img = getCurrentImage(currentImageName + "-histogram");
    gui.updateHistogram(img);

  }

  /**
   * Switches to the next version of the image if available.
   */
  @Override
  public void nextVersion() {
    if (versionIndex < imageVersions.size() - 1) {
      versionIndex++;
      currentImageName = imageVersions.get(versionIndex);
      updateImageDisplay();
    }
  }

  /**
   * Switches to the previous version of the image if available.
   */
  @Override
  public void previousVersion() {
    if (versionIndex > 0) {
      versionIndex--;
      currentImageName = imageVersions.get(versionIndex);
      updateImageDisplay();
    }
  }

  /**
   * Saves the current image to a file.
   *
   * @throws IOException If there's an error saving the image.
   */
  @Override
  public void save() throws IOException {
    if (currentImageName != null && !currentImageName.isEmpty()) {
      try {
        String savePath = gui.saveWithFileChooser(this, currentImageName, fileExtension);
        gui.displayMessage("Image saved successfully to: " + savePath);
      } catch (RuntimeException e) {
        gui.displayMessage("Error saving image: " + e.getMessage());
      }
    }
  }
}
