package ime.controller;

import java.util.List;

import ime.view.ViewInterface;

/**
 * A mock implementation of the {@link ViewInterface} used for testing.
 * This class records method invocations to a provided log list instead of displaying messages or
 * errors.
 */
public class MockView implements ViewInterface {

  private final List<String> log; /* List for recording method invocations */

  /**
   * Constructs a MockView with a specified log list to capture method invocations.
   *
   * @param log The list where method invocations are logged.
   */
  public MockView(List<String> log) {
    this.log = log;
  }

  /**
   * Logs a message to the provided log list, prefixed with "displayMessage:".
   *
   * @param message The message to be displayed.
   */
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
}
