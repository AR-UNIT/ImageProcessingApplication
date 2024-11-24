package ime.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import ime.CommandConstants;
import ime.model.ModelInterface;
import ime.view.DisplayMessageConstants;
import ime.view.ViewInterface;

/**
 * The Controller class orchestrates image processing operations by interacting with the model and
 * view.
 * It processes commands, executes scripts, and handles image reading and saving operations.
 */
public class Controller implements ControllerInterface {

  private final ModelInterface model;
  private final ViewInterface view;
  private final Readable in;
  private final CommandRegistry commandRegistry;
  private static final Set<String> ARGB_TYPES = Set.of("png");

  /**
   * Constructs a Controller with the specified input source and view.
   */
  public Controller(Readable in, ViewInterface view, ModelInterface model) {
    this.model = model;
    this.in = in;
    this.view = view;
    this.commandRegistry = new CommandRegistry(model, this, view);
  }

  /**
   * Executes a single command line input.
   * This method trims the input, checks if it's not empty, and processes the command.
   *
   * @param line the command line to be executed
   */
  private void executeCommandLine(String line) {
    line = line.trim();
    if (!isLineEmpty(line)) {
      view.displayMessage(DisplayMessageConstants.PROCESSING_COMMAND + line);
      boolean isCommandExecuted = processCommand(line);
      view.displayMessage(DisplayMessageConstants.EXECUTED_COMMAND + isCommandExecuted);
    }
  }

  @Override
  public void run() {
    Scanner scanner = new Scanner(this.in);
    String line;
    view.displayMessage(DisplayMessageConstants.INITIAL_PROMPT);
    while (scanner.hasNextLine()) {
      line = scanner.nextLine().trim();
      if (line.equalsIgnoreCase(CommandConstants.EXIT)) {
        view.displayMessage(DisplayMessageConstants.EXIT);
        break;
      }
      executeCommandLine(line);
    }
    scanner.close();
  }

  /**
   * Runs a script file containing multiple commands.
   * This method reads commands from the specified file and executes them sequentially.
   *
   * @param commandTokens a list of command tokens, where the second token is the script file path
   * @throws IOException if there's an error reading the script file
   */
  void runScript(List<String> commandTokens) throws IOException {
    String filePath = commandTokens.get(1);
    try (BufferedReader scriptReader = loadScriptFile(filePath)) {
      processScriptFile(scriptReader);
    } catch (FileNotFoundException e) {
      view.displayError(DisplayMessageConstants.SCRIPT_NOT_FOUND + filePath);
    }
  }

  /**
   * Loads a script file and returns a BufferedReader for it.
   *
   * @param filePath the path to the script file
   * @return a BufferedReader for the script file
   * @throws FileNotFoundException if the script file is not found
   */
  private BufferedReader loadScriptFile(String filePath) throws FileNotFoundException {
    return new BufferedReader(new FileReader(filePath));
  }

  /**
   * Processes a script file by reading and executing each line.
   *
   * @param scriptReader a BufferedReader for the script file
   * @throws IOException if there's an error reading the script file
   */
  private void processScriptFile(BufferedReader scriptReader) throws IOException {
    String line;
    while ((line = scriptReader.readLine()) != null) {
      executeCommandLine(line.trim());
    }
  }

  /**
   * Checks if a line is empty or a comment (starts with #).
   *
   * @param line the line to check
   * @return true if the line is empty or a comment, false otherwise
   */
  private static boolean isLineEmpty(String line) {
    return line.isEmpty() || line.startsWith("#");
  }

  /**
   * Processes a single command by parsing it into tokens, validating it, and executing it.
   *
   * @param command the command string to process
   * @return true if the command was processed successfully, false otherwise
   */
  private boolean processCommand(String command) {
    List<String> commandTokens = parseCommandTokens(command);
    CommandValidator.validateCommand(commandTokens);
    try {
      executeRegisteredCommand(commandTokens);
    } catch (IOException e) {
      view.displayError(DisplayMessageConstants.PROCESSING_COMMAND + e.getMessage());
    }
    return true;
  }


  /**
   * Parses a command string into a list of command tokens.
   *
   * @param command the command string to parse
   * @return a list of command tokens
   */
  private List<String> parseCommandTokens(String command) {
    return Arrays.asList(command.trim().split("\\s+"));
  }

  /**
   * Executes a registered command using the CommandRegistry.
   *
   * @param commandTokens a list of command tokens
   * @throws IOException if there's an error executing the command
   */
  private void executeRegisteredCommand(List<String> commandTokens) throws IOException {
    commandRegistry.executeCommand(commandTokens.get(0), commandTokens);
  }

  /**
   * Reads an image from a file and stores it in the model.
   *
   * @param commandTokens a list of command tokens, where the second token is the file path and the
   *                      third is the image name
   * @throws IOException if there's an error reading the image file
   */
  void readImage(List<String> commandTokens) throws IOException {
    String imageFilePath = commandTokens.get(1);
    String imageName = commandTokens.get(2);
    BufferedImage bufferedImage = loadBufferedImage(imageFilePath);
    Map<String, int[][]> imagePixelArr = convertTo2DPixelArray(bufferedImage);
    model.convertAndStoreImage(imageName, imagePixelArr);
  }

  /**
   * Loads a BufferedImage from a file.
   *
   * @param imageFilePath the path to the image file
   * @return a BufferedImage object representing the loaded image
   * @throws IOException if there's an error reading the image file
   */
  private BufferedImage loadBufferedImage(String imageFilePath) throws IOException {
    String fileType = getFileExtension(imageFilePath);
    ImageReader reader = ImageReaderFactory.getReader(fileType);
    return reader.read(imageFilePath);
  }

  /**
   * Converts a BufferedImage to a 2D pixel array representation.
   *
   * @param bufferedImage the BufferedImage to convert
   * @return a Map containing separate 2D arrays for red, green, blue, and alpha channels
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
   * Saves an image to a file.
   *
   * @param commandTokens a list of command tokens, where the second token is the output path and
   *                      the third is the image name
   * @throws IOException if there's an error writing the image file
   */
  void saveImage(List<String> commandTokens) throws IOException {
    String outputPath = commandTokens.get(1);
    String imageName = commandTokens.get(2);
    Map<String, int[][]> matrices = fetchImageMatrices(imageName);

    if (matrices == null) {
      return;
    }

    BufferedImage image = createBufferedImage(matrices, outputPath);
    writeImageToFile(image, outputPath);
    view.displayMessage(DisplayMessageConstants.SAVED_TO + outputPath);
  }

  /**
   * Fetches the image matrices from the model for a given image name.
   *
   * @param imageName the name of the image to fetch
   * @return a Map containing separate 2D arrays for red, green, blue, and alpha channels, or null
   */
  private Map<String, int[][]> fetchImageMatrices(String imageName) {
    try {
      return model.convertAndFetchImage(imageName);
    } catch (Exception e) {
      view.displayError(e.getMessage());
      return null;
    }
  }

  /**
   * Creates a BufferedImage from the given image matrices.
   *
   * @param matrices   a Map containing separate 2D arrays for red, green, blue, and alpha channels
   * @param outputPath the output path for the image, used to determine if alpha channel should be
   *                   included
   * @return a BufferedImage created from the given matrices
   */
  private BufferedImage createBufferedImage(Map<String, int[][]> matrices, String outputPath) {
    int height = matrices.get("red").length;
    int width = matrices.get("red")[0].length;
    boolean isARGB = ARGB_TYPES.contains(getFileExtension(outputPath));
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
   * Writes a BufferedImage to a file.
   *
   * @param image      the BufferedImage to write
   * @param outputPath the path where the image should be saved
   * @throws IOException if there's an error writing the image file
   */
  private void writeImageToFile(BufferedImage image, String outputPath) throws IOException {
    String fileType = getFileExtension(outputPath);
    File outputFile = new File(outputPath);
    ImageWriter writer = ImageWriterFactory.getWriter(fileType);
    writer.write(image, fileType, outputFile);
  }

  /**
   * Gets the file extension from a file path.
   *
   * @param filePath the file path
   * @return the file extension, or an empty string if no extension is found
   */
  private static String getFileExtension(String filePath) {
    int lastDot = filePath.lastIndexOf('.');
    int lastSeparator = Math.max(filePath.lastIndexOf('/'), filePath.lastIndexOf('\\'));
    return (lastDot > lastSeparator) ? filePath.substring(lastDot + 1) : "";
  }
}