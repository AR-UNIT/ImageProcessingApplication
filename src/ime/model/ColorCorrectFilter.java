package ime.model;

import java.util.List;

/**
 * The ColorCorrect class represents a filter that performs color correction on an image.
 * It extends the AbstractFilters class and implements the execute method to perform
 * the color correction operation.
 */
public class ColorCorrectFilter extends AbstractFilters {
  private final ModelInterface model;
  private final ImageTransformation imageTransformation;

  /**
   * Constructs a ColorCorrect filter with the given model.
   *
   * @param model the ImageInterface model to be used for image operations
   */
  public ColorCorrectFilter(ModelInterface model) {
    this.model = model;
    this.imageTransformation = new ImageTransformation();
  }

  /**
   * Finds the peak value in a histogram between a specified range of values.
   *
   * @param histogram The histogram array for a specific color channel.
   * @return An array where [0] is the peak intensity value and [1] is its frequency.
   */
  private int[] findPeak(int[] histogram) {
    int peakValue = 10;
    int peakFrequency = histogram[10];

    for (int i = 10; i <= 245; i++) {
      if (histogram[i] > peakFrequency) {
        peakFrequency = histogram[i];
        peakValue = i;
      }
    }

    return new int[]{peakValue, peakFrequency};
  }

  @Override
  public void execute(List<String> commandTokens) throws IllegalArgumentException {
    String imageName = commandTokens.get(1);
    String destImageName = commandTokens.get(2);

    int splitPercent = extractSplitPercent(commandTokens);

    // Step 1: Fetch the original image and generate histograms for each channel
    ImageInterface originalImage = model.getImage(imageName);
    int[][] histogram = generateHistogram(originalImage);

    // Step 2: Find histogram peaks for each channel (ignoring values < 10 and > 245)
    int[] redPeak = findPeak(histogram[0]);
    int[] greenPeak = findPeak(histogram[1]);
    int[] bluePeak = findPeak(histogram[2]);

    // Step 3: Calculate average peak position
    int averagePeak = (redPeak[0] + greenPeak[0] + bluePeak[0]) / 3;

    // Step 4: Compute offsets for each channel
    int redOffset = averagePeak - redPeak[0];
    int greenOffset = averagePeak - greenPeak[0];
    int blueOffset = averagePeak - bluePeak[0];

    // Step 5: Adjust pixel values in the original image
    ImageInterface correctedImage = imageTransformation.applyColorCorrection(originalImage,
            redOffset, greenOffset, blueOffset, splitPercent);

    // Step 6: Store the corrected image
    model.storeImage(destImageName, correctedImage);

  }
}
