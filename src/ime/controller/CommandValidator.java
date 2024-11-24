package ime.controller;

import java.util.List;

import ime.CommandConstants;

/**
 * The CommandValidator class is responsible for validating user commands within the image
 * processing application. It checks the format and arguments of commands to ensure they meet the
 * required specifications before execution.
 *
 * <p>This class includes validation methods for various commands, ensuring
 * correct usage and helping to prevent runtime errors.</p>
 */
public class CommandValidator {

  /**
   * Validates the provided command tokens.
   *
   * @param tokens the list of command tokens to validate
   * @throws IllegalArgumentException if the command is invalid or if the
   *                                  number of arguments is incorrect for the given command
   */
  public static void validateCommand(List<String> tokens) throws IllegalArgumentException {
    if (tokens.isEmpty()) {
      throw new IllegalArgumentException("Empty command");
    }

    String command = tokens.get(0).toLowerCase();

    switch (command) {
      case CommandConstants.LOAD:
      case CommandConstants.SAVE:
        validateLoadSave(tokens);
        break;
      case CommandConstants.RED_COMPONENT:
      case CommandConstants.GREEN_COMPONENT:
      case CommandConstants.BLUE_COMPONENT:
      case CommandConstants.VALUE_COMPONENT:
      case CommandConstants.LUMA_COMPONENT:
      case CommandConstants.INTENSITY_COMPONENT:
      case CommandConstants.HORIZONTAL_FLIP:
      case CommandConstants.VERTICAL_FLIP:
        validateTwoImageCommand(tokens);
        break;
      case CommandConstants.BLUR:
      case CommandConstants.SHARPEN:
      case CommandConstants.SEPIA:
      case CommandConstants.COLOR_CORRECT:
        validateTwoImageCommandWithSplit(tokens);
        break;
      case CommandConstants.BRIGHTEN:
        validateBrighten(tokens);
        break;
      case CommandConstants.RGB_SPLIT:
        validateRgbSplit(tokens);
        break;
      case CommandConstants.RGB_COMBINE:
        validateRgbCombine(tokens);
        break;
      case CommandConstants.RUN:
        validateRun(tokens);
        break;
      case CommandConstants.HISTOGRAM:
        validateHistogram(tokens);
        break;
      case CommandConstants.LEVELS_ADJUST:
        validateLevelsAdjust(tokens);
        break;
      case CommandConstants.COMPRESS:
        validateCompress(tokens);
        break;
      case CommandConstants.DOWNSCALE:
        break;
      default:
        throw new IllegalArgumentException("Invalid command: " + command);
    }
  }

  private static void validateCompress(List<String> tokens) {
    if (tokens.size() != 4) {
      throw new IllegalArgumentException("compress command requires 3 arguments");
    }
    try {
      Integer.parseInt(tokens.get(1));
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Increment must be a number");
    }
  }

  /**
   * Validates the levels-adjust command and its arguments.
   * This command adjusts the levels of an image, requiring at least 6 and at most 8 arguments.
   *
   * @param tokens the command tokens to validate
   * @throws IllegalArgumentException if the number of arguments is incorrect
   */
  private static void validateLevelsAdjust(List<String> tokens) throws IllegalArgumentException {
    if (tokens.size() < 6 || tokens.size() > 8) {
      throw new IllegalArgumentException("Invalid number of arguments");
    }

  }

  /**
   * Validates the histogram command and its arguments.
   * This command generates a histogram of an image, requiring exactly 3 arguments.
   *
   * @param tokens the command tokens to validate
   * @throws IllegalArgumentException if the number of arguments is incorrect
   */
  private static void validateHistogram(List<String> tokens) throws IllegalArgumentException {
    if (tokens.size() != 3) {
      throw new IllegalArgumentException("Invalid number of arguments");
    }
  }

  /**
   * Validates commands that require loading or saving an image.
   *
   * @param tokens the command tokens to validate
   * @throws IllegalArgumentException if the number of arguments is incorrect
   */
  private static void validateLoadSave(List<String> tokens) {
    if (tokens.size() != 3) {
      throw new IllegalArgumentException("Invalid number of arguments");
    }
  }

  /**
   * Validates commands that require two image arguments.
   *
   * @param tokens the command tokens to validate
   * @throws IllegalArgumentException if the number of arguments is incorrect
   */
  private static void validateTwoImageCommandWithSplit(List<String> tokens) {
    if (tokens.size() < 3 || tokens.size() > 5) {
      throw new IllegalArgumentException("Invalid number of arguments");
    }
  }

  /**
   * Validates commands that require two image arguments.
   *
   * @param tokens the command tokens to validate
   * @throws IllegalArgumentException if the number of arguments is incorrect
   */
  private static void validateTwoImageCommand(List<String> tokens) {
    if (tokens.size() != 3) {
      throw new IllegalArgumentException("Invalid number of arguments");
    }
  }

  /**
   * Validates the brighten command and its arguments.
   *
   * @param tokens the command tokens to validate
   * @throws IllegalArgumentException if the number of arguments is incorrect or
   *                                  if the increment is not an integer
   */
  private static void validateBrighten(List<String> tokens) {
    if (tokens.size() != 4) {
      throw new IllegalArgumentException("brighten command requires 3 arguments");
    }
    try {
      Integer.parseInt(tokens.get(1));
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Increment must be an integer");
    }
  }

  /**
   * Validates the rgb-split command and its arguments.
   *
   * @param tokens the command tokens to validate
   * @throws IllegalArgumentException if the number of arguments is incorrect
   */
  private static void validateRgbSplit(List<String> tokens) {
    if (tokens.size() != 5) {
      throw new IllegalArgumentException("rgb-split command requires 4 arguments");
    }
  }

  /**
   * Validates the rgb-combine command and its arguments.
   *
   * @param tokens the command tokens to validate
   * @throws IllegalArgumentException if the number of arguments is incorrect
   */
  private static void validateRgbCombine(List<String> tokens) {
    if (tokens.size() != 5) {
      throw new IllegalArgumentException("rgb-combine command requires 4 arguments");
    }
  }

  /**
   * Validates the run command and its arguments.
   *
   * @param tokens the command tokens to validate
   * @throws IllegalArgumentException if the number of arguments is incorrect
   */
  private static void validateRun(List<String> tokens) {
    if (tokens.size() != 2) {
      throw new IllegalArgumentException("run command requires 1 argument");
    }
  }
}
