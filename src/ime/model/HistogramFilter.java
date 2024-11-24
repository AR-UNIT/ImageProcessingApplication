package ime.model;

import java.util.List;

/**
 * The Histogram class represents a filter that generates and visualizes a histogram of an image.
 * It extends the AbstractFilters class and implements the execute method to perform
 * the histogram generation and visualization operation.
 */
public class HistogramFilter extends AbstractFilters {
  private final ModelInterface model;

  /**
   * Constructs a Histogram filter with the given model.
   *
   * @param model the ImageInterface model to be used for image operations
   */
  public HistogramFilter(ModelInterface model) {
    this.model = model;
  }


  /**
   * Draws a histogram as a line graph on a new Image object. The histogram is drawn for
   * the red, green, and blue channels based on their respective frequencies. The resulting
   * image has a white background with optional grid lines and colored line graphs for each channel.
   *
   * @param histogram a 2D array representing the histograms for red, green, and blue channels
   * @return an Image object containing the drawn histogram as a line graph
   */
  private Image drawHistogram(int[][] histogram) {
    int width = 256;
    int height = 256;
    Image histogramImage = new Image(width, height);

    // Set white background
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        histogramImage.updatePixel(i, j, new Pixel(255, 255, 255, 255));
      }
    }

    // Find maximum frequency to scale the graph
    int maxFrequency = 0;
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 256; j++) {
        maxFrequency = Math.max(maxFrequency, histogram[i][j]);
      }
    }

    // Draw grid (optional)
    for (int x = 0; x < width; x += 32) { // Vertical lines every 32 pixels
      for (int y = 0; y < height; y++) {
        histogramImage.updatePixel(y, x, new Pixel(200, 200, 200, 255));
      }
    }

    for (int y = 0; y < height; y += 32) { // Horizontal lines every 32 pixels
      for (int x = 0; x < width; x++) {
        histogramImage.updatePixel(y, x, new Pixel(200, 200, 200, 255));
      }
    }

    // Draw histograms as line graphs
    int[][] colors = {{255, 0, 0}, {0, 255, 0}, {0, 0, 255}}; // RGB colors
    for (int channel = 0; channel < 3; channel++) {
      for (int x = 0; x < width - 1; x++) {
        int y1 = Math.max(0, Math.min(height - 1,
                height - (histogram[channel][x] * height / maxFrequency)));   // Scale frequency
        int y2 = Math.max(0, Math.min(height - 1,
                height - (histogram[channel][x + 1] * height / maxFrequency)));

        drawLine(histogramImage, x, y1, x + 1, y2, colors[channel]); // Draw line between points
      }
    }

    return histogramImage;
  }

  /**
   * Draws a line between two points on an image using Bresenham's Line Algorithm.
   * The line is drawn in the specified color.
   *
   * @param image the Image object on which to draw the line
   * @param x1    the starting x-coordinate of the line
   * @param y1    the starting y-coordinate of the line
   * @param x2    the ending x-coordinate of the line
   * @param y2    the ending y-coordinate of the line
   * @param color an array representing the RGB color of the line [R,G,B]
   */
  private void drawLine(Image image, int x1, int y1, int x2, int y2, int[] color) {
    int dx = Math.abs(x2 - x1);
    int dy = Math.abs(y2 - y1);
    int sx = x1 < x2 ? 1 : -1;
    int sy = y1 < y2 ? 1 : -1;
    int err = dx - dy;

    while (true) {
      if (x1 >= 0 && x1 < image.getWidth() && y1 >= 0 && y1 < image.getHeight()) {
        image.updatePixel(y1, x1, new Pixel(color[0], color[1], color[2], 255));
      }

      if (x1 == x2 && y1 == y2) {
        break;
      }

      int e2 = err * 2;
      if (e2 > -dy) {
        err -= dy;
        x1 += sx;
      }
      if (e2 < dx) {
        err += dx;
        y1 += sy;
      }
    }
  }

  @Override
  public void execute(List<String> commandTokens) {
    String imageName = commandTokens.get(1);
    String destImageName = commandTokens.get(2);

    ImageInterface originalImage = model.getImage(imageName);
    int[][] histogram = generateHistogram(originalImage);
    Image histogramImage = drawHistogram(histogram);

    model.storeImage(destImageName, histogramImage);
  }
}
