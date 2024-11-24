package ime.view.gui;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;

/**
 * The HistogramPanel class represents a panel for displaying a histogram image.
 * It extends JPanel and provides functionality to update the displayed histogram.
 */
public class HistogramPanel extends JPanel {
  private final JLabel histoLabel;

  /**
   * Constructs a new HistogramPanel.
   * Initializes the panel with a BorderLayout and sets up a JLabel for displaying the histogram.
   */
  public HistogramPanel() {
    setLayout(new BorderLayout());
    histoLabel = new JLabel();
    histoLabel.setHorizontalAlignment(JLabel.RIGHT);
    histoLabel.setVerticalAlignment(SwingConstants.BOTTOM);
    add(histoLabel, BorderLayout.CENTER);
  }

  /**
   * Updates the displayed histogram with a new image.
   *
   * @param histogram The BufferedImage representing the new histogram to be displayed.
   *                  If null, no update will occur.
   */
  public void updateHistogram(BufferedImage histogram) {
    if (histogram != null) {
      ImageIcon icon = new ImageIcon(histogram);
      histoLabel.setIcon(icon);
      histoLabel.revalidate();
      histoLabel.repaint();
    }
  }
}