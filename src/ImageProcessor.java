import java.io.IOException;
import java.io.InputStreamReader;
import java.io.FileReader;
import java.io.BufferedReader;

import ime.controller.Controller;
import ime.controller.ControllerInterface;
import ime.controller.gui.GUIController;
import ime.controller.gui.GUIControllerInterface;
import ime.model.Model;
import ime.model.ModelInterface;
import ime.view.gui.GraphicalIME;
import ime.view.TerminalView;
import ime.view.ViewInterface;
import ime.view.gui.GraphicalIMEInterface;


/**
 * The ImageProcessor class serves as the entry point for the image processing application.
 * It provides different modes of operation: GUI, command-line interface, and script execution.
 */
public class ImageProcessor {

  /**
   * The main method that determines the mode of operation based on command-line arguments.
   *
   * @param args Command-line arguments.
   * @throws IOException If there's an error reading input or script file.
   */
  public static void main(String[] args) throws IOException {
    if (args.length > 0) {
      switch (args[0]) {
        case "-file":
          if (args.length < 2) {
            return;
          }
          runFromScript(args[1]);
          break;
        case "-text":
          runFromCLI();
          break;
        default:
      }
    } else {
      runGUI();
    }
  }

  /**
   * Runs the application in script mode, executing commands from a specified file.
   *
   * @param scriptPath The path to the script file containing commands.
   * @throws IOException If there's an error reading the script file.
   */
  private static void runFromScript(String scriptPath) throws IOException {
    ModelInterface model = new Model();
    ViewInterface terminalView = new TerminalView(System.out);
    try (BufferedReader reader = new BufferedReader(new FileReader(scriptPath))) {
      ControllerInterface controller = new Controller(reader, terminalView, model);
      controller.run();
    }
  }

  /**
   * Runs the application in command-line interface mode, accepting commands from standard input.
   *
   * @throws IOException If there's an error reading from standard input.
   */
  private static void runFromCLI() throws IOException {
    ModelInterface model = new Model();
    ViewInterface terminalView = new TerminalView(System.out);
    ControllerInterface controller = new Controller(new InputStreamReader(System.in),
            terminalView, model);
    controller.run();
  }

  /**
   * Runs the application in graphical user interface mode.
   */
  private static void runGUI() {
    ModelInterface model = new Model();
    /* view */
    GraphicalIMEInterface graphicalIME = new GraphicalIME();
    /* controller with model and view passed */
    GUIControllerInterface guiController = new GUIController(model, graphicalIME);
    guiController.startMethod();
  }
}