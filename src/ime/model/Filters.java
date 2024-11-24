package ime.model;

import java.util.List;

/**
 * The Filters interface defines the contract for all image filter operations.
 * Implementing classes will provide specific image transformations or manipulations
 * such as blurring, sharpening, or compressing an image.
 */
public interface Filters {

  /**
   * Executes the filter operation based on the provided arguments.
   *
   * @param args A list of strings containing the necessary parameters for the filter operation.
   *             The specific format and content of the list will depend on the filter being
   *             applied.
   *             For example, it may include:
   *             - Image name
   *             - Destination image name
   *             - Additional parameters like compression percentage or intensity level
   */
  void execute(List<String> args);
}