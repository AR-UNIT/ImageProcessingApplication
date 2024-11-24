package ime.view.gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JButton;

/**
 * The ButtonPanel class represents a panel containing buttons for various image processing
 * operations.
 * It extends JPanel and creates buttons based on the provided map of operations.
 */
public class ButtonPanel extends JPanel {
  private final Map<String, Runnable> operations;

  /**
   * Constructs a ButtonPanel with the given map of operations.
   *
   * @param operations A map where keys are operation names and values are corresponding Runnable
   *                   actions.
   */
  public ButtonPanel(Map<String, Runnable> operations) {
    this.operations = operations;
    setLayout(new GridLayout(0, 1));
    initializeButtons();
  }

  /**
   * Initializes the buttons for each operation in the operations map.
   * Each button is created with the operation name as its label and
   * the corresponding Runnable as its action listener.
   */
  private void initializeButtons() {
    for (Map.Entry<String, Runnable> entry : operations.entrySet()) {
      JButton button = new JButton(entry.getKey());
      button.addActionListener(e -> entry.getValue().run());

      if ("Load Image".equals(entry.getKey())) {
        button.setBackground(Color.CYAN);
        button.setOpaque(true);
        button.setBorderPainted(false);

      }
      if ("Save".equals(entry.getKey())) {
        button.setBackground(Color.GREEN);
        button.setOpaque(true);
        button.setBorderPainted(false);

      }


      add(button);
    }
  }
}