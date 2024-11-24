package ime.controller;

import static org.junit.Assert.assertEquals;

import ime.controller.gui.GUIController;
import ime.model.ModelInterface;
import ime.view.gui.GraphicalIMEInterface;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

/**
 * This test class tests that no-ops occur appropriately, with relevant message
 * displayed at view, when we try to perform operations and there is no image
 * selected.
 */
public class GuiControllerTestNoOps {
  private ModelInterface mockModel;
  private GUIController controller;
  private GraphicalIMEInterface mockView;
  private List<String> log;

  /**
   * Sets up the test environment by initializing the log list for capturing
   * output.
   */
  @Before
  public void setUp() {
    this.log = new ArrayList<>();
  }

  /**
   * Tests that no action occurs when attempting to save an image with no image
   * selected.
   */
  @Test
  public void save1() throws IOException {
    mockView = new MockGuiView(log, null, null);
    controller = new GUIController(mockModel, mockView);
    controller.save();
    assertEquals(null, log);
  }

  /**
   * Tests the behavior when attempting to brighten an image with no image
   * selected. Verifies that the relevant prompt is displayed and no error
   * occurs.
   */
  @Test
  public void brightenTest1() {
    mockView = new MockGuiView(log, null, "50");
    controller = new GUIController(mockModel, mockView);
    controller.brighten();

    List<String> expectedLog = new ArrayList<String>() {
      {
        add("offerPromptToGetOperationParameters(Enter brighten intensity "
                + "(-255 to 255): )");
        add("displayError: null");
      }
    };

    assertEquals(expectedLog, log);
  }

  /**
   * Tests the behavior when attempting to compress an image with no image
   * selected. Verifies that the relevant prompt is displayed and no error
   * occurs.
   */
  @Test
  public void compressTest1() {
    mockView = new MockGuiView(log, null, "50");
    controller = new GUIController(mockModel, mockView);
    controller.compress();

    List<String> expectedLog = new ArrayList<String>() {
      {
        add("offerPromptToGetOperationParameters(Enter compression ratio "
                + "(0-100): )");
        add("displayError: null");
      }
    };

    assertEquals(expectedLog, log);
  }

  /**
   * Tests the behavior when attempting to horizontally flip an image with no
   * image selected. Verifies that no operation is performed and no error is
   * displayed.
   */
  @Test
  public void horizontalFlipTest1() {
    mockView = new MockGuiView(log, null, null);
    controller = new GUIController(mockModel, mockView);
    controller.horizontalFlip();
    List<String> expectedLog = new ArrayList<String>() {
      { add("displayError: null"); }
    };

    assertEquals(expectedLog, log);
  }

  /**
   * Tests the behavior when attempting to vertically flip an image with no
   * image selected. Verifies that no operation is performed and no error is
   * displayed.
   */
  @Test
  public void verticalFlipTest1() {
    mockView = new MockGuiView(log, null, null);
    controller = new GUIController(mockModel, mockView);
    controller.verticalFlip();
    List<String> expectedLog = new ArrayList<String>() {
      { add("displayError: null"); }
    };

    assertEquals(expectedLog, log);
  }

  /**
   * Tests the behavior when attempting to apply the red color component to an
   * image with no image selected. Verifies that no operation is performed and
   * no error is displayed.
   */
  @Test
  public void redComponentTest1() {
    mockView = new MockGuiView(log, null, null);
    controller = new GUIController(mockModel, mockView);
    controller.redComp();
    List<String> expectedLog = new ArrayList<String>() {
      { add("displayError: null"); }
    };

    assertEquals(expectedLog, log);
  }

  /**
   * Tests the behavior when attempting to apply the blue color component to an
   * image with no image selected. Verifies that no operation is performed and
   * no error is displayed.
   */
  @Test
  public void blueComponentTest1() {
    mockView = new MockGuiView(log, null, null);
    controller = new GUIController(mockModel, mockView);
    controller.blueComp();
    List<String> expectedLog = new ArrayList<String>() {
      { add("displayError: null"); }
    };

    assertEquals(expectedLog, log);
  }

  /**
   * Tests the behavior when attempting to apply the green color component to an
   * image with no image selected. Verifies that no operation is performed and
   * no error is displayed.
   */
  @Test
  public void greenComponentTest1() {
    mockView = new MockGuiView(log, null, null);
    controller = new GUIController(mockModel, mockView);
    controller.greenComp();
    List<String> expectedLog = new ArrayList<String>() {
      { add("displayError: null"); }
    };

    assertEquals(expectedLog, log);
  }

  /**
   * Tests the behavior when attempting to apply the value color component to an
   * image with no image selected. Verifies that no operation is performed and
   * no error is displayed.
   */
  @Test
  public void valueComponentTest1() {
    mockView = new MockGuiView(log, null, null);
    controller = new GUIController(mockModel, mockView);
    controller.valueComp();
    List<String> expectedLog = new ArrayList<String>() {
      { add("displayError: null"); }
    };

    assertEquals(expectedLog, log);
  }

  /**
   * Tests the behavior when attempting to apply the intensity color component
   * to an image with no image selected. Verifies that no operation is performed
   * and no error is displayed.
   */
  @Test
  public void intensityComponentTest1() {
    mockView = new MockGuiView(log, null, null);
    controller = new GUIController(mockModel, mockView);
    controller.intensityComp();
    List<String> expectedLog = new ArrayList<String>() {
      { add("displayError: null"); }
    };

    assertEquals(expectedLog, log);
  }

  /**
   * Tests the behavior when attempting to apply the luma color component to an
   * image with no image selected. Verifies that no operation is performed and
   * no error is displayed.
   */
  @Test
  public void lumaComponentTest1() {
    mockView = new MockGuiView(log, null, null);
    controller = new GUIController(mockModel, mockView);
    controller.lumaComp();
    List<String> expectedLog = new ArrayList<String>() {
      { add("displayError: null"); }
    };

    assertEquals(expectedLog, log);
  }

  /**
   * Tests the behavior when attempting to apply color correction to an image
   * with no image selected. Verifies that no operation is performed and no
   * error is displayed.
   */
  @Test
  public void colorCorrectionTest1() {
    mockView = new MockGuiView(log, null, null);
    controller = new GUIController(mockModel, mockView);
    controller.colorCorrection();
    List<String> expectedLog = new ArrayList<String>() {
      { add("displayError: null"); }
    };

    assertEquals(expectedLog, log);
  }

  /**
   * Tests the behavior when attempting to apply a blur effect to an image with
   * no image selected. Verifies that no operation is performed and no error is
   * displayed.
   */
  @Test
  public void blurTest1() {
    mockView = new MockGuiView(log, null, null);
    controller = new GUIController(mockModel, mockView);
    controller.blur();
    List<String> expectedLog = new ArrayList<String>() {
      { add("displayError: null"); }
    };

    assertEquals(expectedLog, log);
  }

  /**
   * Tests the behavior when attempting to apply a sharpen effect to an image
   * with no image selected. Verifies that no operation is performed and no
   * error is displayed.
   */
  @Test
  public void sharpenTest1() {
    mockView = new MockGuiView(log, null, null);
    controller = new GUIController(mockModel, mockView);
    controller.sharpen();
    List<String> expectedLog = new ArrayList<String>() {
      { add("displayError: null"); }
    };

    assertEquals(expectedLog, log);
  }

  /**
   * Tests the behavior when attempting to apply sepia effect to an image with
   * no image selected. Verifies that no operation is performed and no error is
   * displayed.
   */
  @Test
  public void sepiaTest1() {
    mockView = new MockGuiView(log, null, null);
    controller = new GUIController(mockModel, mockView);
    controller.sepia();
    List<String> expectedLog = new ArrayList<String>() {
      { add("displayError: null"); }
    };

    assertEquals(expectedLog, log);
  }

  @Test
  public void levelAdjustTest1() {
    mockView = new MockGuiView(log, null, "black-mid-white");
    controller = new GUIController(mockModel, mockView);
    controller.levelsAdjust();

    List<String> expectedLog = new ArrayList<String>() {
      {
        add("offerPromptToGetOperationParameters(Enter black point (0-255):)");
        add("offerPromptToGetOperationParameters(Enter mid point (0-255):)");
        add("offerPromptToGetOperationParameters(Enter white point (0-255):)");
        add("splitView(levels-adjust, [levels-adjust, 50, 50, 50, null, "
                + "null-levels-adjust], null)");
      }
    };

    assertEquals(expectedLog, log);
  }

  /**
   * Tests the behavior when attempting to downscale an image with no image
   * selected. Verifies that the relevant prompts are displayed for target width
   * and height, and no error occurs.
   */
  @Test
  public void downscaleTest1() {
    mockView = new MockGuiView(log, null, "width-height");
    controller = new GUIController(mockModel, mockView);
    controller.downscale();

    List<String> expectedLog = new ArrayList<String>() {
      {
        add("offerPromptToGetOperationParameters(Enter target width: )");
        add("offerPromptToGetOperationParameters(Enter target height: )");
        add("displayError: null");
      }
    };

    assertEquals(expectedLog, log);
  }
}
