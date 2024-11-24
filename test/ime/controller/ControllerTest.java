package ime.controller;

import static ime.controller.ControllerTestConstants.ALPHA_MATRIX_READ;
import static ime.controller.ControllerTestConstants.BLUE_MATRIX_READ;
import static ime.controller.ControllerTestConstants.GREEN_MATRIX_READ;
import static ime.controller.ControllerTestConstants.RED_MATRIX_READ;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import ime.model.MockModel;
import ime.model.ModelInterface;
import ime.view.ViewInterface;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for the Controller class in the ime.controller package.
 * This class tests various image processing commands executed by the
 * controller, such as loading, saving, and transforming images. It uses mock
 * objects for the model and view to simulate interactions and verify behavior.
 */
public class ControllerTest {
  private ModelInterface mockModel;
  private ControllerInterface controller;
  private ViewInterface mockView;

  private Readable inputReader; /* Reader to simulate user input */
  private List<String> log;

  /**
   * Sets up the mock objects and initializes the log list before each test.
   */
  @Before
  public void setUp() {
    log = new ArrayList<>();
    mockModel = new MockModel(log, false);
    mockView = new MockView(log);
  }

  /**
   * Tests the loading and processing of a PPM image file.
   * Simulates user input to load a PPM image and verifies the expected log
   * output.
   */
  @Test
  public void testLoadImagePPM() throws IOException {
    String loadImageString = "load test/ime/controller/testResources/"
            + "controller_tests_image_ppm.ppm sample";
    inputReader = new StringReader(loadImageString); /* Simulated user input */
    controller = new Controller(inputReader, mockView, mockModel);
    controller.run(); /* Execute the controller */

    List<String> expectedLog = new ArrayList<>() {
      {
        add("displayMessage: Enter commands (type 'exit' to quit) = \n");
        add("displayMessage: Processing Command = load test/ime/controller/"
                + "testResources/controller_tests_image_ppm.ppm sample");
        add("convertAndStoreImage(sample, {red=" + RED_MATRIX_READ
                + ", green=" + GREEN_MATRIX_READ + ", blue=" + BLUE_MATRIX_READ
                + ", alpha=" + ALPHA_MATRIX_READ + "})");

        add("displayMessage: Executed command = true");
      }
    };

    assertEquals(4, log.size());
    assertEquals(expectedLog, log);
  }

  /**
   * Tests the loading and processing of a PNG image file.
   * Simulates user input to load a PNG image and verifies the expected log
   * output.
   */
  @Test
  public void testLoadImagePNG() throws IOException {
    String loadImageString = "load test/ime/controller/testResources/"
            + "controller_tests_image_png.png sample";
    inputReader = new StringReader(loadImageString); /* Simulated user input */
    controller = new Controller(inputReader, mockView, mockModel);
    controller.run(); /* Execute the controller */

    List<String> expectedLog = new ArrayList<>() {
      {
        add("displayMessage: Enter commands (type 'exit' to quit) = \n");
        add("displayMessage: Processing Command = load "
                + "test/ime/controller/testResources/"
                + "controller_tests_image_png.png sample");
        add("convertAndStoreImage(sample, {red=" + RED_MATRIX_READ
                + ", green=" + GREEN_MATRIX_READ + ", blue=" + BLUE_MATRIX_READ
                + ", alpha=" + ALPHA_MATRIX_READ + "})");
        add("displayMessage: Executed command = true");
      }
    };

    assertEquals(4, log.size());
    assertEquals(expectedLog, log);
  }

  /**
   * Tests the loading and processing of a JPG image file.
   * Simulates user input to load a JPG image and verifies the expected log
   * output.
   */
  @Test
  public void testLoadImageJPG() throws IOException {
    String loadImageString = "load test/ime/controller/testResources/"
            + "controller_tests_image_jpg.jpg sample";
    inputReader = new StringReader(loadImageString); /* Simulated user input */
    controller = new Controller(inputReader, mockView, mockModel);
    controller.run(); /* Execute the controller */

    List<String> expectedLog = new ArrayList<>() {
      {
        add("displayMessage: Enter commands (type 'exit' to quit) = \n");
        add("displayMessage: Processing Command = load test/ime/controller/"
                + "testResources/controller_tests_image_jpg.jpg sample");
        add("displayMessage: Executed command = true");
      }
    };
    assertTrue(log.contains(expectedLog.get(0)));
    assertTrue(log.contains(expectedLog.get(1)));
    assertTrue(log.contains(expectedLog.get(2)));
  }

  /**
   * Tests the saving of a PPM image file and verifies the log output.
   * Simulates loading a PPM image, saving it, and then loading the saved image.
   */
  @Test
  public void testSaveImagePPM() throws IOException {
    String loadImageString = "load test/ime/controller/testResources"
            + "/controller_tests_image_ppm.ppm sample";
    String saveImageString = "save test/ime/controller/testOutputs/"
            + "controller_tests_image_ppm.ppm sample";
    String loadSavedImageString = "load test/ime/controller/testOutputs/"
            + "controller_tests_image_ppm.ppm sample2";
    inputReader = new StringReader(loadImageString + "\n" + saveImageString
            + "\n" + loadSavedImageString); /* Simulated user input */
    controller = new Controller(inputReader, mockView, mockModel);
    controller.run(); /* Execute the controller */
    List<String> expectedLog = new ArrayList<>() {
      {
        add("displayMessage: Enter commands (type 'exit' to quit) = \n");
        add("displayMessage: Processing Command = load test/ime/controller/"
                + "testResources/controller_tests_image_ppm.ppm sample");
        add("convertAndStoreImage(sample, {red=" + RED_MATRIX_READ
                + ", green=" + GREEN_MATRIX_READ + ", blue=" + BLUE_MATRIX_READ
                + ", alpha=" + ALPHA_MATRIX_READ + "})");
        add("displayMessage: Executed command = true");
        add("displayMessage: Processing Command = save test/ime/controller/"
                + "testOutputs/controller_tests_image_ppm.ppm sample");
        add("convertAndFetchImage(sample)");
        add("displayMessage: Image saved to = test/ime/controller/"
                + "testOutputs/controller_tests_image_ppm.ppm");
        add("displayMessage: Executed command = true");
        add("displayMessage: Processing Command = load test/ime/controller/"
                + "testOutputs/controller_tests_image_ppm.ppm sample2");
        add("convertAndStoreImage(sample2, {red=" + RED_MATRIX_READ
                + ", green=" + GREEN_MATRIX_READ + ", blue=" + BLUE_MATRIX_READ
                + ", alpha=" + ALPHA_MATRIX_READ + "})");
        add("displayMessage: Executed command = true");
      }
    };

    System.out.println(log);
    assertEquals(expectedLog, log);
  }

  /**
   * Tests the saving of a PNG image file and verifies the log output.
   * Simulates loading a PNG image, saving it, and then loading the saved image.
   */
  @Test
  public void testSaveImagePNG() throws IOException {
    String loadImageString = "load test/ime/controller/testResources/"
            + "controller_tests_image_png.png sample";
    String saveImageString = "save test/ime/controller/testOutputs/"
            + "controller_tests_image_png.png sample";
    String loadSavedImageString = "load test/ime/controller/testOutputs/"
            + "controller_tests_image_png.png sample2";
    inputReader = new StringReader(loadImageString + "\n" + saveImageString
            + "\n" + loadSavedImageString); /* Simulated user input */
    controller = new Controller(inputReader, mockView, mockModel);
    controller.run(); /* Execute the controller */
    List<String> expectedLog = new ArrayList<>() {
      {
        add("displayMessage: Enter commands (type 'exit' to quit) = \n");
        add("displayMessage: Processing Command = load test/ime/controller/"
                + "testResources/controller_tests_image_png.png sample");
        add("convertAndStoreImage(sample, {red=" + RED_MATRIX_READ
                + ", green=" + GREEN_MATRIX_READ + ", blue=" + BLUE_MATRIX_READ
                + ", alpha=" + ALPHA_MATRIX_READ + "})");
        add("displayMessage: Executed command = true");
        add("displayMessage: Processing Command = save test/ime/controller/"
                + "testOutputs/controller_tests_image_png.png sample");
        add("convertAndFetchImage(sample)");
        add("displayMessage: Image saved to = test/ime/controller/"
                + "testOutputs/controller_tests_image_png.png");
        add("displayMessage: Executed command = true");
        add("displayMessage: Processing Command = load "
                + "test/ime/controller/testOutputs/"
                + "controller_tests_image_png.png sample2");
        add("convertAndStoreImage(sample2, {red=" + RED_MATRIX_READ
                + ", green=" + GREEN_MATRIX_READ + ", blue=" + BLUE_MATRIX_READ
                + ", alpha=" + ALPHA_MATRIX_READ + "})");
        add("displayMessage: Executed command = true");
      }
    };

    System.out.println(log);
    assertEquals(expectedLog, log);
  }

  /**
   * Tests the saving of a JPG image file and verifies the log output.
   * Simulates loading a JPG image, saving it, and then loading the saved image.
   */
  @Test
  public void testSaveImageJPG() throws IOException {
    String loadImageString = "load test/ime/controller/testResources/"
            + "controller_tests_image_jpg.jpg sample";
    String saveImageString = "save test/ime/controller/testOutputs/"
            + "controller_tests_image_jpg.jpg sample";
    String loadSavedImageString = "load test/ime/controller/testOutputs/"
            + "controller_tests_image_jpg.jpg sample2";
    inputReader = new StringReader(loadImageString + "\n" + saveImageString
            + "\n" + loadSavedImageString); /* Simulated user input */
    controller = new Controller(inputReader, mockView, mockModel);
    controller.run(); /* Execute the controller */
    List<String> expectedLog = new ArrayList<>() {
      {
        add("displayMessage: Enter commands (type 'exit' to quit) = \n");
        add("displayMessage: Processing Command = load test/ime/controller/"
                + "testResources/controller_tests_image_jpg.jpg sample");
        add("displayMessage: Executed command = true");
        add("displayMessage: Processing Command = save test/ime/controller/"
                + "testOutputs/controller_tests_image_jpg.jpg sample");
        add("convertAndFetchImage(sample)");
        add("displayMessage: Image saved to = test/ime/controller/"
                + "testOutputs/controller_tests_image_jpg.jpg");
        add("displayMessage: Executed command = true");
        add("displayMessage: Processing Command = load test/ime/controller/"
                + "testOutputs/controller_tests_image_jpg.jpg sample2");
        add("displayMessage: Executed command = true");
      }
    };
    System.out.println(log);
    assertTrue(log.contains(expectedLog.get(0)));
    assertTrue(log.contains(expectedLog.get(1)));
    assertTrue(log.contains(expectedLog.get(2)));
    assertTrue(log.contains(expectedLog.get(3)));
    assertTrue(log.contains(expectedLog.get(4)));
    assertTrue(log.contains(expectedLog.get(5)));
    assertTrue(log.contains(expectedLog.get(6)));
    assertTrue(log.contains(expectedLog.get(7)));
    assertTrue(log.contains(expectedLog.get(8)));
  }

  /**
   * Tests the execution of a script file that contains multiple image
   * processing commands. Simulates the execution of a script and verifies the
   * log output for each command.
   */
  @Test
  public void testRunScript() throws IOException {
    String runScriptString =
            "run test/ime/controller/testResources/TestScript1.txt";
    inputReader = new StringReader(runScriptString); /* Simulated user input */
    controller = new Controller(inputReader, mockView, mockModel);
    List<String> expectedLog = new ArrayList<String>() {
      {
        add("displayMessage: Enter commands (type 'exit' to quit) = \n");
        add("displayMessage: Processing Command = run test/ime/controller/"
                + "testResources/TestScript1.txt");
        add("displayMessage: Processing Command = load test/ime/controller/"
                + "testResources/controller_tests_image_ppm.ppm sample");
        add("convertAndStoreImage(sample, {red=" + RED_MATRIX_READ
                + ", green=" + GREEN_MATRIX_READ + ", blue=" + BLUE_MATRIX_READ
                + ", alpha=" + ALPHA_MATRIX_READ + "})");
        add("displayMessage: Executed command = true");
        add("displayMessage: Processing Command = rgb-split sample "
                + "sample-red sample-green sample-blue");
        add("commandFactoryCall(rgb-split, [rgb-split, sample, "
                + "sample-red, sample-green, sample-blue])");
        add("displayMessage: Executed command = true");
        add("displayMessage: Processing Command = rgb-combine "
                + "sample-red-combine sample-red sample-green sample-blue");
        add("commandFactoryCall(rgb-combine, [rgb-combine, sample-red-combine, "
                + "sample-red, sample-green, sample-blue])");
        add("displayMessage: Executed command = true");
        add("displayMessage: Processing Command = blur sample sample-blurred1");
        add("commandFactoryCall(blur, [blur, sample, sample-blurred1])");
        add("displayMessage: Executed command = true");
        add("displayMessage: Processing Command = sharpen sample "
                + "sample-sharpened1");
        add("commandFactoryCall(sharpen, [sharpen, sample, "
                + "sample-sharpened1])");
        add("displayMessage: Executed command = true");
        add("displayMessage: Processing Command = brighten 50 sample "
                + "sample-brighter");
        add("commandFactoryCall(brighten, [brighten, 50, sample, "
                + "sample-brighter])");
        add("displayMessage: Executed command = true");
        add("displayMessage: Processing Command = brighten 1000 sample "
                + "sample-brightest");
        add("commandFactoryCall(brighten, [brighten, 1000, sample, "
                + "sample-brightest])");
        add("displayMessage: Executed command = true");
        add("displayMessage: Processing Command = brighten -50 sample "
                + "sample-darker");
        add("commandFactoryCall(brighten, [brighten, -50, sample, "
                + "sample-darker])");
        add("displayMessage: Executed command = true");
        add("displayMessage: Processing Command = brighten -1000 sample "
                + "sample-darkest");
        add("commandFactoryCall(brighten, [brighten, -1000, sample, "
                + "sample-darkest])");
        add("displayMessage: Executed command = true");
        add("displayMessage: Processing Command = sepia sample sample-sepia");
        add("commandFactoryCall(sepia, [sepia, sample, sample-sepia])");
        add("displayMessage: Executed command = true");
        add("displayMessage: Processing Command = vertical-flip sample "
                + "sample-vertical");
        add("commandFactoryCall(vertical-flip, [vertical-flip, sample, "
                + "sample-vertical])");
        add("displayMessage: Executed command = true");
        add("displayMessage: Processing Command = horizontal-flip sample "
                + "sample-horizontal");
        add("commandFactoryCall(horizontal-flip, [horizontal-flip, sample, "
                + "sample-horizontal])");
        add("displayMessage: Executed command = true");
        add("displayMessage: Processing Command = histogram sample "
                + "sample-histogram");
        add("commandFactoryCall(histogram, [histogram, sample, "
                + "sample-histogram])");
        add("displayMessage: Executed command = true");
        add("displayMessage: Processing Command = levels-adjust 20 100 255 "
                + "sample "
                + "sample-levels-adjust-split split 50");
        add("commandFactoryCall(levels-adjust, [levels-adjust, 20, 100, 255, "
                + "sample, sample-levels-adjust-split, split, 50])");
        add("displayMessage: Executed command = true");
        add("displayMessage: Processing Command = save test/ime/controller/"
                + "testOutputs/controller_tests_image_ppm.ppm sample");
        add("convertAndFetchImage(sample)");
        add("displayMessage: Image saved to = test/ime/controller/"
                + "testOutputs/controller_tests_image_ppm.ppm");
        add("displayMessage: Executed command = true");
        add("displayMessage: Executed command = true");
      }
    };
    controller.run(); /* Execute the controller */
    System.out.println(log);
    assertEquals(expectedLog, log);
  }

  /**
   * Tests that an exception is thrown for an unknown command.
   * Verifies that the controller handles invalid commands correctly by throwing
   * an
   * {@link IllegalArgumentException}.
   */
  @Test(expected = IllegalArgumentException.class)
  public void unknownCommandThrowsExceptionTest() throws IOException {
    String runScriptString = "some unknown command";
    inputReader = new StringReader(runScriptString); /* Simulated user input */
    controller = new Controller(inputReader, mockView, mockModel);
    controller.run(); /* Execute the controller */
  }

  /**
   * Tests that an exception is thrown when the levels-adjust command is missing
   * required parameters. Verifies that the controller handles invalid command
   * input properly.
   */
  @Test(expected = IllegalArgumentException.class)
  public void invalidLevelsAdjustCommandTest() throws IOException {
    String runScriptString = "levels-adjust";
    inputReader = new StringReader(runScriptString); /* Simulated user input */
    controller = new Controller(inputReader, mockView, mockModel);
    controller.run(); /* Execute the controller */
  }

  /**
   * Tests that an exception is thrown when the load command is given without
   * the necessary parameters. Verifies that the controller correctly handles
   * missing arguments in the load command.
   */
  @Test(expected = IllegalArgumentException.class)
  public void invalidIOCommandTest() throws IOException {
    String runScriptString = "load";
    inputReader = new StringReader(runScriptString); /* Simulated user input */
    controller = new Controller(inputReader, mockView, mockModel);
    controller.run(); /* Execute the controller */
  }

  /**
   * Tests that an exception is thrown when an invalid command that requires two
   * images is executed. Verifies that the controller properly handles missing
   * or incorrect arguments for commands like 'red-component'.
   */
  @Test(expected = IllegalArgumentException.class)
  public void invalidTwoImageCommandTest() throws IOException {
    String runScriptString = "red-component";
    inputReader = new StringReader(runScriptString); /* Simulated user input */
    controller = new Controller(inputReader, mockView, mockModel);
    controller.run(); /* Execute the controller */
  }

  /**
   * Tests that an {@link IllegalArgumentException} is thrown when the
   * "color-correct" command is used without the required two images.
   */
  @Test(expected = IllegalArgumentException.class)
  public void invalidTwoImageCommandWithSplitTest() throws IOException {
    String runScriptString = "color-correct";
    inputReader = new StringReader(runScriptString); /* Simulated user input */
    controller = new Controller(inputReader, mockView, mockModel);
    controller.run(); /* Execute the controller */
  }

  /**
   * Tests that an {@link IllegalArgumentException} is thrown when the
   * "brighten" command is used without the required increment value.
   */
  @Test(expected = IllegalArgumentException.class)
  public void invalidBrightenCommandTest() throws IOException {
    String runScriptString = "brighten";
    inputReader = new StringReader(runScriptString); /* Simulated user input */
    controller = new Controller(inputReader, mockView, mockModel);
    controller.run(); /* Execute the controller */
  }

  /**
   * Tests that an {@link IllegalArgumentException} is thrown when the
   * "brighten" command is used with invalid arguments (non-numeric increment).
   */
  @Test(expected = IllegalArgumentException.class)
  public void invalidBrightenIncrementCommandTest() throws IOException {
    String runScriptString = "brighten a manas manas-brighter";
    inputReader = new StringReader(runScriptString); /* Simulated user input */
    controller = new Controller(inputReader, mockView, mockModel);
    controller.run(); /* Execute the controller */
  }

  /**
   * Tests that an {@link IllegalArgumentException} is thrown when the
   * "rgb-split" command is used without the required input.
   */
  @Test(expected = IllegalArgumentException.class)
  public void invalidRGBSplitCommandTest() throws IOException {
    String runScriptString = "rgb-split";
    inputReader = new StringReader(runScriptString); /* Simulated user input */
    controller = new Controller(inputReader, mockView, mockModel);
    controller.run(); /* Execute the controller */
  }

  /**
   * Tests that an {@link IllegalArgumentException} is thrown when the
   * "rgb-combine" command is used without the required input.
   */
  @Test(expected = IllegalArgumentException.class)
  public void invalidRGBCombineCommandTest() throws IOException {
    String runScriptString = "rgb-combine";
    inputReader = new StringReader(runScriptString); /* Simulated user input */
    controller = new Controller(inputReader, mockView, mockModel);
    controller.run(); /* Execute the controller */
  }

  /**
   * Tests that an {@link IllegalArgumentException} is thrown when the "run"
   * command is used without required arguments.
   */
  @Test(expected = IllegalArgumentException.class)
  public void invalidRunCommandTest() throws IOException {
    String runScriptString = "run";
    inputReader = new StringReader(runScriptString); /* Simulated user input */
    controller = new Controller(inputReader, mockView, mockModel);
    controller.run(); /* Execute the controller */
  }

  /**
   * Tests that an {@link IllegalArgumentException} is thrown when the
   * "histogram" command is used without the required input.
   */
  @Test(expected = IllegalArgumentException.class)
  public void invalidHistogramCommandTest() throws IOException {
    String runScriptString = "histogram";
    inputReader = new StringReader(runScriptString); /* Simulated user input */
    controller = new Controller(inputReader, mockView, mockModel);
    controller.run(); /* Execute the controller */
  }

  /**
   * Tests the behavior when attempting to load a non-existent file, expecting
   * an error message.
   */
  @Test
  public void loadNonExistentFileTest() throws IOException {
    String runScriptString = "load bad_path sample";
    inputReader = new StringReader(runScriptString); /* Simulated user input */
    controller = new Controller(inputReader, mockView, mockModel);
    controller.run(); /* Execute the controller */
    List<String> expectedLog = new ArrayList<>() {
      {
        add("displayMessage: Enter commands (type 'exit' to quit) = \n");
        add("displayMessage: Processing Command = load bad_path sample");
        add("displayError: Processing Command = Can't read input file!");
        add("displayMessage: Executed command = true");
      }
    };
    System.out.println(log);
    assertEquals(expectedLog, log);
  }

  /**
   * Tests the initial prompt message when no input is provided.
   */
  @Test
  public void testInitialPrompt() throws IOException {
    String runScriptString = "";
    inputReader = new StringReader(runScriptString); /* Simulated user input */
    controller = new Controller(inputReader, mockView, mockModel);
    controller.run(); /* Execute the controller */
    List<String> expectedLog = new ArrayList<>() {
      { add("displayMessage: Enter commands (type 'exit' to quit) = \n"); }
    };
    System.out.println(log);
    assertEquals(expectedLog, log);
  }

  /**
   * Tests the exit message when the "exit" command is used.
   */
  @Test
  public void testExitMessage() throws IOException {
    String runScriptString = "exit";
    inputReader = new StringReader(runScriptString); /* Simulated user input */
    controller = new Controller(inputReader, mockView, mockModel);
    controller.run(); /* Execute the controller */
    String expectedExitMessage =
            "displayMessage: Thank you for using this program!";

    System.out.println(log);
    assertTrue(log.contains(expectedExitMessage));
  }
}