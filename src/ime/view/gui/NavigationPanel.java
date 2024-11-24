package ime.view.gui;

import java.awt.FlowLayout;

import javax.swing.JPanel;
import javax.swing.JButton;

/**
 * The NavigationPanel class represents a panel containing navigation buttons
 * for moving between different versions of an image in the image processing application.
 * It extends JPanel and provides buttons for navigating to previous and next versions.
 */
public class NavigationPanel extends JPanel {
  private final Runnable previousVersionAction;
  private final Runnable nextVersionAction;

  /**
   * Constructs a new NavigationPanel with specified actions for previous and next version
   * navigation.
   *
   * @param previousVersionAction The Runnable to be executed when the "Previous Version" button is
   *                              clicked.
   * @param nextVersionAction     The Runnable to be executed when the "Next Version" button is
   *                              clicked.
   */
  public NavigationPanel(Runnable previousVersionAction, Runnable nextVersionAction) {
    this.previousVersionAction = previousVersionAction;
    this.nextVersionAction = nextVersionAction;
    initializePanel();
  }

  /**
   * Initializes the panel by setting up the layout and adding navigation buttons.
   * This method is called in the constructor and sets up the UI components of the panel.
   */
  private void initializePanel() {
    setLayout(new FlowLayout());
    JButton prevButton = new JButton("Previous Version");
    JButton nextButton = new JButton("Next Version");
    prevButton.addActionListener(e -> previousVersionAction.run());
    nextButton.addActionListener(e -> nextVersionAction.run());
    add(prevButton);
    add(nextButton);
  }
}