package ime.view.gui;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.ImageIcon;

/**
 * The ImageDisplayPanel class represents a panel for displaying images.
 * It extends JPanel and provides functionality to update the displayed image.
 */
public class ImageDisplayPanel extends JPanel {
  private final JLabel imageLabel;

  /**
   * Constructs a new ImageDisplayPanel.
   * Initializes the panel with a BorderLayout and sets up a JLabel within a JScrollPane for
   * displaying images.
   */
  public ImageDisplayPanel() {
    setLayout(new BorderLayout());
    imageLabel = new JLabel();
    imageLabel.setHorizontalAlignment(JLabel.CENTER);
    JScrollPane scrollPane = new JScrollPane(imageLabel);
    add(scrollPane, BorderLayout.CENTER);
  }

  /**
   * Updates the displayed image with a new BufferedImage.
   *
   * @param image The BufferedImage to be displayed. If null, no update will occur.
   */
  public void updateImage(BufferedImage image) {
    if (image != null) {
      ImageIcon icon = new ImageIcon(image);
      imageLabel.setIcon(icon);
      imageLabel.revalidate();
      imageLabel.repaint();
    }
  }
}