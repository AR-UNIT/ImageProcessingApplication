package ime.model;

import java.util.HashMap;
import java.util.Map;

import ime.CommandConstants;

/**
 * The OperationsFactory class is responsible for creating and managing image processing operations.
 * It uses the Factory pattern to instantiate different filter objects based on command strings.
 */
class OperationsFactory {
  private final Map<String, Filters> commandActions;

  /**
   * Constructs an OperationsFactory with a given model.
   * Initializes all supported image processing operations.
   *
   * @param model the ModelInterface instance to be used by the filters
   */
  OperationsFactory(ModelInterface model) {
    this.commandActions = new HashMap<>();
    initializeModelCommands(model);
  }

  /**
   * Initializes all supported image processing operations.
   * Each operation is associated with a command constant and its corresponding filter object.
   *
   * @param model the ModelInterface instance to be passed to each filter constructor
   */
  private void initializeModelCommands(ModelInterface model) {
    commandActions.put(CommandConstants.BRIGHTEN, new BrightenFilter(model));
    commandActions.put(CommandConstants.VERTICAL_FLIP, new VerticalFlipFilter(model));
    commandActions.put(CommandConstants.HORIZONTAL_FLIP, new HorizontalFlipFilter(model));
    commandActions.put(CommandConstants.SEPIA, new SepiaFilter(model));
    commandActions.put(CommandConstants.BLUR, new BlurFilter(model));
    commandActions.put(CommandConstants.SHARPEN, new SharpenFilter(model));
    commandActions.put(CommandConstants.RGB_SPLIT, new RGBSplitFilter(model));
    commandActions.put(CommandConstants.RGB_COMBINE, new RGBCombineFilter(model));
    commandActions.put(CommandConstants.RED_COMPONENT, new RedComponentFilter(model));
    commandActions.put(CommandConstants.GREEN_COMPONENT, new GreenComponentFilter(model));
    commandActions.put(CommandConstants.BLUE_COMPONENT, new BlueComponentFilter(model));
    commandActions.put(CommandConstants.LUMA_COMPONENT, new LumaFilter(model));
    commandActions.put(CommandConstants.INTENSITY_COMPONENT, new IntensityFilter(model));
    commandActions.put(CommandConstants.VALUE_COMPONENT, new ValueFilter(model));
    commandActions.put(CommandConstants.HISTOGRAM, new HistogramFilter(model));
    commandActions.put(CommandConstants.COLOR_CORRECT, new ColorCorrectFilter(model));
    commandActions.put(CommandConstants.LEVELS_ADJUST, new LevelsAdjustFilter(model));
    commandActions.put(CommandConstants.COMPRESS, new Compress(model));
    commandActions.put(CommandConstants.DOWNSCALE, new ImageDownscaling(model));
  }

  /**
   * Retrieves the filter object associated with the given command.
   *
   * @param command the string representation of the command
   * @return the Filters object associated with the command, or null if the command is not found
   */
  protected Filters getCommandFilter(String command) {
    return commandActions.get(command);
  }

  /**
   * Checks if a given command exists in the factory.
   *
   * @param command the string representation of the command to check
   * @return true if the command exists, false otherwise
   */
  protected Boolean commandExists(String command) {
    return commandActions.containsKey(command);
  }
}