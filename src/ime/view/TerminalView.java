package ime.view;

import java.io.IOException;

/**
 * A view implementation that outputs messages to a specified {@link Appendable}
 * object, such as a console or a file. This class provides methods to display
 * messages, errors, results, and other outputs in a terminal format.
 */
public class TerminalView implements ViewInterface {
  private final Appendable output;

  /**
   * Constructs a TerminalView with the specified output destination.
   *
   * @param output the {@link Appendable} object to which messages will be written
   */
  public TerminalView(Appendable output) {
    this.output = output;
  }

  /**
   * Displays a message to the output.
   *
   * @param message the message to display.
   */
  @Override
  public void displayMessage(String message) {
    try {
      output.append(message).append("\n");
    } catch (IOException e) {
      handleOutputError(e);
    }
  }

  /**
   * Displays an error message to the output.
   *
   * @param error the error message to display
   */
  @Override
  public void displayError(String error) {
    try {
      output.append("Error: ").append(error).append("\n");
    } catch (IOException e) {
      handleOutputError(e);
    }
  }

  /**
   * Handles any IO exceptions that occur during output operations.
   *
   * @param e the IOException that occurred
   */
  private void handleOutputError(IOException e) {
    System.err.println("Error writing to output: " + e.getMessage());
  }
}
