package ime.controller;

import java.io.IOException;

/**
 * Interface representing a controller in the image processing application.
 * It defines the necessary operations for controlling the application's workflow.
 */
public interface ControllerInterface {

  /**
   * Executes the main functionality of the controller.
   * This method typically includes the main loop for processing commands
   * and managing application state.
   *
   * @throws IOException if an input/output error occurs during execution
   */
  void run() throws IOException;
}