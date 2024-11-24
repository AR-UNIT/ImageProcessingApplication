package ime.controller;

import ime.CommandConstants;
import ime.model.ModelInterface;
import ime.view.DisplayMessageConstants;
import ime.view.ViewInterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The CommandRegistry class is responsible for registering and managing command actions
 * related to image processing within the application. It maps command identifiers to
 * their corresponding actions, allowing for dynamic command execution.
 *
 * <p>This class maintains a map for general commands that interact with the model,
 * and uses a switch statement for local commands.</p>
 */
public class CommandRegistry {
  private final List<String> commandActions;
  private final ViewInterface view;
  private final Controller controller;
  private final ModelInterface model;

  /**
   * Constructs a CommandRegistry and registers the available command actions from text input.
   *
   * @param model the model interface that provides image processing functionality.
   */
  public CommandRegistry(ModelInterface model, Controller controller, ViewInterface view) {
    this.commandActions = new ArrayList<>();
    this.view = view;
    this.controller = controller;
    this.model = model;
    initializeModelCommands();
  }

  /**
   * Initializes the map of controller-based commands.
   */
  private void initializeModelCommands() {
    commandActions.add(CommandConstants.LOAD);
    commandActions.add(CommandConstants.SAVE);
    commandActions.add(CommandConstants.RUN);
  }

  /**
   * Executes a command based on the given command name and arguments.
   * This method first checks if the command is a model-based command (stored in commandActions).
   * If it is, it executes the corresponding filter. If not, it checks if it's a local command
   * and executes it using the controller.
   *
   * @param commandName the name of the command to be executed
   * @param args        a List of String arguments for the command
   * @throws IllegalArgumentException if the command is unknown
   */
  public void executeCommand(String commandName, List<String> args) {
    // First, check if the command is a model-based command

    if (commandActions.contains(commandName)) {
      try {
        // Use switch-case to handle local commands
        switch (commandName) {
          case CommandConstants.LOAD:
            controller.readImage(args);
            break;
          case CommandConstants.SAVE:
            controller.saveImage(args);
            break;
          case CommandConstants.RUN:
            controller.runScript(args);
            break;
          default:
            throw new IllegalArgumentException("command not found!");
        }
      } catch (IOException e) {
        view.displayError(DisplayMessageConstants.PROCESSING_COMMAND + e.getMessage());
      }

    } else {
      model.operationsFactoryCall(commandName, args, model);
    }
  }
}
