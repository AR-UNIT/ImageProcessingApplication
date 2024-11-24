package ime.view;

/**
 * An interface for defining a view that displays messages to the user.
 * Implementations of this interface can handle various output formats,
 * such as terminal output, GUI components, or file logging.
 */
public interface ViewInterface {

  /**
   * Displays a generic message to the user.
   *
   * @param message the message to display.
   */
  void displayMessage(String message);

  /**
   * Displays an error message to the user.
   *
   * @param error the error message to display.
   */
  void displayError(String error);

}
