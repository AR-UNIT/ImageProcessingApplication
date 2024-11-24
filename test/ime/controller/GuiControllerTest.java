package ime.controller;

import static ime.controller.ControllerTestConstants.ALPHA_MATRIX_READ;
import static ime.controller.ControllerTestConstants.BLUE_MATRIX_READ;
import static ime.controller.ControllerTestConstants.GREEN_MATRIX_READ;
import static ime.controller.ControllerTestConstants.RED_MATRIX_READ;
import static ime.controller.ControllerTestConstants.convertTo2DPixelArray;
import static ime.model.ModelTest.compareImageMaps;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import ime.controller.gui.GUIController;
import ime.model.MockModel;
import ime.model.ModelInterface;
import ime.view.gui.GraphicalIMEInterface;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import org.junit.Before;
import org.junit.Test;

/**
 * This class tests the GuiController, and its interactions with a mock model
 * and mock gui view, ensuring that the controller is properly handling
 * different scenarios.
 */
public class GuiControllerTest {
  private ModelInterface mockModel;
  private GUIController controller;
  private GraphicalIMEInterface mockView;

  private List<String> log;
  private String currentImageName;
  private File selectedFile;

  /**
   * Sets up the mock objects and initializes the log list before each test.
   */
  @Before
  public void setUp() {
    log = new ArrayList<>();
    mockModel = new MockModel(log, false);
    selectedFile = new File(
            "test/ime/controller/testResources/controller_tests_image_ppm.ppm");
    mockView = new MockGuiView(log, selectedFile, null);
    currentImageName = "controller_tests_image_ppm";
  }

  /**
   * Test for the `go` method in the controller.
   * This test verifies that the `go` method correctly makes the appropriate
   * calls to the view. It checks if the "view()" method is logged after the
   * controller's `go()` method is called.
   */
  @Test
  public void testGo() {
    controller = new GUIController(mockModel, mockView);
    controller.startMethod();
    ArrayList<String> expectedLog = new ArrayList<>();
    expectedLog.add("view()");
    assertEquals(expectedLog, log);
  }

  /**
   * Test for loading a PPM image file.
   * This test verifies if the controller correctly handles loading a PPM image.
   * It checks the logs for method calls related to loading the image,
   * converting it, and displaying the result.
   */
  @Test
  public void testLoadImagePPM() {
    String imagePath =
            "test/ime/controller/testResources/controller_tests_image_ppm.ppm";
    File selectedFile = new File(imagePath);
    mockView = new MockGuiView(log, selectedFile, null);
    controller = new GUIController(mockModel, mockView);
    String currentImageName = "controller_tests_image_ppm";
    String currentImageNameHistogram = currentImageName + "-histogram";
    controller.loadImage(); /* Execute the controller */

    List<String> expectedLog = new ArrayList<>() {
      {
        add("loadImage()");
        add("convertAndStoreImage(" + currentImageName + ", {red="
                + RED_MATRIX_READ + ", green=" + GREEN_MATRIX_READ + ", blue="
                + BLUE_MATRIX_READ + ", alpha=" + ALPHA_MATRIX_READ + "})");
        add("displayMessage: Image loaded successfully.");
        add("convertAndFetchImage(" + currentImageName + ")");
        add("updateImageDisplay: ");
        add("commandFactoryCall(histogram, [histogram, " + currentImageName
                + ", " + currentImageNameHistogram + "])");
        add("convertAndFetchImage(" + currentImageNameHistogram + ")");
      }
    };
    assertEquals(expectedLog, log);
  }

  /**
   * Test for loading a PNG image file.
   * This test verifies if the controller can correctly handle loading a PNG
   * image. Similar to the PPM test, it checks for proper logging of image
   * conversion, display, and histogram generation.
   */
  @Test
  public void testLoadImagePNG() {
    String imagePath =
            "test/ime/controller/testResources/controller_tests_image_png.png";
    File selectedFile = new File(imagePath);
    mockView = new MockGuiView(log, selectedFile, null);
    controller = new GUIController(mockModel, mockView);
    String currentImageName = "controller_tests_image_png";
    String currentImageNameHistogram = currentImageName + "-histogram";
    controller.loadImage(); /* Execute the controller */

    List<String> expectedLog = new ArrayList<>() {
      {
        add("loadImage()"); // Log entry for loadImage method call
        add("convertAndStoreImage(" + currentImageName
                + ", {red=" + RED_MATRIX_READ + ", "
                + "green=" + GREEN_MATRIX_READ + ", blue=" + BLUE_MATRIX_READ
                + ", alpha=" + ALPHA_MATRIX_READ + "})");
        add("displayMessage: Image loaded successfully.");
        add("convertAndFetchImage(" + currentImageName + ")");
        add("updateImageDisplay: "); // Log for updating image display
        add("commandFactoryCall(histogram, [histogram, " + currentImageName
                + ", " + currentImageNameHistogram + "])");
        add("convertAndFetchImage(" + currentImageNameHistogram + ")");
      }
    };
    assertEquals(expectedLog, log);
  }

  /**
   * Test for loading a JPG image file.
   * This test checks if the controller correctly handles loading a JPG image.
   * It validates the logging for loading, conversion, image display, and
   * histogram creation.
   */
  @Test
  public void testLoadImageJPG() throws IOException {
    String imagePath =
            "test/ime/controller/testResources/controller_tests_image_jpg.jpg";
    File selectedFile = new File(imagePath);
    mockView = new MockGuiView(log, selectedFile, null);
    controller = new GUIController(mockModel, mockView);
    String currentImageName = "controller_tests_image_jpg";
    String currentImageNameHistogram = currentImageName + "-histogram";
    controller.loadImage(); /* Execute the controller */

    List<String> expectedLog = new ArrayList<>() {
      {
        add("loadImage()");
        add("convertAndStoreImage(" + currentImageName + ", {red="
                + RED_MATRIX_READ + ", green=" + GREEN_MATRIX_READ + ", blue="
                + BLUE_MATRIX_READ + ", alpha=" + ALPHA_MATRIX_READ + "})");
        add("displayMessage: Image loaded successfully.");
        add("convertAndFetchImage(" + currentImageName + ")");
        add("updateImageDisplay: ");
        add("commandFactoryCall(histogram, [histogram, " + currentImageName
                + ", " + currentImageNameHistogram + "])");
        add("convertAndFetchImage(" + currentImageNameHistogram + ")");
      }
    };
    assertTrue(expectedLog.stream().allMatch(expectedMessage
        -> expectedLog.stream().anyMatch(
          log -> log.contains(expectedMessage))));
  }

  /**
   * Test for error handling when loading an image fails.
   * This test ensures that the controller behaves correctly when it fails to
   * load an image. It checks that an appropriate error message is logged when
   * the image load fails.
   */
  @Test
  public void testLoadImageErrorCase() {
    String imagePath =
            "test/ime/controller/testResources/controller_tests_image_ppm.ppm";
    File selectedFile = new File(imagePath);
    mockView = new MockGuiView(log, selectedFile, "failLoading");
    controller = new GUIController(mockModel, mockView);
    controller.loadImage(); /* Execute the controller */

    List<String> expectedLog = new ArrayList<>() {
      {
        add("loadImage()");
        add("displayError: null");
      }
    };
    assertEquals(expectedLog, log);
  }

  /**
   * Tests the saving of a PPM image file and verifies the log output.
   * Simulates loading a PPM image, saving it, and then loading the saved image.
   */
  @Test
  public void testSaveImagePPM() throws IOException {
    String filePath =
            "test/ime/controller/testOutputs/controller_tests_image_ppm";
    String currentImageName = "sample";
    String fileExtension = "ppm";
    File selectedFile = new File(filePath);
    mockView = new MockGuiView(log, selectedFile, null);
    controller = new GUIController(mockModel, mockView);
    controller.saveImage(filePath, currentImageName, fileExtension);
    List<String> expectedLog = new ArrayList<>() {
      {
        add("convertAndFetchImage(" + currentImageName + ")");
        add("displayMessage: Image saved to = " + filePath);
      }
    };
    assertEquals(expectedLog, log);
  }

  /**
   * Test for saving an image as a PNG file.
   * This test verifies that the controller's `saveImage` method correctly saves
   * a PNG image. It checks that the appropriate methods are called, including
   * converting the image and saving it to the specified file path.
   */
  @Test
  public void testSaveImagePNG() throws IOException {
    String filePath =
            "test/ime/controller/testOutputs/controller_tests_image_png";
    String currentImageName = "sample";
    String fileExtension = "png";
    File selectedFile = new File(filePath);
    mockView = new MockGuiView(log, selectedFile, null);
    controller = new GUIController(mockModel, mockView);
    controller.saveImage(filePath, currentImageName, fileExtension);

    List<String> expectedLog = new ArrayList<>() {
      {
        add("convertAndFetchImage(" + currentImageName + ")");
        add("displayMessage: Image saved to = " + filePath);
      }
    };
    assertEquals(expectedLog, log);
  }

  /**
   * Test for saving an image as a JPG file.
   * This test verifies that the controller's `saveImage` method correctly saves
   * a JPG image. It ensures that appropriate model and view calls are made from
   * controller, and the file is saved to the correct path.
   */
  @Test
  public void testSaveImageJPG() throws IOException {
    String filePath =
            "test/ime/controller/testOutputs/controller_tests_image_jpg";
    String currentImageName = "sample";
    String fileExtension = "jpg";
    File selectedFile = new File(filePath);
    mockView = new MockGuiView(log, selectedFile, null);
    controller = new GUIController(mockModel, mockView);
    controller.saveImage(filePath, currentImageName, fileExtension);

    List<String> expectedLog = new ArrayList<>() {
      {
        add("convertAndFetchImage(" + currentImageName + ")");
        add("displayMessage: Image saved to = " + filePath);
      }
    };
    assertEquals(expectedLog, log);
  }

  /**
   * Test for fetching the current image from the controller.
   * This test verifies that the controller correctly fetches the current image
   * and does the appropriate method calls in model and view. It also compares
   * the fetched image to an expected image to ensure correctness.
   */
  @Test
  public void getCurrentImageTest() throws IOException {
    controller = new GUIController(mockModel, mockView);
    List<String> commandTokens = new ArrayList<>();
    String outputPath = "outputImage";
    String imageName = "currentImage";
    commandTokens.add("getCurrentImage");
    commandTokens.add(outputPath);
    commandTokens.add(imageName);

    ArrayList<String> expectedLog = new ArrayList<>();
    String filePath =
            "test/ime/controller/testOutputs/controller_tests_image_png.png";
    BufferedImage expectedCurrentImage = ImageIO.read(new File(filePath));

    expectedLog.add("convertAndFetchImage(currentImage)");

    BufferedImage fetchedCurrentImage = controller.getCurrentImage(imageName);

    assertEquals(expectedLog, log);
    assertTrue(compareImageMaps(convertTo2DPixelArray(expectedCurrentImage),
            convertTo2DPixelArray(fetchedCurrentImage)));
  }

  /**
   * Test for the `brighten` method when no brightness intensity is provided in
   * the prompt. This test ensures that when the user does not provide a value
   * for the brightness intensity, the appropriate error message is logged.
   */
  @Test
  public void brightenTest1() {
    /* brighten intensity not set in the prompt */
    mockView = new MockGuiView(log, selectedFile, null);
    controller = new GUIController(mockModel, mockView);
    controller.loadImage();
    controller.brighten();

    List<String> expectedLog = new ArrayList<String>() {
      {
        add("loadImage()");
        add("convertAndStoreImage(" + currentImageName + ", {red="
                + RED_MATRIX_READ + ", green=" + GREEN_MATRIX_READ + ", blue="
                + BLUE_MATRIX_READ + ", alpha=" + ALPHA_MATRIX_READ + "})");
        add("displayMessage: Image loaded successfully.");
        add("convertAndFetchImage(" + currentImageName + ")");
        add("updateImageDisplay: ");
        add("commandFactoryCall(histogram, [histogram, "
                + "controller_tests_image_ppm, "
                + "controller_tests_image_ppm-histogram])");
        add("convertAndFetchImage(controller_tests_image_ppm-histogram)");
        add("offerPromptToGetOperationParameters(Enter brighten intensity "
                + "(-255 to 255): )");
        add("displayError: No value passed!");
      }
    };

    assertEquals(expectedLog, log);
  }

  /**
   * Test for the `brighten` method when the brightness intensity is set
   * correctly in the prompt. This test ensures that when the user provides a
   * valid brightness intensity (255), the `brighten` method processes the input
   * correctly, updates the image, and checks required calls to model and view
   * via mocks logging them.
   */
  @Test
  public void brightenTest2() {
    /* brighten intensity set in the prompt */
    mockView = new MockGuiView(log, selectedFile, "255");
    controller = new GUIController(mockModel, mockView);
    controller.loadImage();
    controller.brighten();

    List<String> expectedLog = new ArrayList<>() {
      {
        add("loadImage()");
        add("convertAndStoreImage(" + currentImageName + ", {red="
                + RED_MATRIX_READ + ", green=" + GREEN_MATRIX_READ + ", blue="
                + BLUE_MATRIX_READ + ", alpha=" + ALPHA_MATRIX_READ + "})");
        add("displayMessage: Image loaded successfully.");
        add("convertAndFetchImage(" + currentImageName + ")");
        add("updateImageDisplay: ");
        add("commandFactoryCall(histogram, [histogram, "
                + "controller_tests_image_ppm, "
                + "controller_tests_image_ppm-histogram])");
        add("convertAndFetchImage(controller_tests_image_ppm-histogram)");
        add("offerPromptToGetOperationParameters(Enter brighten intensity "
                + "(-255 to 255): )");
        add("commandFactoryCall(brighten, [brighten, 255, "
                + "controller_tests_image_ppm, "
                + "controller_tests_image_ppm-brighten])");
        add("convertAndFetchImage(controller_tests_image_ppm-brighten)");
        add("updateImageDisplay: ");
        add("commandFactoryCall(histogram, [histogram, "
                + "controller_tests_image_ppm-brighten, "
                + "controller_tests_image_ppm-brighten-histogram])");
        add("convertAndFetchImage(controller_tests_image_ppm-brighten-"
                + "histogram)");
        add("offerToSaveImage(controller_tests_image_ppm-brighten, ppm)");
        add("convertAndFetchImage(controller_tests_image_ppm-brighten)");
        add("updateImageDisplay: ");
        add("commandFactoryCall(histogram, [histogram, "
                + "controller_tests_image_ppm-brighten, "
                + "controller_tests_image_ppm-brighten-histogram])");
        add("convertAndFetchImage(controller_tests_image_ppm-brighten-"
                + "histogram)");
      }
    };

    assertEquals(expectedLog, log);
  }

  /**
   * Test for the `brighten` method when the brightness intensity is set to an
   * invalid value in the prompt. This test ensures that when the user provides
   * an invalid brightness intensity value (such as 1000), the system handles
   * exception appropriately and displays error at view.
   */
  @Test
  public void brightenTest3() {
    /* brighten intensity set to invalid in the prompt */
    controller = new GUIController(mockModel, mockView);
    controller.loadImage();

    mockView = new MockGuiView(log, null, "1000");
    mockModel = new MockModel(log, true);
    controller = new GUIController(mockModel, mockView);
    controller.brighten();

    List<String> expectedLog = new ArrayList<>() {
      {
        add("loadImage()");
        add("convertAndStoreImage(" + currentImageName + ", {red="
                + RED_MATRIX_READ + ", green=" + GREEN_MATRIX_READ + ", blue="
                + BLUE_MATRIX_READ + ", alpha=" + ALPHA_MATRIX_READ + "})");
        add("displayMessage: Image loaded successfully.");
        add("convertAndFetchImage(" + currentImageName + ")");
        add("updateImageDisplay: ");
        add("commandFactoryCall(histogram, [histogram, "
                + "controller_tests_image_ppm, "
                + "controller_tests_image_ppm-histogram])");
        add("convertAndFetchImage(controller_tests_image_ppm-histogram)");
        add("offerPromptToGetOperationParameters(Enter brighten intensity "
                + "(-255 to 255): )");
        add("displayError: Exception trying to process: "
                + "commandFactoryCall(brighten, "
                + "[brighten, 1000, controller_tests_image_ppm, "
                + "controller_tests_image_ppm-brighten])");
      }
    };

    assertEquals(expectedLog, log);
  }

  /**
   * Test for the `compress` method when no compression ratio is provided in the
   * prompt. This test ensures that when the user does not provide a value for
   * the compression ratio, the appropriate error message is logged.
   */
  @Test
  public void compressTest1() {
    /* compression ratio not set in the prompt */
    controller = new GUIController(mockModel, mockView);
    controller.loadImage();
    controller.compress();

    List<String> expectedLog = new ArrayList<>() {
      {
        add("loadImage()");
        add("convertAndStoreImage(" + currentImageName + ", {red="
                + RED_MATRIX_READ + ", green=" + GREEN_MATRIX_READ + ", blue="
                + BLUE_MATRIX_READ + ", alpha=" + ALPHA_MATRIX_READ + "})");
        add("displayMessage: Image loaded successfully.");
        add("convertAndFetchImage(" + currentImageName + ")");
        add("updateImageDisplay: ");
        add("commandFactoryCall(histogram, [histogram, "
                + "controller_tests_image_ppm, "
                + "controller_tests_image_ppm-histogram])");
        add("convertAndFetchImage(controller_tests_image_ppm-histogram)");
        add("offerPromptToGetOperationParameters(Enter compression ratio "
                + "(0-100): )");
        add("displayError: No value passed!");
      }
    };

    assertEquals(expectedLog, log);
  }

  /**
   * Test for the `compress` method when the compression ratio is set correctly
   * in the prompt. This test ensures that when the user provides a valid
   * compression ratio (20), the `compress` method processes the input
   * correctly, updates the image, and checks necessary function calls in model
   * and view by checking logs from mocks.
   */
  @Test
  public void compressTest2() {
    /* compression ratio set in the prompt */
    mockView = new MockGuiView(log, selectedFile, "20");
    controller = new GUIController(mockModel, mockView);
    controller.loadImage();
    controller.compress();

    List<String> expectedLog = new ArrayList<String>() {
      {
        add("loadImage()");
        add("convertAndStoreImage(" + currentImageName + ", {red="
                + RED_MATRIX_READ + ", green=" + GREEN_MATRIX_READ + ", blue="
                + BLUE_MATRIX_READ + ", alpha=" + ALPHA_MATRIX_READ + "})");
        add("displayMessage: Image loaded successfully.");
        add("convertAndFetchImage(controller_tests_image_ppm)");
        add("updateImageDisplay: ");
        add("commandFactoryCall(histogram, [histogram, "
                + "controller_tests_image_ppm, "
                + "controller_tests_image_ppm-histogram])");
        add("convertAndFetchImage(controller_tests_image_ppm-histogram)");
        add("offerPromptToGetOperationParameters(Enter compression ratio "
                + "(0-100): )");
        add("commandFactoryCall(compress, [compress, 20, "
                + "controller_tests_image_ppm, "
                + "controller_tests_image_ppm-compress])");
        add("convertAndFetchImage(controller_tests_image_ppm-compress)");
        add("updateImageDisplay: ");
        add("commandFactoryCall(histogram, [histogram, "
                + "controller_tests_image_ppm-compress, "
                + "controller_tests_image_ppm-compress-histogram])");
        add("convertAndFetchImage(controller_tests_image_ppm-compress-"
                + "histogram)");
        add("offerToSaveImage(controller_tests_image_ppm-compress, ppm)");
        add("convertAndFetchImage(controller_tests_image_ppm-compress)");
        add("updateImageDisplay: ");
        add("commandFactoryCall(histogram, [histogram, "
                + "controller_tests_image_ppm-compress, "
                + "controller_tests_image_ppm-compress-histogram])");
        add("convertAndFetchImage(controller_tests_image_ppm-compress-"
                + "histogram)");
      }
    };

    assertEquals(expectedLog, log);
  }

  /**
   * Test for the `compress` method when the compression ratio is set to invalid
   * value in prompt. This test ensures that when the user provides an invalid
   * compression ratio (such as 1000), the system handles exception
   * appropriately and displays error at view.
   */
  @Test
  public void compressTest3() {
    /* compression ratio set to invalid in the prompt */
    controller = new GUIController(mockModel, mockView);
    controller.loadImage();

    mockView = new MockGuiView(log, null, "1000");
    mockModel = new MockModel(log, true);
    controller = new GUIController(mockModel, mockView);
    controller.compress();

    List<String> expectedLog = new ArrayList<>() {
      {
        add("loadImage()");
        add("convertAndStoreImage(" + currentImageName + ", {red="
                + RED_MATRIX_READ + ", green=" + GREEN_MATRIX_READ + ", blue="
                + BLUE_MATRIX_READ + ", alpha=" + ALPHA_MATRIX_READ + "})");
        add("displayMessage: Image loaded successfully.");
        add("convertAndFetchImage(" + currentImageName + ")");
        add("updateImageDisplay: ");
        add("commandFactoryCall(histogram, [histogram, "
                + "controller_tests_image_ppm, "
                + "controller_tests_image_ppm-histogram])");
        add("convertAndFetchImage(controller_tests_image_ppm-histogram)");
        add("offerPromptToGetOperationParameters(Enter compression ratio "
                + "(0-100): )");
        add("displayError: Exception trying to process: "
                + "commandFactoryCall(compress, "
                + "[compress, 1000, controller_tests_image_ppm, "
                + "controller_tests_image_ppm-compress])");
      }
    };

    assertEquals(expectedLog, log);
  }

  /**
   * Test for the horizontal flip operation applied to the full image.
   * This test verifies that when the user applies a horizontal flip to the
   * image, the expected sequence of method calls and log entries are generated.
   * The test assumes that no additional inputs are required for the horizontal
   * flip.
   */
  @Test
  public void horizontalFlipTest1() {
    /* no inputs in horizontal flip operation, simply applying horizontal flip
     * to full image */
    controller = new GUIController(mockModel, mockView);
    controller.loadImage();
    controller.horizontalFlip();

    String currentImageNameHistogram = currentImageName + "-histogram";

    List<String> expectedLog = new ArrayList<>() {
      {
        add("loadImage()");
        add("convertAndStoreImage(" + currentImageName + ", {red="
                + RED_MATRIX_READ + ", green=" + GREEN_MATRIX_READ + ", blue="
                + BLUE_MATRIX_READ + ", alpha=" + ALPHA_MATRIX_READ + "})");
        add("displayMessage: Image loaded successfully.");
        add("convertAndFetchImage(" + currentImageName + ")");
        add("updateImageDisplay: ");
        add("commandFactoryCall(histogram, [histogram, " + currentImageName
                + ", " + currentImageNameHistogram + "])");
        add("convertAndFetchImage(controller_tests_image_ppm-histogram)");
        add("commandFactoryCall(horizontal-flip, [horizontal-flip, "
                + "controller_tests_image_ppm,"
                + " controller_tests_image_ppm-horizontal-flip])");
        add("convertAndFetchImage(controller_tests_image_ppm-horizontal-flip)");
        add("updateImageDisplay: ");
        add("commandFactoryCall(histogram, [histogram, "
                + "controller_tests_image_ppm-horizontal-flip, "
                + "controller_tests_image_ppm-horizontal-flip-histogram])");
        add("convertAndFetchImage(controller_tests_image_ppm-horizontal-flip-"
                + "histogram)");
        add("offerToSaveImage(controller_tests_image_ppm-horizontal-flip, "
                + "ppm)");
        add("convertAndFetchImage(controller_tests_image_ppm-horizontal-flip)");
        add("updateImageDisplay: ");
        add("commandFactoryCall(histogram, [histogram, "
                + "controller_tests_image_ppm-horizontal-flip, "
                + "controller_tests_image_ppm-horizontal-flip-histogram])");
        add("convertAndFetchImage(controller_tests_image_ppm-horizontal-flip-"
                + "histogram)");
      }
    };

    assertEquals(expectedLog, log);
  }

  /**
   * Test for the vertical flip operation applied to the full image.
   * This test verifies that when the user applies a vertical flip to the image,
   * the expected sequence of method calls and log entries are generated.
   * The test assumes that no additional inputs are required for the vertical
   * flip.
   */
  @Test
  public void verticalFlipTest1() {
    /* no inputs in vertical flip operation, simply applying horizontal flip to
     * full image */
    controller = new GUIController(mockModel, mockView);
    controller.loadImage();
    controller.verticalFlip();

    List<String> expectedLog = new ArrayList<String>() {
      {
        add("loadImage()");
        add("convertAndStoreImage(" + currentImageName + ", {red="
                + RED_MATRIX_READ + ", green=" + GREEN_MATRIX_READ + ", blue="
                + BLUE_MATRIX_READ + ", alpha=" + ALPHA_MATRIX_READ + "})");
        add("displayMessage: Image loaded successfully.");
        add("convertAndFetchImage(controller_tests_image_ppm)");
        add("updateImageDisplay: ");
        add("commandFactoryCall(histogram, [histogram, "
                + "controller_tests_image_ppm, "
                + "controller_tests_image_ppm-histogram])");
        add("convertAndFetchImage(controller_tests_image_ppm-histogram)");
        add("commandFactoryCall(vertical-flip, [vertical-flip, "
                + "controller_tests_image_ppm, "
                + "controller_tests_image_ppm-vertical-flip])");
        add("convertAndFetchImage(controller_tests_image_ppm-vertical-flip)");
        add("updateImageDisplay: ");
        add("commandFactoryCall(histogram, [histogram, "
                + "controller_tests_image_ppm-vertical-flip, "
                + "controller_tests_image_ppm-vertical-flip-histogram])");
        add("convertAndFetchImage(controller_tests_image_ppm-vertical-flip-"
                + "histogram)");
        add("offerToSaveImage(controller_tests_image_ppm-vertical-flip, ppm)");
        add("convertAndFetchImage(controller_tests_image_ppm-vertical-flip)");
        add("updateImageDisplay: ");
        add("commandFactoryCall(histogram, [histogram, "
                + "controller_tests_image_ppm-vertical-flip, "
                + "controller_tests_image_ppm-vertical-flip-histogram])");
        add("convertAndFetchImage(controller_tests_image_ppm-vertical-flip-"
                + "histogram)");
      }
    };

    assertEquals(expectedLog, log);
  }

  /**
   * Tests the functionality of the red component extraction operation.
   * This test simulates loading an image and extracting its red component,
   * and verifies the series of log entries produced during this process.
   */
  @Test
  public void redComponentTest1() {
    /* no inputs in redcomponent operation, simply get red component of image */

    controller = new GUIController(mockModel, mockView);
    controller.loadImage();
    controller.redComp();
    List<String> expectedLog = new ArrayList<String>() {
      {
        add("loadImage()");
        add("convertAndStoreImage(" + currentImageName + ", {red="
                + RED_MATRIX_READ + ", green=" + GREEN_MATRIX_READ + ", blue="
                + BLUE_MATRIX_READ + ", alpha=" + ALPHA_MATRIX_READ + "})");
        add("displayMessage: Image loaded successfully.");
        add("convertAndFetchImage(controller_tests_image_ppm)");
        add("updateImageDisplay: ");
        add("commandFactoryCall(histogram, [histogram, "
                + "controller_tests_image_ppm, "
                + "controller_tests_image_ppm-histogram])");
        add("convertAndFetchImage(controller_tests_image_ppm-histogram)");
        add("commandFactoryCall(red-component, [red-component, "
                + "controller_tests_image_ppm, "
                + "controller_tests_image_ppm-red-component])");
        add("convertAndFetchImage(controller_tests_image_ppm-red-component)");
        add("updateImageDisplay: ");
        add("commandFactoryCall(histogram, [histogram, "
                + "controller_tests_image_ppm-red-component, "
                + "controller_tests_image_ppm-red-component-histogram])");
        add("convertAndFetchImage(controller_tests_image_ppm-red-component-"
                + "histogram)");
        add("offerToSaveImage(controller_tests_image_ppm-red-component, ppm)");
        add("convertAndFetchImage(controller_tests_image_ppm-red-component)");
        add("updateImageDisplay: ");
        add("commandFactoryCall(histogram, [histogram, "
                + "controller_tests_image_ppm-red-component,"
                + " controller_tests_image_ppm-red-component-histogram])");
        add("convertAndFetchImage(controller_tests_image_ppm-red-component-"
                + "histogram)");
      }
    };

    assertEquals(expectedLog, log);
  }

  /**
   * Tests the functionality of the green component extraction operation.
   * This test simulates loading an image and extracting its green component,
   * and verifies the series of log entries produced during this process.
   */
  @Test
  public void greenComponentTest1() {
    /* no inputs in greenComponent operation, simply get green component of
     * image */

    controller = new GUIController(mockModel, mockView);
    controller.loadImage();
    controller.greenComp();
    List<String> expectedLog = new ArrayList<String>() {
      {
        add("loadImage()");
        add("convertAndStoreImage(" + currentImageName + ", {red="
                + RED_MATRIX_READ + ", green=" + GREEN_MATRIX_READ + ", blue="
                + BLUE_MATRIX_READ + ", alpha=" + ALPHA_MATRIX_READ + "})");
        add("displayMessage: Image loaded successfully.");
        add("convertAndFetchImage(controller_tests_image_ppm)");
        add("updateImageDisplay: ");
        add("commandFactoryCall(histogram, [histogram, "
                + "controller_tests_image_ppm, "
                + "controller_tests_image_ppm-histogram])");
        add("convertAndFetchImage(controller_tests_image_ppm-histogram)");
        add("commandFactoryCall(green-component, [green-component, "
                + "controller_tests_image_ppm, "
                + "controller_tests_image_ppm-green-component])");
        add("convertAndFetchImage(controller_tests_image_ppm-green-component)");
        add("updateImageDisplay: ");
        add("commandFactoryCall(histogram, [histogram, "
                + "controller_tests_image_ppm-green-component, "
                + "controller_tests_image_ppm-green-component-histogram])");
        add("convertAndFetchImage(controller_tests_image_ppm-green-component-"
                + "histogram)");
        add("offerToSaveImage(controller_tests_image_ppm-green-component, "
                + "ppm)");
        add("convertAndFetchImage(controller_tests_image_ppm-green-component)");
        add("updateImageDisplay: ");
        add("commandFactoryCall(histogram, [histogram, "
                + "controller_tests_image_ppm-green-component, "
                + "controller_tests_image_ppm-green-component-histogram])");
        add("convertAndFetchImage(controller_tests_image_ppm-green-component-"
                + "histogram)");
      }
    };

    assertEquals(expectedLog, log);
  }

  /**
   * Tests the functionality of the blue component extraction operation.
   * This test simulates loading an image and extracting its blue component,
   * and verifies the series of log entries produced during this process.
   */
  @Test
  public void blueComponentTest1() {
    /* no inputs in blueComponent operation, simply get blue component of image
     */

    controller = new GUIController(mockModel, mockView);
    controller.loadImage();
    controller.blueComp();
    List<String> expectedLog = new ArrayList<String>() {
      {
        add("loadImage()");
        add("convertAndStoreImage(" + currentImageName + ", {red="
                + RED_MATRIX_READ + ", green=" + GREEN_MATRIX_READ + ", blue="
                + BLUE_MATRIX_READ + ", alpha=" + ALPHA_MATRIX_READ + "})");
        add("displayMessage: Image loaded successfully.");
        add("convertAndFetchImage(controller_tests_image_ppm)");
        add("updateImageDisplay: ");
        add("commandFactoryCall(histogram, [histogram, "
                + "controller_tests_image_ppm, "
                + "controller_tests_image_ppm-histogram])");
        add("convertAndFetchImage(controller_tests_image_ppm-histogram)");
        add("commandFactoryCall(blue-component, [blue-component, "
                + "controller_tests_image_ppm, "
                + "controller_tests_image_ppm-blue-component])");
        add("convertAndFetchImage(controller_tests_image_ppm-blue-component)");
        add("updateImageDisplay: ");
        add("commandFactoryCall(histogram, [histogram, "
                + "controller_tests_image_ppm-blue-component, "
                + "controller_tests_image_ppm-blue-component-histogram])");
        add("convertAndFetchImage(controller_tests_image_ppm-blue-component-"
                + "histogram)");
        add("offerToSaveImage(controller_tests_image_ppm-blue-component, ppm)");
        add("convertAndFetchImage(controller_tests_image_ppm-blue-component)");
        add("updateImageDisplay: ");
        add("commandFactoryCall(histogram, [histogram, "
                + "controller_tests_image_ppm-blue-component, "
                + "controller_tests_image_ppm-blue-component-histogram])");
        add("convertAndFetchImage(controller_tests_image_ppm-blue-component-"
                + "histogram)");
      }
    };

    assertEquals(expectedLog, log);
  }

  /**
   * Tests the functionality of the value component extraction operation.
   * This test simulates loading an image and extracting its value component,
   * and verifies the series of log entries produced during this process.
   */
  @Test
  public void valueComponentTest1() {
    /* no inputs in valueComponent operation, simply get value component of
     * image */

    controller = new GUIController(mockModel, mockView);
    controller.loadImage();
    controller.valueComp();
    List<String> expectedLog = new ArrayList<String>() {
      {
        add("loadImage()");
        add("convertAndStoreImage(" + currentImageName + ", {red="
                + RED_MATRIX_READ + ", green=" + GREEN_MATRIX_READ + ", blue="
                + BLUE_MATRIX_READ + ", alpha=" + ALPHA_MATRIX_READ + "})");
        add("displayMessage: Image loaded successfully.");
        add("convertAndFetchImage(controller_tests_image_ppm)");
        add("updateImageDisplay: ");
        add("commandFactoryCall(histogram, [histogram, "
                + "controller_tests_image_ppm, "
                + "controller_tests_image_ppm-histogram])");
        add("convertAndFetchImage(controller_tests_image_ppm-histogram)");
        add("commandFactoryCall(value-component, [value-component, "
                + "controller_tests_image_ppm, "
                + "controller_tests_image_ppm-value-component])");
        add("convertAndFetchImage(controller_tests_image_ppm-value-component)");
        add("updateImageDisplay: ");
        add("commandFactoryCall(histogram, [histogram, "
                + "controller_tests_image_ppm-value-component, "
                + "controller_tests_image_ppm-value-component-histogram])");
        add("convertAndFetchImage(controller_tests_image_ppm-value-component-"
                + "histogram)");
        add("offerToSaveImage(controller_tests_image_ppm-value-component, "
                + "ppm)");
        add("convertAndFetchImage(controller_tests_image_ppm-value-component)");
        add("updateImageDisplay: ");
        add("commandFactoryCall(histogram, [histogram, "
                + "controller_tests_image_ppm-value-component, "
                + "controller_tests_image_ppm-value-component-histogram])");
        add("convertAndFetchImage(controller_tests_image_ppm-value-component-"
                + "histogram)");
      }
    };

    assertEquals(expectedLog, log);
  }

  /**
   * Tests the functionality of the intensity component extraction operation.
   * This test simulates loading an image and extracting its intensity
   * component, and verifies the series of log entries produced during this
   * process.
   */
  @Test
  public void intensityComponentTest1() {
    /* no inputs in intensityComponent operation, simply get intensity component
     * of image */

    controller = new GUIController(mockModel, mockView);
    controller.loadImage();
    controller.intensityComp();
    List<String> expectedLog = new ArrayList<String>() {
      {
        add("loadImage()");
        add("convertAndStoreImage(" + currentImageName + ", {red="
                + RED_MATRIX_READ + ", green=" + GREEN_MATRIX_READ + ", blue="
                + BLUE_MATRIX_READ + ", alpha=" + ALPHA_MATRIX_READ + "})");
        add("displayMessage: Image loaded successfully.");
        add("convertAndFetchImage(controller_tests_image_ppm)");
        add("updateImageDisplay: ");
        add("commandFactoryCall(histogram, [histogram, "
                + "controller_tests_image_ppm, "
                + "controller_tests_image_ppm-histogram])");
        add("convertAndFetchImage(controller_tests_image_ppm-histogram)");
        add("commandFactoryCall(intensity-component, [intensity-component, "
                + "controller_tests_image_ppm, "
                + "controller_tests_image_ppm-intensity-component])");
        add("convertAndFetchImage(controller_tests_image_ppm-intensity-"
                + "component)");
        add("updateImageDisplay: ");
        add("commandFactoryCall(histogram, [histogram, "
                + "controller_tests_image_ppm-intensity-component, "
                + "controller_tests_image_ppm-intensity-component-histogram])");
        add("convertAndFetchImage(controller_tests_image_ppm-intensity-"
                + "component-histogram)");
        add("offerToSaveImage(controller_tests_image_ppm-intensity-component, "
                + "ppm)");
        add("convertAndFetchImage(controller_tests_image_ppm-intensity-"
                + "component)");
        add("updateImageDisplay: ");
        add("commandFactoryCall(histogram, [histogram, "
                + "controller_tests_image_ppm-intensity-component, "
                + "controller_tests_image_ppm-intensity-component-histogram])");
        add("convertAndFetchImage(controller_tests_image_ppm-intensity-"
                + "component-histogram)");
      }
    };

    assertEquals(expectedLog, log);
  }

  /**
   * Tests the functionality of the luma component extraction operation.
   * This test simulates loading an image and extracting its luma component,
   * and verifies the series of log entries produced during this process.
   */
  @Test
  public void lumaComponentTest1() {
    /* no inputs in lumaComponent operation, simply get luma component of image
     */

    controller = new GUIController(mockModel, mockView);
    controller.loadImage();
    controller.lumaComp();
    List<String> expectedLog = new ArrayList<String>() {
      {
        add("loadImage()");
        add("convertAndStoreImage(" + currentImageName + ", {red="
                + RED_MATRIX_READ + ", green=" + GREEN_MATRIX_READ + ", blue="
                + BLUE_MATRIX_READ + ", alpha=" + ALPHA_MATRIX_READ + "})");
        add("displayMessage: Image loaded successfully.");
        add("convertAndFetchImage(controller_tests_image_ppm)");
        add("updateImageDisplay: ");
        add("commandFactoryCall(histogram, [histogram, "
                + "controller_tests_image_ppm, "
                + "controller_tests_image_ppm-histogram])");
        add("convertAndFetchImage(controller_tests_image_ppm-histogram)");
        add("commandFactoryCall(luma-component, [luma-component, "
                + "controller_tests_image_ppm, "
                + "controller_tests_image_ppm-luma-component])");
        add("convertAndFetchImage(controller_tests_image_ppm-luma-component)");
        add("updateImageDisplay: ");
        add("commandFactoryCall(histogram, [histogram, "
                + "controller_tests_image_ppm-luma-component, "
                + "controller_tests_image_ppm-luma-component-histogram])");
        add("convertAndFetchImage(controller_tests_image_ppm-luma-component-"
                + "histogram)");
        add("offerToSaveImage(controller_tests_image_ppm-luma-component, ppm)");
        add("convertAndFetchImage(controller_tests_image_ppm-luma-component)");
        add("updateImageDisplay: ");
        add("commandFactoryCall(histogram, [histogram, "
                + "controller_tests_image_ppm-luma-component, "
                + "controller_tests_image_ppm-luma-component-histogram])");
        add("convertAndFetchImage(controller_tests_image_ppm-luma-component-"
                + "histogram)");
      }
    };

    assertEquals(expectedLog, log);
  }

  /**
   * Tests the functionality of the color correction operation.
   * This test simulates loading an image and applying color correction to the
   * image, and verifies the series of log entries produced during this process.
   */
  @Test
  public void colorCorrectionTest1() {
    /* no inputs in color correction operation, simply get color corrected image
     */

    controller = new GUIController(mockModel, mockView);
    controller.loadImage();
    controller.colorCorrection();
    List<String> expectedLog = new ArrayList<String>() {
      {
        add("loadImage()");
        add("convertAndStoreImage(" + currentImageName + ", {red="
                + RED_MATRIX_READ + ", green=" + GREEN_MATRIX_READ + ", blue="
                + BLUE_MATRIX_READ + ", alpha=" + ALPHA_MATRIX_READ + "})");
        add("displayMessage: Image loaded successfully.");
        add("convertAndFetchImage(controller_tests_image_ppm)");
        add("updateImageDisplay: ");
        add("commandFactoryCall(histogram, [histogram, "
                + "controller_tests_image_ppm, "
                + "controller_tests_image_ppm-histogram])");
        add("convertAndFetchImage(controller_tests_image_ppm-histogram)");
        add("splitView(color-correct, [color-correct, "
                + "controller_tests_image_ppm, "
                + "controller_tests_image_ppm-color-correct], "
                + "controller_tests_image_ppm)");
      }
    };

    assertEquals(expectedLog, log);
  }

  /**
   * Tests the functionality of the blur operation.
   * This test simulates loading an image and applying a blur effect to the
   * image, and verifies the series of log entries produced during this process.
   */
  @Test
  public void blurTest1() {
    /* no inputs in blur operation, simply get blurred image */

    controller = new GUIController(mockModel, mockView);
    controller.loadImage();
    controller.blur();
    List<String> expectedLog = new ArrayList<String>() {
      {
        add("loadImage()");
        add("convertAndStoreImage(" + currentImageName + ", {red="
                + RED_MATRIX_READ + ", green=" + GREEN_MATRIX_READ + ", blue="
                + BLUE_MATRIX_READ + ", alpha=" + ALPHA_MATRIX_READ + "})");
        add("displayMessage: Image loaded successfully.");
        add("convertAndFetchImage(controller_tests_image_ppm)");
        add("updateImageDisplay: ");
        add("commandFactoryCall(histogram, [histogram, "
                + "controller_tests_image_ppm, "
                + "controller_tests_image_ppm-histogram])");
        add("convertAndFetchImage(controller_tests_image_ppm-histogram)");
        add("splitView(blur, [blur, controller_tests_image_ppm, "
                + "controller_tests_image_ppm-blur], controller_tests_image_ppm)");
      }
    };

    assertEquals(expectedLog, log);
  }

  /**
   * Tests the functionality of the sharpen operation.
   * This test simulates loading an image and applying a sharpen effect to the
   * image, and verifies the series of log entries produced during this process.
   */
  @Test
  public void sharpenTest1() {
    /* no inputs in sharpen operation, simply get sharpened image */

    controller = new GUIController(mockModel, mockView);
    controller.loadImage();
    controller.sharpen();
    List<String> expectedLog = new ArrayList<String>() {
      {
        add("loadImage()");
        add("convertAndStoreImage(" + currentImageName + ", {red="
                + RED_MATRIX_READ + ", green=" + GREEN_MATRIX_READ + ", blue="
                + BLUE_MATRIX_READ + ", alpha=" + ALPHA_MATRIX_READ + "})");
        add("displayMessage: Image loaded successfully.");
        add("convertAndFetchImage(controller_tests_image_ppm)");
        add("updateImageDisplay: ");
        add("commandFactoryCall(histogram, [histogram, "
                + "controller_tests_image_ppm, "
                + "controller_tests_image_ppm-histogram])");
        add("convertAndFetchImage(controller_tests_image_ppm-histogram)");
        add("splitView(sharpen, [sharpen, controller_tests_image_ppm, "
                + "controller_tests_image_ppm-sharpen], "
                + "controller_tests_image_ppm)");
      }
    };

    assertEquals(expectedLog, log);
  }

  /**
   * Tests the functionality of the sepia operation.
   * This test simulates loading an image and applying a sepia effect to the
   * image, and verifies the series of log entries produced during this process.
   */
  @Test
  public void sepiaTest1() {
    /* no inputs in sepia operation, simply get sepia image */

    controller = new GUIController(mockModel, mockView);
    controller.loadImage();
    controller.sepia();
    List<String> expectedLog = new ArrayList<String>() {
      {
        add("loadImage()");
        add("convertAndStoreImage(" + currentImageName + ", {red="
                + RED_MATRIX_READ + ", green=" + GREEN_MATRIX_READ + ", blue="
                + BLUE_MATRIX_READ + ", alpha=" + ALPHA_MATRIX_READ + "})");
        add("displayMessage: Image loaded successfully.");
        add("convertAndFetchImage(controller_tests_image_ppm)");
        add("updateImageDisplay: ");
        add("commandFactoryCall(histogram, [histogram, "
                + "controller_tests_image_ppm, "
                + "controller_tests_image_ppm-histogram])");
        add("convertAndFetchImage(controller_tests_image_ppm-histogram)");
        add("splitView(sepia, [sepia, controller_tests_image_ppm, "
                + "controller_tests_image_ppm-sepia], controller_tests_image_ppm)");
      }
    };

    assertEquals(expectedLog, log);
  }

  /**
   * Test case to ensure proper behavior when the black point is not set during
   * the level adjustment operation. This test verifies that the system
   * correctly handles the case where the black point value is not passed into
   * the prompt.
   */
  @Test
  public void levelAdjustTest1() {
    /* level adjust black point not set in the prompt */

    mockView = new MockGuiView(log, selectedFile, "null-null-null");

    controller = new GUIController(mockModel, mockView);
    controller.loadImage();
    controller.levelsAdjust();
    List<String> expectedLog = new ArrayList<String>() {
      {
        add("loadImage()");
        add("convertAndStoreImage(" + currentImageName + ", {red="
                + RED_MATRIX_READ + ", green=" + GREEN_MATRIX_READ + ", blue="
                + BLUE_MATRIX_READ + ", alpha=" + ALPHA_MATRIX_READ + "})");
        add("displayMessage: Image loaded successfully.");
        add("convertAndFetchImage(controller_tests_image_ppm)");
        add("updateImageDisplay: ");
        add("commandFactoryCall(histogram, [histogram, "
                + "controller_tests_image_ppm, "
                + "controller_tests_image_ppm-histogram])");
        add("convertAndFetchImage(controller_tests_image_ppm-histogram)");
        add("offerPromptToGetOperationParameters(Enter black point (0-255):)");
        add("displayError: No value passed!");
      }
    };

    assertEquals(expectedLog, log);
  }

  /**
   * Test case to ensure proper behavior when the black point is not set during
   * the level adjustment operation. This test verifies that the system
   * correctly handles the case where the mid point value is not passed into the
   * prompt.
   */
  @Test
  public void levelAdjustTest2() {
    /* level adjust mid point not set in the prompt */

    mockView = new MockGuiView(log, selectedFile, "black-null-null");
    controller = new GUIController(mockModel, mockView);
    controller.loadImage();
    controller.levelsAdjust();
    List<String> expectedLog = new ArrayList<String>() {
      {
        add("loadImage()");
        add("convertAndStoreImage(" + currentImageName
                + ", {red=" + RED_MATRIX_READ + ", "
                + "green=" + GREEN_MATRIX_READ + ", blue=" + BLUE_MATRIX_READ
                + ", alpha=" + ALPHA_MATRIX_READ + "})");
        add("displayMessage: Image loaded successfully.");
        add("convertAndFetchImage(controller_tests_image_ppm)");
        add("updateImageDisplay: ");
        add("commandFactoryCall(histogram, [histogram, "
                + "controller_tests_image_ppm, "
                + "controller_tests_image_ppm-histogram])");
        add("convertAndFetchImage(controller_tests_image_ppm-histogram)");
        add("offerPromptToGetOperationParameters(Enter black point (0-255):)");
        add("offerPromptToGetOperationParameters(Enter mid point (0-255):)");
        add("displayError: No value passed!");
      }
    };

    assertEquals(expectedLog, log);
  }

  /**
   * Test case to ensure proper behavior when the black point is not set during
   * the level adjustment operation. This test verifies that the system
   * correctly handles the case where the white point value is not passed into
   * the prompt.
   */
  @Test
  public void levelAdjustTest3() {
    /* level adjust white point not set in the prompt */

    mockView = new MockGuiView(log, selectedFile, "black-mid-null");
    controller = new GUIController(mockModel, mockView);
    controller.loadImage();
    controller.levelsAdjust();
    List<String> expectedLog = new ArrayList<String>() {
      {
        add("loadImage()");
        add("convertAndStoreImage(" + currentImageName + ", {red="
                + RED_MATRIX_READ + ", green=" + GREEN_MATRIX_READ + ", blue="
                + BLUE_MATRIX_READ + ", alpha=" + ALPHA_MATRIX_READ + "})");
        add("displayMessage: Image loaded successfully.");
        add("convertAndFetchImage(controller_tests_image_ppm)");
        add("updateImageDisplay: ");
        add("commandFactoryCall(histogram, [histogram, "
                + "controller_tests_image_ppm, "
                + "controller_tests_image_ppm-histogram])");
        add("convertAndFetchImage(controller_tests_image_ppm-histogram)");
        add("offerPromptToGetOperationParameters(Enter black point (0-255):)");
        add("offerPromptToGetOperationParameters(Enter mid point (0-255):)");
        add("offerPromptToGetOperationParameters(Enter white point (0-255):)");
        add("displayError: No value passed!");
      }
    };

    assertEquals(expectedLog, log);
  }

  /**
   * Test case to ensure proper behavior when all required inputs are passed
   * into prompts for level adjustment operation. This test verifies that the
   * system correctly handles the case where all required points are passed in
   * the prompts.
   */
  @Test
  public void levelAdjustTest4() {
    /* level adjust all points set in prompts */

    mockView = new MockGuiView(log, selectedFile, "black-mid-white");
    controller = new GUIController(mockModel, mockView);
    controller.loadImage();
    controller.levelsAdjust();
    List<String> expectedLog = new ArrayList<String>() {
      {
        add("loadImage()");
        add("convertAndStoreImage(" + currentImageName + ", {red="
                + RED_MATRIX_READ + ", green=" + GREEN_MATRIX_READ + ", blue="
                + BLUE_MATRIX_READ + ", alpha=" + ALPHA_MATRIX_READ + "})");
        add("displayMessage: Image loaded successfully.");
        add("convertAndFetchImage(controller_tests_image_ppm)");
        add("updateImageDisplay: ");
        add("commandFactoryCall(histogram, [histogram, "
                + "controller_tests_image_ppm, "
                + "controller_tests_image_ppm-histogram])");
        add("convertAndFetchImage(controller_tests_image_ppm-histogram)");
        add("offerPromptToGetOperationParameters(Enter black point (0-255):)");
        add("offerPromptToGetOperationParameters(Enter mid point (0-255):)");
        add("offerPromptToGetOperationParameters(Enter white point (0-255):)");
        add("splitView(levels-adjust, [levels-adjust, 50, 50, 50, "
                + "controller_tests_image_ppm, "
                + "controller_tests_image_ppm-levels-adjust], "
                + "controller_tests_image_ppm)");
      }
    };

    assertEquals(expectedLog, log);
  }

  /**
   * Test case to ensure proper behavior when the target width is not set during
   * the downscale operation. This test verifies that the system correctly
   * handles the case where the target width value is not passed in the prompt.
   */
  @Test
  public void downscale1() {
    /* target width not set in prompt */

    mockView = new MockGuiView(log, selectedFile, "null-null");
    controller = new GUIController(mockModel, mockView);
    controller.loadImage();
    controller.downscale();
    List<String> expectedLog = new ArrayList<String>() {
      {
        add("loadImage()");
        add("convertAndStoreImage(" + currentImageName + ", {red="
                + RED_MATRIX_READ + ", green=" + GREEN_MATRIX_READ + ", blue="
                + BLUE_MATRIX_READ + ", alpha=" + ALPHA_MATRIX_READ + "})");
        add("displayMessage: Image loaded successfully.");
        add("convertAndFetchImage(controller_tests_image_ppm)");
        add("updateImageDisplay: ");
        add("commandFactoryCall(histogram, [histogram, "
                + "controller_tests_image_ppm, "
                + "controller_tests_image_ppm-histogram])");
        add("convertAndFetchImage(controller_tests_image_ppm-histogram)");
        add("offerPromptToGetOperationParameters(Enter target width: )");
        add("displayError: No value passed!");
      }
    };

    assertEquals(expectedLog, log);
  }

  /**
   * Test case to ensure proper behavior when the target width is not set during
   * the downscale operation. This test verifies that the system correctly
   * handles the case where target height value is not passed in the prompt.
   */
  @Test
  public void downscale2() {
    /* target height not set in prompt */

    mockView = new MockGuiView(log, selectedFile, "width-null");
    controller = new GUIController(mockModel, mockView);
    controller.loadImage();
    controller.downscale();
    List<String> expectedLog = new ArrayList<String>() {
      {
        add("loadImage()");
        add("convertAndStoreImage(" + currentImageName + ", {red="
                + RED_MATRIX_READ + ", green=" + GREEN_MATRIX_READ + ", blue="
                + BLUE_MATRIX_READ + ", alpha=" + ALPHA_MATRIX_READ + "})");
        add("displayMessage: Image loaded successfully.");
        add("convertAndFetchImage(controller_tests_image_ppm)");
        add("updateImageDisplay: ");
        add("commandFactoryCall(histogram, [histogram, "
                + "controller_tests_image_ppm, "
                + "controller_tests_image_ppm-histogram])");
        add("convertAndFetchImage(controller_tests_image_ppm-histogram)");
        add("offerPromptToGetOperationParameters(Enter target width: )");
        add("offerPromptToGetOperationParameters(Enter target height: )");
        add("displayError: No value passed!");
      }
    };

    assertEquals(expectedLog, log);
  }

  /**
   * Test case to ensure proper behavior when the target width and height are
   * passed during the downscale operation. This test verifies that the system
   * correctly handles the case when values passed via prompt input.
   */
  @Test
  public void downscale3() {
    /* target width and height set in prompt */

    mockView = new MockGuiView(log, selectedFile, "width-height");
    controller = new GUIController(mockModel, mockView);
    controller.loadImage();
    controller.downscale();
    String currentImageNameHistogram = currentImageName + "-histogram";
    List<String> expectedLog = new ArrayList<String>() {
      {
        add("loadImage()");
        add("convertAndStoreImage(" + currentImageName + ", {red="
                + RED_MATRIX_READ + ", green=" + GREEN_MATRIX_READ + ", blue="
                + BLUE_MATRIX_READ + ", alpha=" + ALPHA_MATRIX_READ + "})");
        add("displayMessage: Image loaded successfully.");
        add("convertAndFetchImage(controller_tests_image_ppm)");
        add("updateImageDisplay: ");
        add("commandFactoryCall(histogram, [histogram, "
                + "controller_tests_image_ppm, "
                + "controller_tests_image_ppm-histogram])");
        add("convertAndFetchImage(controller_tests_image_ppm-histogram)");
        add("offerPromptToGetOperationParameters(Enter target width: )");
        add("offerPromptToGetOperationParameters(Enter target height: )");
        add("commandFactoryCall(downscale, [downscale, 50, 50, "
                + "controller_tests_image_ppm, "
                + "controller_tests_image_ppm-downscale])");
        add("convertAndFetchImage(controller_tests_image_ppm-downscale)");
        add("updateImageDisplay: ");
        add("commandFactoryCall(histogram, [histogram, "
                + "controller_tests_image_ppm-downscale, "
                + "controller_tests_image_ppm-downscale-histogram])");
        add("convertAndFetchImage(controller_tests_image_ppm-downscale-"
                + "histogram)");
        add("offerToSaveImage(controller_tests_image_ppm-downscale, ppm)");
        add("convertAndFetchImage(controller_tests_image_ppm-downscale)");
        add("updateImageDisplay: ");
        add("commandFactoryCall(histogram, [histogram, "
                + "controller_tests_image_ppm-downscale, "
                + "controller_tests_image_ppm-downscale-histogram])");
        add("convertAndFetchImage(controller_tests_image_ppm-downscale-"
                + "histogram)");
      }
    };

    assertEquals(expectedLog, log);
  }

  /**
   * Test to check correct flow of controller, view, and model when we do
   * multiple operations on an image in sequence.
   */
  @Test
  public void chainingOperations() {
    /* performing multiple image operations */

    mockView = new MockGuiView(log, selectedFile, "width-height");
    controller = new GUIController(mockModel, mockView);
    controller.loadImage();
    controller.verticalFlip();
    controller.horizontalFlip();
    controller.colorCorrection();

    List<String> expectedLog = new ArrayList<String>() {
      {
        add("loadImage()");
        add("convertAndStoreImage(" + currentImageName + ", {red="
                + RED_MATRIX_READ + ", green=" + GREEN_MATRIX_READ + ", blue="
                + BLUE_MATRIX_READ + ", alpha=" + ALPHA_MATRIX_READ + "})");
        add("displayMessage: Image loaded successfully.");
        add("convertAndFetchImage(controller_tests_image_ppm)");
        add("updateImageDisplay: ");
        add("commandFactoryCall(histogram, [histogram, "
                + "controller_tests_image_ppm, "
                + "controller_tests_image_ppm-histogram])");
        add("convertAndFetchImage(controller_tests_image_ppm-histogram)");
        add("commandFactoryCall(vertical-flip, [vertical-flip, "
                + "controller_tests_image_ppm, "
                + "controller_tests_image_ppm-vertical-flip])");
        add("convertAndFetchImage(controller_tests_image_ppm-vertical-flip)");
        add("updateImageDisplay: ");
        add("commandFactoryCall(histogram, [histogram, "
                + "controller_tests_image_ppm-vertical-flip, "
                + "controller_tests_image_ppm-vertical-flip-histogram])");
        add("convertAndFetchImage(controller_tests_image_ppm-vertical-flip-"
                + "histogram)");
        add("offerToSaveImage(controller_tests_image_ppm-vertical-flip, ppm)");
        add("convertAndFetchImage(controller_tests_image_ppm-vertical-flip)");
        add("updateImageDisplay: ");
        add("commandFactoryCall(histogram, [histogram, "
                + "controller_tests_image_ppm-vertical-flip, "
                + "controller_tests_image_ppm-vertical-flip-histogram])");
        add("convertAndFetchImage(controller_tests_image_ppm-vertical-flip-"
                + "histogram)");
        add("commandFactoryCall(horizontal-flip, [horizontal-flip, "
                + "controller_tests_image_ppm-vertical-flip, "
                + "controller_tests_image_ppm-vertical-flip-horizontal-flip])");
        add("convertAndFetchImage(controller_tests_image_ppm-vertical-flip-"
                + "horizontal-flip)");
        add("updateImageDisplay: ");
        add("commandFactoryCall(histogram, [histogram, "
                + "controller_tests_image_ppm-vertical-flip-horizontal-flip, "
                + "controller_tests_image_ppm-vertical-flip-horizontal-flip-"
                + "histogram])");
        add("convertAndFetchImage("
                + "controller_tests_image_ppm-vertical-flip-horizontal-flip-"
                + "histogram)");
        add("offerToSaveImage(controller_tests_image_ppm-vertical-flip-"
                + "horizontal-flip, ppm)");
        add("convertAndFetchImage(controller_tests_image_ppm-vertical-flip-"
                + "horizontal-flip)");
        add("updateImageDisplay: ");
        add("commandFactoryCall(histogram, [histogram, "
                + "controller_tests_image_ppm-vertical-flip-horizontal-flip, "
                + "controller_tests_image_ppm-vertical-flip-horizontal-flip-"
                + "histogram])");
        add("convertAndFetchImage("
                + "controller_tests_image_ppm-vertical-flip-horizontal-flip-"
                + "histogram)");
        add("splitView(color-correct, [color-correct, "
                + "controller_tests_image_ppm-vertical-flip-horizontal-flip, "
                + "controller_tests_image_ppm-vertical-flip-horizontal-flip-color-"
                + "correct], "
                + "controller_tests_image_ppm-vertical-flip-horizontal-flip)]");
      }
    };
    assertEquals(expectedLog, log);
  }

  /**
   * Test case to verify the correct behavior when switching between previous
   * and next versions of an image when both previous and next versions exist.
   * This test checks if the system can correctly move between versions
   * (previous and next) when the image has been modified (e.g., by applying a
   * vertical flip) and versions are available for navigation.
   */
  @Test
  public void nextVersion1() {
    /* changing to previous and next version correctly when there is a previous
     * and next version */

    mockView = new MockGuiView(log, selectedFile, "width-height");
    controller = new GUIController(mockModel, mockView);
    controller.loadImage();
    controller.verticalFlip();
    controller.previousVersion();
    controller.nextVersion();

    List<String> expectedLog = new ArrayList<String>() {
      {
        add("loadImage()");
        add("convertAndStoreImage(" + currentImageName + ", {red="
                + RED_MATRIX_READ + ", green=" + GREEN_MATRIX_READ + ", blue="
                + BLUE_MATRIX_READ + ", alpha=" + ALPHA_MATRIX_READ + "})");
        add("displayMessage: Image loaded successfully.");
        add("convertAndFetchImage(controller_tests_image_ppm)");
        add("updateImageDisplay: ");
        add("commandFactoryCall(histogram, [histogram, "
                + "controller_tests_image_ppm, "
                + "controller_tests_image_ppm-histogram])");
        add("convertAndFetchImage(controller_tests_image_ppm-histogram)");
        add("commandFactoryCall(vertical-flip, [vertical-flip, "
                + "controller_tests_image_ppm, "
                + "controller_tests_image_ppm-vertical-flip])");
        add("convertAndFetchImage(controller_tests_image_ppm-vertical-flip)");
        add("updateImageDisplay: ");
        add("commandFactoryCall(histogram, [histogram, "
                + "controller_tests_image_ppm-vertical-flip, "
                + "controller_tests_image_ppm-vertical-flip-histogram])");
        add("convertAndFetchImage(controller_tests_image_ppm-vertical-flip-"
                + "histogram)");
        add("offerToSaveImage(controller_tests_image_ppm-vertical-flip, ppm)");
        add("convertAndFetchImage(controller_tests_image_ppm-vertical-flip)");
        add("updateImageDisplay: ");
        add("commandFactoryCall(histogram, [histogram, "
                + "controller_tests_image_ppm-vertical-flip, "
                + "controller_tests_image_ppm-vertical-flip-histogram])");
        add("convertAndFetchImage(controller_tests_image_ppm-vertical-flip-"
                + "histogram)");
        add("convertAndFetchImage(controller_tests_image_ppm)");
        add("updateImageDisplay: ");
        add("commandFactoryCall(histogram, [histogram, "
                + "controller_tests_image_ppm, "
                + "controller_tests_image_ppm-histogram])");
        add("convertAndFetchImage(controller_tests_image_ppm-histogram)");
        add("convertAndFetchImage(controller_tests_image_ppm-vertical-flip)");
        add("updateImageDisplay: ");
        add("commandFactoryCall(histogram, [histogram, "
                + "controller_tests_image_ppm-vertical-flip, "
                + "controller_tests_image_ppm-vertical-flip-histogram])");
        add("convertAndFetchImage(controller_tests_image_ppm-vertical-flip-"
                + "histogram)");
      }
    };

    assertEquals(expectedLog, log);
  }

  /**
   * Test case to verify the behavior when trying to navigate to the next
   * version when there is no next version. This test checks the system's
   * response is as expected where no operation occurs with previous and next
   * calls when the user attempts to move to the next and previous version,
   * because no version exists beyond the current state of the image.
   */
  @Test
  public void nextVersion2() {
    /* changing to next version when there is no next version */

    controller = new GUIController(mockModel, mockView);
    controller.loadImage();
    controller.nextVersion();
    controller.previousVersion();

    List<String> expectedLog = new ArrayList<String>() {
      {
        add("loadImage()");
        add("convertAndStoreImage(" + currentImageName + ", {red="
                + RED_MATRIX_READ + ", green=" + GREEN_MATRIX_READ + ", blue="
                + BLUE_MATRIX_READ + ", alpha=" + ALPHA_MATRIX_READ + "})");
        add("displayMessage: Image loaded successfully.");
        add("convertAndFetchImage(controller_tests_image_ppm)");
        add("updateImageDisplay: ");
        add("commandFactoryCall(histogram, [histogram, "
                + "controller_tests_image_ppm, "
                + "controller_tests_image_ppm-histogram])");
        add("convertAndFetchImage(controller_tests_image_ppm-histogram)");
      }
    };
    assertEquals(expectedLog, log);
  }

  /**
   * Test case to verify the behavior when trying to navigate to the previous
   * version when there is no previous version. This test checks the system's
   * response is as expected where no operation occurs with previous when the
   * user attempts to move to the previous version, but no previous version
   * exists for the image.
   */
  @Test
  public void nextVersion3() {
    /* changing to previous version when there is no previous version */

    controller = new GUIController(mockModel, mockView);
    controller.loadImage();
    controller.nextVersion();
    controller.previousVersion();

    List<String> expectedLog = new ArrayList<String>() {
      {
        add("loadImage()");
        add("convertAndStoreImage(" + currentImageName + ", {red="
                + RED_MATRIX_READ + ", green=" + GREEN_MATRIX_READ + ", blue="
                + BLUE_MATRIX_READ + ", alpha=" + ALPHA_MATRIX_READ + "})");
        add("displayMessage: Image loaded successfully.");
        add("convertAndFetchImage(controller_tests_image_ppm)");
        add("updateImageDisplay: ");
        add("commandFactoryCall(histogram, [histogram, "
                + "controller_tests_image_ppm, "
                + "controller_tests_image_ppm-histogram])");
        add("convertAndFetchImage(controller_tests_image_ppm-histogram)");
      }
    };
    assertEquals(expectedLog, log);
  }

  /**
   * Test case to verify that attempting to save an image, when the image is
   * loaded and present, is successful. This test simulates a scenario where an
   * image is loaded into the system, and the user attempts to save it. The
   * system should show the appropriate success messages at view, indicating
   * that the image was saved successfully.
   */
  @Test
  public void save1() throws IOException {
    /* check attempting to save image is loaded/present, is successful */

    controller = new GUIController(mockModel, mockView);
    controller.loadImage();
    controller.save();
    List<String> expectedLog = new ArrayList<String>() {
      {
        add("loadImage()");
        add("convertAndStoreImage(" + currentImageName + ", {red="
                + RED_MATRIX_READ + ", green=" + GREEN_MATRIX_READ + ", blue="
                + BLUE_MATRIX_READ + ", alpha=" + ALPHA_MATRIX_READ + "})");
        add("displayMessage: Image loaded successfully.");
        add("convertAndFetchImage(controller_tests_image_ppm)");
        add("updateImageDisplay: ");
        add("commandFactoryCall(histogram, [histogram, "
                + "controller_tests_image_ppm, "
                + "controller_tests_image_ppm-histogram])");
        add("convertAndFetchImage(controller_tests_image_ppm-histogram)");
        add("saveWithFileChooser(controller_tests_image_ppm, ppm)");
        add("displayMessage: Image saved successfully to: ");
      }
    };

    boolean allExpectedMessagesLogged = expectedLog.stream().allMatch(
        expectedMessage
          -> expectedLog.stream().anyMatch(log -> log.contains(expectedMessage)));

    assertTrue(allExpectedMessagesLogged);
  }

  /**
   * Test case to verify the behavior when attempting to save an image that is
   * loaded, but an error occurs during the saving process. This test simulates
   * a scenario where the image is loaded successfully, but an error is
   * encountered while saving the image (e.g., due to file access issues). The
   * system should display appropriate error message at view.
   */
  @Test
  public void save2() throws IOException {
    /* check attempting to save image is loaded/present but saving exception
     * occurs */

    mockView = new MockGuiView(log, selectedFile, "failSaving");
    controller = new GUIController(mockModel, mockView);
    controller.loadImage();
    controller.save();
    List<String> expectedLog = new ArrayList<String>() {
      {
        add("loadImage()");
        add("convertAndStoreImage(" + currentImageName + ", {red="
                + RED_MATRIX_READ + ", green=" + GREEN_MATRIX_READ + ", blue="
                + BLUE_MATRIX_READ + ", alpha=" + ALPHA_MATRIX_READ + "})");
        add("displayMessage: Image loaded successfully.");
        add("convertAndFetchImage(controller_tests_image_ppm)");
        add("updateImageDisplay: ");
        add("commandFactoryCall(histogram, [histogram, "
                + "controller_tests_image_ppm, "
                + "controller_tests_image_ppm-histogram])");
        add("convertAndFetchImage(controller_tests_image_ppm-histogram)");
        add("saveWithFileChooser(controller_tests_image_ppm, ppm)");
        add("displayMessage: Error saving image: unable to save file");
      }
    };

    assertEquals(expectedLog, log);
  }
}
