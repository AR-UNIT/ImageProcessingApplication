package ime.model;

import static ime.model.SplitTransformExpectedOutputs.getExpectedPixelsWithSplitLine;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import ime.CommandConstants;
import ime.controller.ImageReader;
import ime.controller.ImageReaderFactory;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for model class inside model package inside ime package.
 */
public class ModelTest {
  private ModelInterface model;
  ImageInterface originalImage;

  /**
   * Sets up the test environment by initializing the Model instance.
   */
  @Before
  public void setUp() {
    model = new Model();
    PixelInterface[][] originalPixels = {
            {new Pixel(100, 100, 100, 255),
            new Pixel(150, 150, 150, 255)},
            {new Pixel(200, 200, 200, 255),
            new Pixel(50, 50, 50, 255)}};
    originalImage = new Image(originalPixels.length, originalPixels[0].length);
    originalImage.imageFill(originalPixels);

    model.storeImage("original", originalImage);
  }

  /**
   * Tests storing and retrieving an image by name/key in the model.
   * Verifies that the stored image is the same as the retrieved one.
   */
  @Test
  public void testImageStorageAndRetrieval() {
    PixelInterface[][] pixels = {
            {new Pixel(100, 100, 100, 255),
            new Pixel(150, 150, 150, 255)},
            {new Pixel(200, 200, 200, 255),
            new Pixel(50, 50, 50, 255)}};
    ImageInterface testImage = new Image(pixels.length, pixels[0].length);
    testImage.imageFill(pixels);

    model.storeImage("testImage", testImage);

    ImageInterface retrievedImage = model.getImage("testImage");
    assertEquals(testImage, retrievedImage);
  }

  /**
   * Asserts that the pixel values of the expected and actual images are equal.
   * This checks the RGBA values of each pixel in the image and reports
   * mismatches with a descriptive message.
   *
   * @param expectedPixels the expected pixel data for comparison
   * @param actualImage    the actual image to compare against the expected
   *     image
   * @param component      the name of the image component being tested (e.g.,
   *     "Sepia",
   *                       "Brightness")
   */
  private void assertImagesEqual(PixelInterface[][] expectedPixels,
                                 ImageInterface actualImage, String component) {
    for (int row = 0; row < expectedPixels.length; row++) {
      for (int col = 0; col < expectedPixels[0].length; col++) {
        PixelInterface expectedPixel = expectedPixels[row][col];
        PixelInterface actualPixel = actualImage.getPixel(row, col);

        assertEquals(
                component + " Red value mismatch at (" + row + ", " + col + ")",
                expectedPixel.getR(), actualPixel.getR());
        assertEquals(
                component + " Green value mismatch at (" + row + ", " + col + ")",
                expectedPixel.getG(), actualPixel.getG());
        assertEquals(
                component + " Blue value mismatch at (" + row + ", " + col + ")",
                expectedPixel.getB(), actualPixel.getB());
        assertEquals(
                component + " Alpha value mismatch at (" + row + ", " + col + ")",
                expectedPixel.getA(), actualPixel.getA());
      }
    }
  }

  /**
   * Tests the sepia tone transformation on an image.
   * It creates an image, applies the sepia transformation, and asserts that the
   * output matches the expected sepia-tone image.
   */
  @Test
  public void testSepiaPixelTransformation() {
    PixelInterface[][] originalPixels = {
            {new Pixel(100, 100, 100, 255),
            new Pixel(150, 150, 150, 255),
            new Pixel(200, 200, 200, 255)},
            {new Pixel(50, 50, 50, 255),
            new Pixel(100, 100, 100, 255),
            new Pixel(150, 150, 150, 255)},
            {new Pixel(0, 0, 0, 255),
            new Pixel(50, 50, 50, 255),
            new Pixel(100, 100, 100, 255)}};

    PixelInterface[][] expectedSepiaPixels = {
            {new Pixel(135, 120, 93, 255),
            new Pixel(202, 180, 140, 255),
            new Pixel(255, 240, 187, 255)},
            {new Pixel(67, 60, 46, 255),
            new Pixel(135, 120, 93, 255),
            new Pixel(202, 180, 140, 255)},
            {new Pixel(0, 0, 0, 255),
            new Pixel(67, 60, 46, 255),
            new Pixel(135, 120, 93, 255)}};

    ImageInterface originalImage =
            new Image(originalPixels.length, originalPixels[0].length);
    originalImage.imageFill(originalPixels);

    ImageInterface expectedImage =
            new Image(expectedSepiaPixels.length, expectedSepiaPixels[0].length);
    expectedImage.imageFill(expectedSepiaPixels);

    ModelInterface model = new Model();
    model.storeImage("original", originalImage);

    /* sepia image-name dest-image-name */
    List<String> commandTokens = List.of("sepia", "original", "sepiaResult");
    model.operationsFactoryCall("sepia", commandTokens, model);

    ImageInterface transformedImage = model.getImage("sepiaResult");

    assertEquals(expectedImage, transformedImage);
  }

  /**
   * Tests the brighten image transformation on an image.
   * It brightens the image by a given increment and asserts that the resulting
   * image matches the expected brightened image.
   */
  @Test
  public void testBrightenImageTransformation() {
    PixelInterface[][] originalPixels = {
            {new Pixel(100, 150, 200, 255),
            new Pixel(50, 100, 150, 255)},
            {new Pixel(0, 50, 100, 255),
            new Pixel(200, 220, 240, 255)}};

    String brightenIncrement = "50";

    PixelInterface[][] expectedBrightenedPixels = {
            {new Pixel(150, 200, 250, 255),
            new Pixel(100, 150, 200, 255)},
            {new Pixel(50, 100, 150, 255),
            new Pixel(250, 255, 255, 255)}};

    ImageInterface originalImage =
            new Image(originalPixels.length, originalPixels[0].length);
    originalImage.imageFill(originalPixels);

    ImageInterface expectedOutputImage = new Image(
            expectedBrightenedPixels.length, expectedBrightenedPixels[0].length);
    expectedOutputImage.imageFill(expectedBrightenedPixels);

    ModelInterface model = new Model();
    model.storeImage("original", originalImage);

    /* brighten brighten-increment image-name dest-image-name */
    List<String> commandTokens =
            List.of("brighten", brightenIncrement, "original", "brightenedResult");
    model.operationsFactoryCall("brighten", commandTokens, model);

    ImageInterface transformedImage = model.getImage("brightenedResult");

    assertEquals(expectedOutputImage, transformedImage);
  }

  /**
   * Tests the luma component transformation on an image.
   * It applies the luma component formula to calculate grayscale values and
   * asserts that the resulting image matches the expected grayscale values.
   */
  @Test
  public void testLumaComponentTransformation() {
    PixelInterface[][] originalPixels = {
            {new Pixel(100, 150, 200, 255),
            new Pixel(50, 100, 150, 255)},
            {new Pixel(0, 50, 100, 255),
            new Pixel(200, 220, 240, 255)}};
    PixelInterface[][] expectedLumaPixels = {
            {new Pixel(143, 143, 143, 255),
            new Pixel(93, 93, 93, 255)},
            {new Pixel(43, 43, 43, 255),
            new Pixel(217, 217, 217, 255)}};

    Image originalImage =
            new Image(originalPixels.length, originalPixels[0].length);
    originalImage.imageFill(originalPixels);
    ImageInterface expectedLumaImage =
            new Image(expectedLumaPixels.length, expectedLumaPixels[0].length);
    expectedLumaImage.imageFill(expectedLumaPixels);
    ModelInterface model = new Model();

    model.storeImage("original", originalImage);

    /* luma-component manas manas-luma */
    List<String> commandTokens =
            List.of("luma-component", "original", "lumaResult");
    model.operationsFactoryCall("luma-component", commandTokens, model);

    ImageInterface transformedImage = model.getImage("lumaResult");
    assertEquals(expectedLumaImage, transformedImage);
  }

  /**
   * Tests the pixel intensity transformation on an image. This operation
   * calculates the intensity for each pixel, which is typically done by
   * averaging the RGB components or applying a specific formula. The
   * transformed image is then compared to the expected result.
   */
  @Test
  public void testPixelIntensityTransformation() {
    PixelInterface[][] originalPixels = {
            {new Pixel(255, 0, 0, 255),
            new Pixel(0, 255, 0, 255)},
            {new Pixel(0, 0, 255, 255),
            new Pixel(255, 255, 255, 255)}};

    PixelInterface[][] expectedPixels = {
            {new Pixel(85, 85, 85, 255),
            new Pixel(85, 85, 85, 255)},
            {new Pixel(85, 85, 85, 255),
            new Pixel(255, 255, 255, 255)}};

    Image originalImage =
            new Image(originalPixels.length, originalPixels[0].length);
    originalImage.imageFill(originalPixels);

    Image expectedImage =
            new Image(expectedPixels.length, expectedPixels[0].length);
    expectedImage.imageFill(expectedPixels);

    ModelInterface model = new Model();

    model.storeImage("original", originalImage);

    /* intensity-component manas manas-intensity */
    List<String> commandTokens =
            List.of("intensity-component", "original", "intensityResult");
    model.operationsFactoryCall("intensity-component", commandTokens, model);

    ImageInterface transformedImage = model.getImage("intensityResult");

    assertEquals(expectedImage, transformedImage);
  }

  /**
   * Tests the RGB split transformation on an image. This operation splits the
   * image into three separate images based on the red, green, and blue
   * components. Each component will be isolated and stored as a grayscale
   * image. The test checks whether each resulting image matches the expected
   * split values.
   */
  @Test
  public void testRgbSplit() {
    PixelInterface[][] originalPixels = {
            {new Pixel(100, 150, 200, 255),
            new Pixel(50, 100, 150, 255)},
            {new Pixel(0, 50, 100, 255),
            new Pixel(200, 220, 240, 255)}};

    PixelInterface[][] expectedRedSplit = {
            {new Pixel(100, 100, 100, 255),
            new Pixel(50, 50, 50, 255)},
            {new Pixel(0, 0, 0, 255),
            new Pixel(200, 200, 200, 255)}};

    PixelInterface[][] expectedGreenSplit = {
            {new Pixel(150, 150, 150, 255),
            new Pixel(100, 100, 100, 255)},
            {new Pixel(50, 50, 50, 255),
            new Pixel(220, 220, 220, 255)}};

    PixelInterface[][] expectedBlueSplit = {
            {new Pixel(200, 200, 200, 255),
            new Pixel(150, 150, 150, 255)},
            {new Pixel(100, 100, 100, 255),
            new Pixel(240, 240, 240, 255)}};

    ImageInterface originalImage =
            new Image(originalPixels.length, originalPixels[0].length);
    originalImage.imageFill(originalPixels);
    model.storeImage("original", originalImage);

    ImageInterface expectedRedSplitImage =
            new Image(originalPixels.length, originalPixels[0].length);
    expectedRedSplitImage.imageFill(expectedRedSplit);

    ImageInterface expectedGreenSplitImage =
            new Image(originalPixels.length, originalPixels[0].length);
    expectedGreenSplitImage.imageFill(expectedGreenSplit);

    ImageInterface expectedBlueSplitImage =
            new Image(originalPixels.length, originalPixels[0].length);
    expectedBlueSplitImage.imageFill(expectedBlueSplit);

    /* intensity-component manas manas-intensity */
    List<String> commandTokens = List.of(
            "rgb-split", "original", "redResult", "greenResult", "blueResult");
    model.operationsFactoryCall("rgb-split", commandTokens, model);

    ImageInterface redSplitImage = model.getImage("redResult");
    ImageInterface greenSplitImage = model.getImage("greenResult");
    ImageInterface blueSplitImage = model.getImage("blueResult");

    assertEquals(expectedRedSplitImage, redSplitImage);
    assertEquals(expectedGreenSplitImage, greenSplitImage);
    assertEquals(expectedBlueSplitImage, blueSplitImage);
  }

  /**
   * Tests the extraction of the red component from an image. This operation
   * isolates the red component of each pixel and sets the green and blue
   * components to zero, effectively creating a grayscale image based on the red
   * values. The test checks whether the extracted red component matches the
   * expected result.
   */
  @Test
  public void testGetRedComponent() {
    PixelInterface[][] originalPixels = {
            {new Pixel(100, 150, 200, 255),
            new Pixel(50, 100, 150, 255)},
            {new Pixel(0, 50, 100, 255),
            new Pixel(200, 220, 240, 255)}};

    PixelInterface[][] expectedRedComponent = {
            {new Pixel(100, 100, 100, 255),
            new Pixel(50, 50, 50, 255)},
            {new Pixel(0, 0, 0, 255),
            new Pixel(200, 200, 200, 255)}};

    Image originalImage =
            new Image(originalPixels.length, originalPixels[0].length);
    originalImage.imageFill(originalPixels);
    model.storeImage("original", originalImage);

    ImageInterface expectedRedComponentImage =
            new Image(originalPixels.length, originalPixels[0].length);
    expectedRedComponentImage.imageFill(expectedRedComponent);

    /* red-component manas manas-red */
    List<String> commandTokens =
            List.of("red-component", "original", "redComponentResult");
    model.operationsFactoryCall("red-component", commandTokens, model);
    ImageInterface transformedImage = model.getImage("redComponentResult");
    assertEquals(expectedRedComponentImage, transformedImage);
  }

  /**
   * Tests the extraction of the green component from an image. The operation
   * isolates the green component of each pixel, setting the red and blue
   * components to zero, resulting in a grayscale image with intensity based on
   * the green values. The test compares the transformed image with the expected
   * result.
   */
  @Test
  public void testGetGreenComponent() {
    PixelInterface[][] originalPixels = {
            {new Pixel(100, 150, 200, 255),
            new Pixel(50, 100, 150, 255)},
            {new Pixel(0, 50, 100, 255),
            new Pixel(200, 220, 240, 255)}};

    PixelInterface[][] expectedGreenComponent = {
            {new Pixel(150, 150, 150, 255),
            new Pixel(100, 100, 100, 255)},
            {new Pixel(50, 50, 50, 255),
            new Pixel(220, 220, 220, 255)}};

    Image originalImage =
            new Image(originalPixels.length, originalPixels[0].length);
    originalImage.imageFill(originalPixels);
    model.storeImage("original", originalImage);

    ImageInterface expectedGreenComponentImage =
            new Image(originalPixels.length, originalPixels[0].length);
    expectedGreenComponentImage.imageFill(expectedGreenComponent);

    /* green-component manas manas-green */
    List<String> commandTokens =
            List.of("green-component", "original", "greenComponentResult");
    model.operationsFactoryCall("green-component", commandTokens, model);
    ImageInterface transformedImage = model.getImage("greenComponentResult");

    assertEquals(expectedGreenComponentImage, transformedImage);
  }

  /**
   * Tests the extraction of the blue component from an image. This operation
   * isolates the blue component of each pixel, setting the red and green
   * components to zero, effectively producing a grayscale image based on the
   * blue values. The test compares the transformed image with the expected blue
   * component result.
   */
  @Test
  public void testBlueComponent() {
    PixelInterface[][] originalPixels = {
            {new Pixel(100, 150, 200, 255),
            new Pixel(50, 100, 150, 255)},
            {new Pixel(0, 50, 100, 255),
            new Pixel(200, 220, 240, 255)}};

    PixelInterface[][] expectedBlueComponent = {
            {new Pixel(200, 200, 200, 255),
            new Pixel(150, 150, 150, 255)},
            {new Pixel(100, 100, 100, 255),
            new Pixel(240, 240, 240, 255)}};

    ImageInterface originalImage =
            new Image(originalPixels.length, originalPixels[0].length);
    originalImage.imageFill(originalPixels);
    model.storeImage("original", originalImage);

    ImageInterface expectedBlueComponentImage = new Image(
            expectedBlueComponent.length, expectedBlueComponent[0].length);
    expectedBlueComponentImage.imageFill(expectedBlueComponent);

    /* green-component manas manas-green */
    List<String> commandTokens =
            List.of("blue-component", "original", "blueComponentResult");
    model.operationsFactoryCall("blue-component", commandTokens, model);

    ImageInterface transformedImage = model.getImage("blueComponentResult");

    assertEquals(expectedBlueComponentImage, transformedImage);
  }

  /**
   * Tests vertical flipping of an image. This operation flips the image
   * vertically, i.e., it reverses the order of rows in the image. The test
   * compares the transformed image with the expected result.
   */
  @Test
  public void testVerticalFlip() {
    PixelInterface[][] originalPixels = {
            {new Pixel(100, 150, 200, 255),
            new Pixel(50, 100, 150, 255)},
            {new Pixel(0, 50, 100, 255),
            new Pixel(200, 220, 240, 255)}};

    PixelInterface[][] expectedFlippedPixels = {
            {new Pixel(0, 50, 100, 255),
            new Pixel(200, 220, 240, 255)},
            {new Pixel(100, 150, 200, 255),
            new Pixel(50, 100, 150, 255)}};

    ImageInterface originalImage =
            new Image(originalPixels.length, originalPixels[0].length);
    originalImage.imageFill(originalPixels);
    model.storeImage("original", originalImage);

    ImageInterface expectedFlippedPixelsImage =
            new Image(originalPixels.length, originalPixels[0].length);
    expectedFlippedPixelsImage.imageFill(expectedFlippedPixels);

    /* green-component manas manas-green */
    List<String> commandTokens =
            List.of("vertical-flip", "original", "verticalFlippedResult");
    model.operationsFactoryCall("vertical-flip", commandTokens, model);

    ImageInterface transformedImage = model.getImage("verticalFlippedResult");

    assertEquals(expectedFlippedPixelsImage, transformedImage);
  }

  /**
   * Tests horizontal flipping of an image. This operation flips the image
   * horizontally, i.e., it reverses the order of columns.
   * The test compares the transformed image with the expected result.
   */
  @Test
  public void testHorizontalFlip() {
    PixelInterface[][] originalPixels = {
            {new Pixel(100, 150, 200, 255),
            new Pixel(50, 100, 150, 255)},
            {new Pixel(0, 50, 100, 255),
            new Pixel(200, 220, 240, 255)}};

    PixelInterface[][] expectedFlippedPixels = {
            {new Pixel(50, 100, 150, 255),
            new Pixel(100, 150, 200, 255)},
            {new Pixel(200, 220, 240, 255),
            new Pixel(0, 50, 100, 255)}};

    ImageInterface expectedFlippedImage =
            new Image(originalPixels.length, originalPixels[0].length);
    expectedFlippedImage.imageFill(expectedFlippedPixels);
    Image originalImage =
            new Image(originalPixels.length, originalPixels[0].length);
    originalImage.imageFill(originalPixels);
    model.storeImage("original", originalImage);

    List<String> commandTokens =
            List.of("horizontal-flip", "original", "horizontalFlippedResult");
    model.operationsFactoryCall("horizontal-flip", commandTokens, model);

    ImageInterface transformedImage = model.getImage("horizontalFlippedResult");
    assertEquals(expectedFlippedImage, transformedImage);
  }

  /**
   * Tests the extraction of the value component from an image. This operation
   * extracts the maximum value among the red, green, and blue components for
   * each pixel, producing a grayscale image based on the highest RGB value. The
   * test compares the transformed image with the expected value component
   * result.
   */
  @Test
  public void testGetValueComponent() {
    PixelInterface[][] originalPixels = {
            {new Pixel(100, 150, 200, 255),
            new Pixel(50, 100, 150, 255)},
            {new Pixel(0, 50, 100, 255),
            new Pixel(200, 220, 240, 255)}};

    PixelInterface[][] expectedValueComponent = {
            {new Pixel(200, 200, 200, 255),
            new Pixel(150, 150, 150, 255)},
            {new Pixel(100, 100, 100, 255),
            new Pixel(240, 240, 240, 255)}};

    ImageInterface originalImage =
            new Image(originalPixels.length, originalPixels[0].length);
    originalImage.imageFill(originalPixels);
    model.storeImage("original", originalImage);

    ImageInterface expectedImage =
            new Image(originalPixels.length, originalPixels[0].length);
    expectedImage.imageFill(expectedValueComponent);

    List<String> commandTokens =
            List.of("value-component", "original", "valueComponentResult");
    model.operationsFactoryCall("value-component", commandTokens, model);

    ImageInterface transformedImage = model.getImage("valueComponentResult");
    assertEquals(expectedImage, transformedImage);
  }

  /**
   * Tests combining red, green, and blue channel images into a single RGB
   * image. Creates three separate images for each color channel, combines them
   * using the "rgb-combine" operation, and verifies the result against the
   * expected combined image object.
   */
  @Test
  public void testRgbCombine() {
    PixelInterface[][] redPixels = {
            {new Pixel(100, 0, 0, 255),
            new Pixel(50, 0, 0, 255)},
            {new Pixel(0, 0, 0, 255),
            new Pixel(200, 0, 0, 255)}};

    PixelInterface[][] greenPixels = {
            {new Pixel(0, 150, 0, 255),
            new Pixel(0, 100, 0, 255)},
            {new Pixel(0, 50, 0, 255),
            new Pixel(0, 220, 0, 255)}};

    PixelInterface[][] bluePixels = {
            {new Pixel(0, 0, 200, 255),
            new Pixel(0, 0, 150, 255)},
            {new Pixel(0, 0, 100, 255),
            new Pixel(0, 0, 240, 255)}};

    PixelInterface[][] expectedCombinedPixels = {
            {new Pixel(100, 150, 200, 255),
            new Pixel(50, 100, 150, 255)},
            {new Pixel(0, 50, 100, 255),
            new Pixel(200, 220, 240, 255)}};

    Image redImage = new Image(redPixels.length, redPixels[0].length);
    redImage.imageFill(redPixels);
    model.storeImage("red", redImage);

    Image greenImage = new Image(greenPixels.length, greenPixels[0].length);
    greenImage.imageFill(greenPixels);
    model.storeImage("green", greenImage);

    Image blueImage = new Image(bluePixels.length, bluePixels[0].length);
    blueImage.imageFill(bluePixels);
    model.storeImage("blue", blueImage);

    List<String> commandTokens =
            List.of("rgb-combine", "combinedResult", "red", "green", "blue");
    model.operationsFactoryCall("rgb-combine", commandTokens, model);

    ImageInterface combinedImage = model.getImage("combinedResult");

    assertImagesEqual(expectedCombinedPixels, combinedImage, "combinedResult");
  }

  /**
   * Tests the blur operation on an image.
   * Blurs the "original" image and checks if result matches the expected
   * blurred image object.
   */
  @Test
  public void testBlurImage() {
    PixelInterface[][] originalPixels = {
            {new Pixel(100, 100, 100, 255),
            new Pixel(150, 150, 150, 255)},
            {new Pixel(200, 200, 200, 255),
            new Pixel(50, 50, 50, 255)}};

    PixelInterface[][] expectedBlurredPixels = {
            {new Pixel(125, 125, 125, 255),
            new Pixel(125, 125, 125, 255)},
            {new Pixel(150, 150, 150, 255),
            new Pixel(100, 100, 100, 255)}};

    Image originalImage =
            new Image(originalPixels.length, originalPixels[0].length);
    originalImage.imageFill(originalPixels);
    model.storeImage("original", originalImage);

    Image expectedBlurredImage =
            new Image(originalPixels.length, originalPixels[0].length);
    expectedBlurredImage.imageFill(expectedBlurredPixels);
    List<String> commandTokens = List.of("blur", "original", "blurredResult");
    model.operationsFactoryCall("blur", commandTokens, model);

    ImageInterface blurredImage = model.getImage("blurredResult");

    assertEquals(expectedBlurredImage, blurredImage);
  }

  /**
   * Tests the sharpen operation on an image. The sharpening operation enhances
   * the contrast of the image,
   * making the edges more defined by increasing the intensity of pixels that
   * differ from their neighbors.
   * The test compares the transformed image with the expected sharpened result.
   */
  @Test
  public void testSharpenImage() {
    PixelInterface[][] originalPixels = {
            {new Pixel(100, 100, 100, 255),
            new Pixel(150, 150, 150, 255)},
            {new Pixel(200, 200, 200, 255),
            new Pixel(50, 50, 50, 255)}};

    PixelInterface[][] expectedSharpenedPixels = {
            {new Pixel(106, 106, 106, 255),
            new Pixel(144, 144, 144, 255)},
            {new Pixel(206, 206, 206, 255),
            new Pixel(44, 44, 44, 255)}};

    Image originalImage =
            new Image(originalPixels.length, originalPixels[0].length);
    originalImage.imageFill(originalPixels);
    model.storeImage("original", originalImage);

    Image expectedSharpenedImage =
            new Image(originalPixels.length, originalPixels[0].length);
    expectedSharpenedImage.imageFill(expectedSharpenedPixels);
    List<String> commandTokens =
            List.of("sharpen", "original", "sharpenedResult");
    model.operationsFactoryCall("sharpen", commandTokens, model);

    ImageInterface sharpenedImage = model.getImage("sharpenedResult");

    assertEquals(expectedSharpenedImage, sharpenedImage);
  }

  /**
   * Tests the image compression operation, which reduces the pixel values
   * by a given factor (e.g., 50% compression).
   * The test verifies that the image is compressed correctly, with pixel
   * values adjusted accordingly.
   */
  @Test
  public void testCompressImage1() {
    /* 50% compression */
    PixelInterface[][] originalPixels = {
            {new Pixel(100, 100, 100, 255),
            new Pixel(150, 150, 150, 255)},
            {new Pixel(200, 200, 200, 255),
            new Pixel(50, 50, 50, 255)}};

    PixelInterface[][] expectedCompressedPixels = {
            {new Pixel(75, 75, 75, 255),
            new Pixel(175, 175, 175, 255)},
            {new Pixel(175, 175, 175, 255),
            new Pixel(75, 75, 75, 255)}};

    Image originalImage =
            new Image(originalPixels.length, originalPixels[0].length);
    originalImage.imageFill(originalPixels);
    model.storeImage("original", originalImage);

    Image expectedCompressedImage =
            new Image(originalPixels.length, originalPixels[0].length);
    expectedCompressedImage.imageFill(expectedCompressedPixels);
    List<String> commandTokens =
            List.of("compress", "50", "original", "compressedResult");
    model.operationsFactoryCall("compress", commandTokens, model);

    ImageInterface compressedImage = model.getImage("compressedResult");

    assertImagesEqual(
            expectedCompressedPixels, compressedImage, "compressedResult");
  }

  /**
   * Tests 0% image compression, ensuring the compressed image matches the
   * original.
   */
  @Test
  public void testCompressImage2() {
    /* 0% compression, same image test */
    PixelInterface[][] originalPixels = {
            {new Pixel(100, 100, 100, 255),
            new Pixel(150, 150, 150, 255)},
            {new Pixel(200, 200, 200, 255),
            new Pixel(50, 50, 50, 255)}};

    PixelInterface[][] expectedCompressedPixels = {
            {new Pixel(100, 100, 100, 255),
            new Pixel(150, 150, 150, 255)},
            {new Pixel(200, 200, 200, 255),
            new Pixel(50, 50, 50, 255)}};

    Image originalImage =
            new Image(originalPixels.length, originalPixels[0].length);
    originalImage.imageFill(originalPixels);
    model.storeImage("original", originalImage);

    Image expectedCompressedImage =
            new Image(originalPixels.length, originalPixels[0].length);
    expectedCompressedImage.imageFill(expectedCompressedPixels);
    List<String> commandTokens =
            List.of("compress", "0", "original", "compressedResult");
    model.operationsFactoryCall("compress", commandTokens, model);

    ImageInterface compressedImage = model.getImage("compressedResult");

    assertImagesEqual(
            expectedCompressedPixels, compressedImage, "compressedResult");
  }

  /**
   * Tests 100% image compression, ensuring the image is zeroed out.
   */
  @Test
  public void testCompressImage3() {
    /* 100% compression zeros out image test */
    PixelInterface[][] originalPixels = {
            {new Pixel(100, 100, 100, 255),
            new Pixel(150, 150, 150, 255)},
            {new Pixel(200, 200, 200, 255),
            new Pixel(50, 50, 50, 255)}};

    PixelInterface[][] expectedCompressedPixels = {
            {new Pixel(0, 0, 0, 255),
            new Pixel(0, 0, 0, 255)},
            {new Pixel(0, 0, 0, 255),
            new Pixel(0, 0, 0, 255)}};

    Image originalImage =
            new Image(originalPixels.length, originalPixels[0].length);
    originalImage.imageFill(originalPixels);
    model.storeImage("original", originalImage);

    Image expectedCompressedImage =
            new Image(originalPixels.length, originalPixels[0].length);
    expectedCompressedImage.imageFill(expectedCompressedPixels);
    List<String> commandTokens =
            List.of("compress", "100", "original", "compressedResult");
    model.operationsFactoryCall("compress", commandTokens, model);

    ImageInterface compressedImage = model.getImage("compressedResult");

    assertImagesEqual(
            expectedCompressedPixels, compressedImage, "compressedResult");
  }

  /**
   * Tests invalid compression percentage (< 0), expecting an
   * IllegalArgumentException.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testCompressImage4() {
    /* invalid compression percentage, < 0 */
    List<String> commandTokens =
            List.of("compress", "-5", "original", "compressedResult");
    model.operationsFactoryCall("compress", commandTokens, model);
  }

  /**
   * Tests invalid compression percentage (> 100), expecting an
   * IllegalArgumentException.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testCompressImage5() {
    /* invalid compression percentage, > 100 */
    List<String> commandTokens =
            List.of("compress", "101", "original", "compressedResult");
    model.operationsFactoryCall("compress", commandTokens, model);
  }

  /**
   * Tests compression on an image with non-power-of-2 dimensions, ensuring
   * padding is handled.
   */
  @Test
  public void testCompressImage6() {
    /* test for compression on image requiring padding, not of power of 2
     * dimensions */
    PixelInterface[][] originalPixels = {
            {new Pixel(100, 100, 100, 255),
            new Pixel(150, 150, 150, 255),
            new Pixel(150, 150, 150, 255)},
            {new Pixel(200, 200, 200, 255),
            new Pixel(50, 50, 50, 255),
            new Pixel(150, 150, 150, 255)}};

    PixelInterface[][] expectedCompressedPixels = {
            {new Pixel(50, 50, 50, 255),
            new Pixel(150, 150, 150, 255),
            new Pixel(175, 175, 175, 255)},
            {new Pixel(150, 150, 150, 255),
            new Pixel(50, 50, 50, 255),
            new Pixel(175, 175, 175, 255)}};

    Image originalImage =
            new Image(originalPixels.length, originalPixels[0].length);
    originalImage.imageFill(originalPixels);
    model.storeImage("original", originalImage);

    Image expectedCompressedImage =
            new Image(originalPixels.length, originalPixels[0].length);
    expectedCompressedImage.imageFill(expectedCompressedPixels);
    List<String> commandTokens =
            List.of("compress", "50", "original", "compressedResult");
    model.operationsFactoryCall("compress", commandTokens, model);

    ImageInterface compressedImage = model.getImage("compressedResult");
    assertImagesEqual(
            expectedCompressedPixels, compressedImage, "compressedResult");
  }

  /**
   * Tests the color correction operation on an image. The operation adjusts the
   * color balance of an image,
   * typically by modifying its RGB channels to achieve a more accurate or
   * visually pleasing result. In this test, a simplified color correction is
   * applied. The test compares the transformed image with the expected
   * color-corrected result.
   */
  @Test
  public void testColorCorrectionImage() {
    PixelInterface[][] originalPixels = {
            {new Pixel(100, 100, 100, 255),
            new Pixel(150, 150, 150, 255)},
            {new Pixel(200, 200, 200, 255),
            new Pixel(50, 50, 50, 255)}};

    PixelInterface[][] expectedColorCorrectedPixels = {
            {new Pixel(100, 100, 100, 255),
            new Pixel(150, 150, 150, 255)},
            {new Pixel(200, 200, 200, 255),
            new Pixel(50, 50, 50, 255)}};

    Image originalImage =
            new Image(originalPixels.length, originalPixels[0].length);
    originalImage.imageFill(originalPixels);
    model.storeImage("original", originalImage);

    Image expectedColorCorrectedImage =
            new Image(originalPixels.length, originalPixels[0].length);
    expectedColorCorrectedImage.imageFill(expectedColorCorrectedPixels);
    /* color-correct manas manas-color-corrected */
    List<String> commandTokens =
            List.of("color-correct", "original", "colorCorrectedResult");
    model.operationsFactoryCall("color-correct", commandTokens, model);

    ImageInterface colorCorrectedResult =
            model.getImage("colorCorrectedResult");

    assertEquals(expectedColorCorrectedImage, colorCorrectedResult);
  }

  /**
   * Tests the histogram filter operation on an image. The histogram filter
   * processes the image to analyze its pixel intensity distribution and can be
   * used to perform operations such as contrast enhancement or filtering based
   * on intensity levels. This test ensures the correct processing of the
   * histogram operation. It compares the processed result with a pre-saved
   * image (historgram.png) to verify the correctness of the operation.
   *
   * @throws IOException if an error occurs while loading the reference image
   */
  @Test
  public void testHistogramFilterImage() throws IOException {
    PixelInterface[][] originalPixels = {
            {new Pixel(100, 100, 100, 255),
            new Pixel(150, 150, 150, 255)},
            {new Pixel(200, 200, 200, 255),
            new Pixel(50, 50, 50, 255)}};

    Image originalImage =
            new Image(originalPixels.length, originalPixels[0].length);
    originalImage.imageFill(originalPixels);
    model.storeImage("original", originalImage);

    /* histogram manas manas-histogram */
    List<String> commandTokens =
            List.of("histogram", "original", "histogramResult");
    model.operationsFactoryCall("histogram", commandTokens, model);

    ImageInterface histogramResult = model.getImage("histogramResult");
    Map<String, int[][]> actualImageMatrix =
            convertImageToMapIntMatrix(histogramResult);
    BufferedImage bufferedImage = loadBufferedImage("test/ime/model/"
            + "testResources/histogram.png");
    Map<String, int[][]> imagePixelArr = convertTo2DPixelArray(bufferedImage);
    assertTrue(compareImageMaps(imagePixelArr, actualImageMatrix));
  }

  /**
   * Tests the levels adjustment operation on an image. This operation adjusts
   * the pixel values based on a provided range, scaling the image to a
   * specified range of levels (e.g., 20 to 255). The test ensures that the
   * pixel values are correctly transformed.
   */

  @Test
  public void testLevelAdjustImage1() {
    /* levels adjust all params within allowed range */
    PixelInterface[][] originalPixels = {
            {new Pixel(100, 100, 100, 255),
            new Pixel(150, 150, 150, 255)},
            {new Pixel(200, 200, 200, 255),
            new Pixel(50, 50, 50, 255)}};
    PixelInterface[][] expectedLevelAdjustPixels = {
            {new Pixel(92, 92, 92, 255),
            new Pixel(135, 135, 135, 255)},
            {new Pixel(165, 165, 165, 255),
            new Pixel(38, 38, 38, 255)}};

    Image originalImage =
            new Image(originalPixels.length, originalPixels[0].length);
    originalImage.imageFill(originalPixels);
    model.storeImage("original", originalImage);

    Image expectedLevelAdjustImage =
            new Image(originalPixels.length, originalPixels[0].length);
    expectedLevelAdjustImage.imageFill(expectedLevelAdjustPixels);
    /* levels-adjust 20 100 255 manas manas-levels-adjusted */
    List<String> commandTokens = List.of(
            CommandConstants.LEVELS_ADJUST, "20", "100", "255", "original", "levelAdjustedResult");
    model.operationsFactoryCall(CommandConstants.LEVELS_ADJUST, commandTokens, model);

    ImageInterface levelAdjustedResult = model.getImage("levelAdjustedResult");

    assertEquals(expectedLevelAdjustImage, levelAdjustedResult);
  }

  /**
   * Tests levels adjustment with invalid values, ensuring
   * IllegalArgumentException is thrown. Checks for out-of-range values for the
   * black, mid, and white level parameters in the levels-adjust operation.
   */
  @Test
  public void testLevelAdjustImage2() {
    /* levels adjust all params in not allowed ranges */

    String[] invalidValueStrings = {"-10", "300"};
    List<String> invalidRanges = Arrays.asList(invalidValueStrings);

    // Test all invalid positions: b, m, w
    for (int i = 0; i < 3; i++) {
      for (String invalidValue : invalidRanges) {
        // Create a valid commandTokens template
        List<String> commandTokens = new ArrayList<>(List.of(CommandConstants.LEVELS_ADJUST,
                "50", "100", "255", "original", "levelAdjustedResult"));

        // Replace the appropriate index for b, m, or w with invalid value
        commandTokens.set(i + 1, invalidValue);

        try {
          model.operationsFactoryCall(CommandConstants.LEVELS_ADJUST, commandTokens, model);
        } catch (IllegalArgumentException e) {
          assertEquals("Invalid levels adjustment values. " +
                  "Ensure 0 <= b < m < w <= 255.", e.getMessage());
        }
      }
    }
  }


  /**
   * Tests converting an image to a specific format (e.g., a 2D map) and storing
   * it in the model. This test ensures that the image can be successfully
   * converted and retrieved correctly from the model.
   */
  @Test
  public void testConvertAndStoreImage() {
    PixelInterface[][] originalPixels = {
            {new Pixel(100, 100, 100, 255),
            new Pixel(150, 150, 150, 255)},
            {new Pixel(200, 200, 200, 255),
            new Pixel(50, 50, 50, 255)}};
    ImageInterface originalImage =
            new Image(originalPixels.length, originalPixels[0].length);
    originalImage.imageFill(originalPixels);
    Map<String, int[][]> imgPixelArr =
            convertImageToMapIntMatrix(originalImage);
    model.convertAndStoreImage("original", imgPixelArr);
    ImageInterface fetchedImage = model.getImage("original");
    assertEquals(originalImage, fetchedImage);
  }

  /**
   * Tests converting and fetching an image from the model. This test ensures
   * that an image can be stored and then fetched back in the form of a 2D pixel
   * array, where individual channels (red, green, blue, alpha) are compared
   * between the stored and fetched images.
   */
  @Test
  public void testConvertAndFetchImage() {
    PixelInterface[][] originalPixels = {
            {new Pixel(100, 100, 100, 255),
            new Pixel(150, 150, 150, 255)},
            {new Pixel(200, 200, 200, 255),
            new Pixel(50, 50, 50, 255)}};
    ImageInterface originalImage =
            new Image(originalPixels.length, originalPixels[0].length);
    originalImage.imageFill(originalPixels);
    model.storeImage("original", originalImage);
    Map<String, int[][]> originalImagePixelArray =
            model.convertAndFetchImage("original");
    Map<String, int[][]> imgPixelArr =
            convertImageToMapIntMatrix(originalImage);
    assertTrue(compareIntArrays(
            originalImagePixelArray.get("red"), imgPixelArr.get("red")));
    assertTrue(compareIntArrays(
            originalImagePixelArray.get("green"), imgPixelArr.get("green")));
    assertTrue(compareIntArrays(
            originalImagePixelArray.get("blue"), imgPixelArr.get("blue")));
    assertTrue(compareIntArrays(
            originalImagePixelArray.get("alpha"), imgPixelArr.get("alpha")));
  }

  /**
   * Tests the hash code method for two images that are identical.
   * The test ensures that two images with
   * the same pixel data produce the same hash code.
   */
  @Test
  public void testHashCodeSameForSameImage() {
    PixelInterface[][] originalPixels = {
            {new Pixel(100, 100, 100, 255),
            new Pixel(150, 150, 150, 255)},
            {new Pixel(200, 200, 200, 255),
            new Pixel(50, 50, 50, 255)}};

    Image image1 = new Image(originalPixels.length, originalPixels[0].length);
    image1.imageFill(originalPixels);

    Image image2 = new Image(originalPixels.length, originalPixels[0].length);
    image2.imageFill(originalPixels);
    assertEquals(image1.hashCode(), image2.hashCode());
  }

  /**
   * Tests the hash code method for two images that are different.
   * The test ensures that two images with
   * different pixel data produce different hash codes.
   */
  @Test
  public void testHashCodeDifferentForDifferentImage() {
    PixelInterface[][] originalPixels1 = {
            {new Pixel(100, 100, 100, 255),
            new Pixel(150, 150, 150, 255)},
            {new Pixel(200, 200, 200, 255),
            new Pixel(50, 50, 50, 255)}};

    PixelInterface[][] originalPixels2 = {
            {new Pixel(120, 100, 100, 255),
            new Pixel(150, 150, 150, 255)},
            {new Pixel(200, 200, 200, 255),
            new Pixel(50, 50, 50, 255)}};
    Image image1 = new Image(originalPixels1.length, originalPixels1[0].length);
    image1.imageFill(originalPixels1);

    Image image2 = new Image(originalPixels2.length, originalPixels2[0].length);
    image2.imageFill(originalPixels2);
    assertNotEquals(image1.hashCode(), image2.hashCode());
  }

  /**
   * Method to convert Image To Map Int Matrix.
   *
   * @param image ImageInterface
   * @return Map of string to int 2d array
   */
  public Map<String, int[][]> convertImageToMapIntMatrix(ImageInterface image) {
    int rows = image.getHeight();
    int cols = image.getWidth();

    Map<String, int[][]> imgPixelArr = new HashMap<>();
    try {
      imgPixelArr.put("red", new int[rows][cols]);
      imgPixelArr.put("green", new int[rows][cols]);
      imgPixelArr.put("blue", new int[rows][cols]);
      imgPixelArr.put("alpha", new int[rows][cols]);

      for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
          PixelInterface pixel = image.getPixel(i, j);
          if (pixel == null) {
            continue;
          }
          imgPixelArr.get("red")[i][j] = pixel.getR();
          imgPixelArr.get("green")[i][j] = pixel.getG();
          imgPixelArr.get("blue")[i][j] = pixel.getB();
          imgPixelArr.get("alpha")[i][j] = pixel.getA();
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return imgPixelArr;
  }

  private static Map<String, int[][]> convertTo2DPixelArray(
          BufferedImage bufferedImage) {
    int width = bufferedImage.getWidth();
    int height = bufferedImage.getHeight();
    int[][] redMatrix = new int[height][width];
    int[][] greenMatrix = new int[height][width];
    int[][] blueMatrix = new int[height][width];
    int[][] alphaMatrix = new int[height][width];

    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        int argb = bufferedImage.getRGB(x, y);
        redMatrix[y][x] = (argb >> 16) & 0xFF;
        greenMatrix[y][x] = (argb >> 8) & 0xFF;
        blueMatrix[y][x] = argb & 0xFF;
        alphaMatrix[y][x] = (argb >> 24) & 0xFF;
      }
    }

    return Map.of("red", redMatrix, "green", greenMatrix, "blue", blueMatrix,
            "alpha", alphaMatrix);
  }

  private BufferedImage loadBufferedImage(String imageFilePath)
          throws IOException {
    String fileType = "png";
    ImageReader reader = ImageReaderFactory.getReader(fileType);
    return reader.read(imageFilePath);
  }

  /**
   * Method to compare image maps.
   *
   * @param map1 map of string to 2d int array
   * @param map2 map of string to 2d int array
   * @return boolean
   */
  public static boolean compareImageMaps(
          Map<String, int[][]> map1, Map<String, int[][]> map2) {
    if (map1 == null || map2 == null) {
      return map1 == map2;
    }
    if (map1.size() != map2.size()) {
      return false;
    }
    for (Map.Entry<String, int[][]> entry : map1.entrySet()) {
      String key = entry.getKey();
      int[][] array1 = entry.getValue();
      if (!map2.containsKey(key)) {
        return false;
      }
      int[][] array2 = map2.get(key);
      if (!compareIntArrays(array1, array2)) {
        return false;
      }
    }
    return true;
  }

  private static boolean compareIntArrays(int[][] array1, int[][] array2) {
    if (array1 == null || array2 == null) {
      return array1 == array2;
    }
    if (array1.length != array2.length) {
      return false;
    }
    for (int i = 0; i < array1.length; i++) {
      if (array1[i].length != array2[i].length) {
        return false;
      }
      for (int j = 0; j < array1[i].length; j++) {
        if (array1[i][j] != array2[i][j]) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * Tests blur operation in split view mode at 50%.
   */
  @Test
  public void testBlurSplitView1() {
    List<String> commandTokens =
            List.of("blur", "original", "blurSplitResult", "split", "50");
    model.operationsFactoryCall("blur", commandTokens, model);

    ImageInterface blurredImage = model.getImage("blurSplitResult");

    PixelInterface[][] expectedBlurredPixels = {
            {new Pixel(125, 125, 125, 255),
            new Pixel(0, 0, 0, 255)},
            {new Pixel(150, 150, 150, 255),
            new Pixel(0, 0, 0, 255)}};

    assertImagesEqual(expectedBlurredPixels, blurredImage, "Blur Split");
  }

  /**
   * Tests blur operation in split view mode at 75%.
   */
  @Test
  public void testBlurSplitView2() {
    List<String> commandTokens =
            List.of("blur", "original", "blurSplitResult", "split", "75");
    model.operationsFactoryCall("blur", commandTokens, model);

    ImageInterface blurredImage = model.getImage("blurSplitResult");

    PixelInterface[][] expectedBlurredPixels = {
            {new Pixel(125, 125, 125, 255),
            new Pixel(0, 0, 0, 255)},
            {new Pixel(150, 150, 150, 255),
            new Pixel(0, 0, 0, 255)}};

    assertImagesEqual(expectedBlurredPixels, blurredImage, "Blur Split");
  }

  /**
   * Tests sharpen operation in split view mode at 50%.
   */
  @Test
  public void testSharpenSplitView1() {
    List<String> commandTokens =
            List.of("sharpen", "original", "sharpenSplitResult", "split", "50");
    model.operationsFactoryCall("sharpen", commandTokens, model);

    ImageInterface sharpenedImage = model.getImage("sharpenSplitResult");

    PixelInterface[][] expectedSharpenedPixels = {
            {new Pixel(106, 106, 106, 255),
            new Pixel(0, 0, 0, 255)},
            {new Pixel(206, 206, 206, 255),
            new Pixel(0, 0, 0, 255)}};

    assertImagesEqual(expectedSharpenedPixels, sharpenedImage, "Sharpen Split");
  }

  /**
   * Tests sharpen operation in split view mode at 75%.
   */
  @Test
  public void testSharpenSplitView2() {
    List<String> commandTokens =
            List.of("sharpen", "original", "sharpenSplitResult", "split", "75");
    model.operationsFactoryCall("sharpen", commandTokens, model);

    ImageInterface sharpenedImage = model.getImage("sharpenSplitResult");

    PixelInterface[][] expectedSharpenedPixels = {
            {new Pixel(106, 106, 106, 255),
            new Pixel(0, 0, 0, 255)},
            {new Pixel(206, 206, 206, 255),
            new Pixel(0, 0, 0, 255)}};

    assertImagesEqual(expectedSharpenedPixels, sharpenedImage, "Sharpen Split");
  }

  /**
   * Tests sepia operation in split view mode at 50%.
   */
  @Test
  public void testSepiaSplitView1() {
    List<String> commandTokens =
            List.of("sepia", "original", "sepiaSplitResult", "split", "50");
    model.operationsFactoryCall("sepia", commandTokens, model);

    ImageInterface sepiaImage = model.getImage("sepiaSplitResult");

    PixelInterface[][] expectedSepiaPixels = {
            {new Pixel(135, 120, 93, 255),
            new Pixel(0, 0, 0, 255)},
            {new Pixel(255, 240, 187, 255),
            new Pixel(0, 0, 0, 255)}};

    assertImagesEqual(expectedSepiaPixels, sepiaImage, "Sepia Split");
  }

  /**
   * Tests sepia operation in split view mode at 75%.
   */
  @Test
  public void testSepiaSplitView2() {
    List<String> commandTokens =
            List.of("sepia", "original", "sepiaSplitResult", "split", "75");
    model.operationsFactoryCall("sepia", commandTokens, model);

    ImageInterface sepiaImage = model.getImage("sepiaSplitResult");

    PixelInterface[][] expectedSepiaPixels = {
            {new Pixel(135, 120, 93, 255),
            new Pixel(0, 0, 0, 255)},
            {new Pixel(255, 240, 187, 255),
            new Pixel(0, 0, 0, 255)}};

    assertImagesEqual(expectedSepiaPixels, sepiaImage, "Sepia Split");
  }

  /**
   * Tests greyscale operation in split view mode at 50%.
   */
  @Test
  public void testGreyscaleSplitView1() {
    List<String> commandTokens =
            List.of("luma-component", "original", "lumaSplitResult", "split", "50");
    model.operationsFactoryCall("luma-component", commandTokens, model);

    ImageInterface greyscaleImage = model.getImage("lumaSplitResult");

    PixelInterface[][] greyscaleExpectedPixels = {
            {new Pixel(100, 100, 100, 255),
            new Pixel(0, 0, 0, 255)},
            {new Pixel(200, 200, 200, 255),
            new Pixel(0, 0, 0, 255)}};

    assertImagesEqual(
            greyscaleExpectedPixels, greyscaleImage, "Greyscale Split");
  }

  /**
   * Tests greyscale operation in split view mode at 75%.
   */
  @Test
  public void testGreyscaleSplitView2() {
    List<String> commandTokens =
            List.of("luma-component", "original", "lumaSplitResult", "split", "75");
    model.operationsFactoryCall("luma-component", commandTokens, model);

    ImageInterface greyscaleImage = model.getImage("lumaSplitResult");

    PixelInterface[][] greyscaleExpectedPixels = {
            {new Pixel(100, 100, 100, 255),
            new Pixel(0, 0, 0, 255)},
            {new Pixel(200, 200, 200, 255),
            new Pixel(0, 0, 0, 255)}};

    assertImagesEqual(
            greyscaleExpectedPixels, greyscaleImage, "Greyscale Split");
  }

  /**
   * Tests color correction in split view mode at 50%.
   */
  @Test
  public void testColorCorrectionSplitView1() {
    List<String> commandTokens = List.of(
            "color-correct", "original", "colorCorrectSplitResult", "split", "50");
    model.operationsFactoryCall("color-correct", commandTokens, model);

    ImageInterface colorCorrected = model.getImage("colorCorrectSplitResult");

    PixelInterface[][] colorCorrectExpectedPixels = {
            {new Pixel(100, 100, 100, 255),
            new Pixel(0, 0, 0, 255)},
            {new Pixel(200, 200, 200, 255),
            new Pixel(0, 0, 0, 255)}};

    assertImagesEqual(
            colorCorrectExpectedPixels, colorCorrected, "Color Correction Split");
  }

  /**
   * Tests color correction in split view mode at 75%.
   */
  @Test
  public void testColorCorrectionSplitView2() {
    List<String> commandTokens = List.of(
            "color-correct", "original", "colorCorrectSplitResult", "split", "75");
    model.operationsFactoryCall("color-correct", commandTokens, model);

    ImageInterface colorCorrected = model.getImage("colorCorrectSplitResult");

    PixelInterface[][] colorCorrectExpectedPixels = {
            {new Pixel(100, 100, 100, 255),
            new Pixel(0, 0, 0, 255)},
            {new Pixel(200, 200, 200, 255),
            new Pixel(0, 0, 0, 255)}};

    assertImagesEqual(
            colorCorrectExpectedPixels, colorCorrected, "Color Correction Split");
  }

  /**
   * Tests levels adjustment in split view mode at 50%.
   */
  @Test
  public void testLevelsAdjustmentSplitView1() {
    List<String> commandTokens = List.of("levels-adjust", "20", "100", "255",
            "original", "levelsAdjustSplitResult", "split", "50");
    model.operationsFactoryCall("levels-adjust", commandTokens, model);

    ImageInterface levelsAdjusted = model.getImage("levelsAdjustSplitResult");

    PixelInterface[][] levelsAdjustExpectedPixels = {
            {new Pixel(92, 92, 92, 255),
            new Pixel(0, 0, 0, 255)},
            {new Pixel(165, 165, 165, 255),
            new Pixel(0, 0, 0, 255)}};

    assertImagesEqual(
            levelsAdjustExpectedPixels, levelsAdjusted, "Levels Adjustment Split");
  }

  /**
   * Tests levels adjustment in split view mode at 75%.
   */
  @Test
  public void testLevelsAdjustmentSplitView2() {
    List<String> commandTokens = List.of("levels-adjust", "20", "100", "255",
            "original", "levelsAdjustSplitResult", "split", "75");
    model.operationsFactoryCall("levels-adjust", commandTokens, model);

    ImageInterface levelsAdjusted = model.getImage("levelsAdjustSplitResult");

    PixelInterface[][] levelsAdjustExpectedPixels = {
            {new Pixel(92, 92, 92, 255),
            new Pixel(0, 0, 0, 255)},
            {new Pixel(165, 165, 165, 255),
            new Pixel(0, 0, 0, 255)}};

    assertImagesEqual(
            levelsAdjustExpectedPixels, levelsAdjusted, "Levels Adjustment Split");
  }

  /**
   * Tests operations with invalid split percentages, ensuring
   * IllegalArgumentException is thrown. Checks for split percentages outside
   * the valid range (0 to 100) for operations blur, sharpen, sepia,
   * luma-component, color-correct, and levels-adjust.
   */
  @Test
  public void testInvalidSplitPercentageOperations() {
    String[] invalidValueStrings = {"-10", "101"};
    List<String> invalidRanges = Arrays.asList(invalidValueStrings);
    boolean checkedAll = true;
    invalidRanges.forEach(item -> {
      /* all operations that can be done with split percentage */
      Map<String, List<String>> operationToCommandTokens = new HashMap<>();
      operationToCommandTokens.put("blur",
              List.of("blur", "original", "blurSplitResult", "split", item));
      operationToCommandTokens.put("sharpen",
              List.of("sharpen", "original", "sharpenSplitResult", "split", item));
      operationToCommandTokens.put("sepia",
              List.of("sepia", "original", "sepiaSplitResult", "split", item));
      operationToCommandTokens.put(CommandConstants.LUMA_COMPONENT,
              List.of(
                      CommandConstants.LUMA_COMPONENT,
                      "original", "lumaSplitResult", "split", item));
      operationToCommandTokens.put(CommandConstants.COLOR_CORRECT,
              List.of(CommandConstants.COLOR_CORRECT, "original", "colorCorrectSplitResult",
                      "split", item));
      operationToCommandTokens.put(CommandConstants.LEVELS_ADJUST,
              List.of(CommandConstants.LEVELS_ADJUST, "20", "100", "255", "original",
                      "levelsAdjustSplitResult", "split", item));

      operationToCommandTokens.forEach((operation, commandTokens) -> {
        try {
          model.operationsFactoryCall(operation, commandTokens, model);
        } catch (IllegalArgumentException exception) {
          assertEquals(
                  "Split percent must be between 0 and 100", exception.getMessage());
        }
      });
    });
    assertTrue(checkedAll);
  }

  /**
   * Tests operations with split percentage at 0%, and comparing against
   * expected image, where expected image is the original image, with split line
   * present on left end, at 0% location. Checks for operations blur, sharpen,
   * sepia, luma-component, color-correct, and levels-adjust.
   */
  @Test
  public void testSplitOperationsAtZeroPercent() {
    /* split operations with 0% split, check if the operation output is the full
     * image */
    String item = "0";
    /* all operations that can be done with split percentage */
    Map<String, List<String>> operationToCommandTokens = new HashMap<>();
    operationToCommandTokens.put(
            "blur", List.of("blur", "original", "blur", "split", item));
    operationToCommandTokens.put(
            "sharpen", List.of("sharpen", "original", "sharpen", "split", item));
    operationToCommandTokens.put(
            "sepia", List.of("sepia", "original", "sepia", "split", item));
    operationToCommandTokens.put(CommandConstants.LUMA_COMPONENT,
            List.of(CommandConstants.LUMA_COMPONENT, "original",
                    CommandConstants.LUMA_COMPONENT, "split", item));
    operationToCommandTokens.put("color-correct",
            List.of(CommandConstants.COLOR_CORRECT, "original",
                    CommandConstants.COLOR_CORRECT, "split", item));
    operationToCommandTokens.put(CommandConstants.LEVELS_ADJUST,
            List.of(CommandConstants.LEVELS_ADJUST, "20", "100", "255", "original",
                    CommandConstants.LEVELS_ADJUST, "split", item));

    /* fetching the original image */
    ImageInterface original = model.getImage("original");

    PixelInterface[][] expectedPixelsWithOutputLine = {
            {new Pixel(0, 0, 0, 255),
            new Pixel(150, 150, 150, 255)},
            {new Pixel(0, 0, 0, 255),
            new Pixel(50, 50, 50, 255)}};

    ImageInterface expectedOutput =
            new Image(original.getHeight(), original.getWidth());
    expectedOutput.imageFill(expectedPixelsWithOutputLine);
    /* asserting that the original & output images same for every split
     * operation, with 0 %split */
    boolean checkedAll = true;
    operationToCommandTokens.forEach((operation, commandTokens) -> {
      model.operationsFactoryCall(operation, commandTokens, model);
      ImageInterface outputImage = model.getImage(operation);
      assertEquals(expectedOutput, outputImage);
    });
    assertTrue(checkedAll);
  }

  /**
   * Tests operations with split percentage at 0%, and comparing against
   * expected image, where expected image is the original image, with split line
   * present on left end, at 0% location. Checks for operations blur, sharpen,
   * sepia, luma-component, color-correct, and levels-adjust.
   */
  @Test
  public void testSplitOperationsAtHundredPercent() {
    /* split operations with 100% split, check if the operation output applied
     * on full image */
    String item = "100";
    /* all operations that can be done with split percentage */
    Map<String, List<String>> operationToCommandTokens = new HashMap<>();
    operationToCommandTokens.put(
            "blur", List.of("blur", "original", "blur", "split", item));
    operationToCommandTokens.put(
            "sharpen", List.of("sharpen", "original", "sharpen", "split", item));
    operationToCommandTokens.put(
            "sepia", List.of("sepia", "original", "sepia", "split", item));
    operationToCommandTokens.put(CommandConstants.LUMA_COMPONENT,
            List.of(CommandConstants.LUMA_COMPONENT, "original",
                    CommandConstants.LUMA_COMPONENT, "split", item));
    operationToCommandTokens.put(CommandConstants.COLOR_CORRECT,
            List.of(CommandConstants.COLOR_CORRECT, "original",
                    CommandConstants.COLOR_CORRECT, "split", item));
    operationToCommandTokens.put(CommandConstants.LEVELS_ADJUST,
            List.of(CommandConstants.LEVELS_ADJUST, "20", "100", "255", "original",
                    CommandConstants.LEVELS_ADJUST, "split", item));

    /* fetching the original image */
    ImageInterface original = model.getImage("original");

    ImageInterface expectedOutput =
            new Image(original.getHeight(), original.getWidth());
    Map<String, PixelInterface[][]> expectedPixelsWithSplitLine =
            getExpectedPixelsWithSplitLine();

    // Loop through each operation and assert correctness
    for (Map.Entry<String, List<String>> entry : operationToCommandTokens.entrySet()) {
      String operation = entry.getKey();
      List<String> commandTokens = entry.getValue();

      expectedOutput.imageFill(expectedPixelsWithSplitLine.get(operation));
      model.operationsFactoryCall(operation, commandTokens, model);
      ImageInterface outputImage = model.getImage(operation);

      // Explicit assertion for static analysis tools
      assertEquals(
              "The output image for operation " + operation +
                      " does not match the expected output.",
              expectedOutput, outputImage);
    }
  }


  /**
   * Test for downscaling an image to the same dimensions.
   * This test ensures that when the target width & height are equal to original
   * image's dimensions, the downscaled image should be identical to the
   * original.
   */
  @Test
  public void testDownscaling1() {
    List<String> commandTokens =
            List.of("downscale", "2", "2", "original", "downscaleResult");
    model.operationsFactoryCall("downscale", commandTokens, model);
    ImageInterface downscaledImage = model.getImage("downscaleResult");
    assertEquals(originalImage, downscaledImage);
  }

  /**
   * Test for downscaling with only the width changed.
   * This test checks that when the width is reduced but the height remains the
   * same, the image is downscaled correctly according to the new width.
   */
  @Test
  public void testDownscaling2() {
    PixelInterface[][] downscaledPixels = {
            {new Pixel(100, 100, 100, 255)},
            {new Pixel(200, 200, 200, 255)}};
    ImageInterface expectedImage =
            new Image(downscaledPixels.length, downscaledPixels[0].length);
    expectedImage.imageFill(downscaledPixels);
    List<String> commandTokens =
            List.of("downscale", "1", "2", "original", "downscaleResult");
    model.operationsFactoryCall("downscale", commandTokens, model);
    ImageInterface downscaledImage = model.getImage("downscaleResult");
    assertEquals(expectedImage, downscaledImage);
  }

  /**
   * Test for downscaling with only the height changed.
   * This test ensures that when the height is reduced but the width remains the
   * same, the image is downscaled correctly according to the new height.
   */
  @Test
  public void testDownscaling3() {
    PixelInterface[][] downscaledPixels = {
            {new Pixel(100, 100, 100, 255),
            new Pixel(150, 150, 150, 255)}};
    ImageInterface expectedImage =
            new Image(downscaledPixels.length, downscaledPixels[0].length);
    expectedImage.imageFill(downscaledPixels);
    List<String> commandTokens =
            List.of("downscale", "2", "1", "original", "downscaleResult");
    model.operationsFactoryCall("downscale", commandTokens, model);
    ImageInterface downscaledImage = model.getImage("downscaleResult");
    assertEquals(expectedImage, downscaledImage);
  }

  /**
   * Test for downscaling with both width and height changed.
   * This test checks if downscaling works correctly when both dimensions are
   * modified. The downscaled image should reflect the new width and height.
   */
  @Test
  public void testDownscaling4() {
    List<String> commandTokens =
            List.of("downscale", "1", "1", "original", "downscaleResult");
    model.operationsFactoryCall("downscale", commandTokens, model);

    ImageInterface downscaledImage = model.getImage("downscaleResult");

    PixelInterface[][] downscaledPixels = {{new Pixel(100, 100, 100, 255)}};
    ImageInterface expectedImage =
            new Image(downscaledPixels.length, downscaledPixels[0].length);
    expectedImage.imageFill(downscaledPixels);
    assertEquals(expectedImage, downscaledImage);
  }

  /**
   * Test for downscaling with a negative width.
   * This test ensures that an exception is thrown when attempting downscale to
   * a negative width. The expected behavior is that an IllegalArgumentException
   * is raised.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testDownscaling5() {
    List<String> commandTokens =
            List.of("downscale", "-1", "1", "original", "downscaleResult");
    model.operationsFactoryCall("downscale", commandTokens, model);
  }

  /**
   * Test for downscaling with a negative height.
   * This test ensures that an exception is thrown when attempting downscale to
   * a negative height. The expected behavior is that an
   * IllegalArgumentException is raised.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testDownscaling6() {
    List<String> commandTokens =
            List.of("downscale", "1", "-1", "original", "downscaleResult");
    model.operationsFactoryCall("downscale", commandTokens, model);
  }

  /**
   * Test for downscaling with a width greater than the original image's width.
   * This test checks that an IllegalArgumentException is thrown when attempting
   * downscale to a width that exceeds the original image's width.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testDownscaling7() {
    List<String> commandTokens =
            List.of("downscale", "3", "2", "original", "downscaleResult");
    model.operationsFactoryCall("downscale", commandTokens, model);
  }

  /**
   * Test for downscaling with a height greater than the original image's
   * height. This test checks that an IllegalArgumentException is thrown when
   * attempting downscale to a height that exceeds the original image's height.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testDownscaling8() {
    List<String> commandTokens =
            List.of("downscale", "2", "3", "original", "downscaleResult");
    model.operationsFactoryCall("downscale", commandTokens, model);
  }

  /**
   * Test for downscaling with fractional mappings (non-integer pixel values).
   * This test checks if the algorithm correctly handles downscaling when the
   * source pixel positions involve fractional mappings, ensuring the resulting
   * downscaled image matches expected outcome.
   */
  @Test
  public void testDownscaling9() {
    PixelInterface[][] originalPixels = {
            {new Pixel(100, 100, 100, 255),
            new Pixel(150, 150, 150, 255),
            new Pixel(150, 150, 150, 255),
            new Pixel(150, 150, 150, 255),
            new Pixel(150, 150, 150, 255)},
            {new Pixel(200, 200, 200, 255),
            new Pixel(50, 50, 50, 255),
            new Pixel(50, 50, 50, 255),
            new Pixel(150, 150, 150, 255),
            new Pixel(150, 150, 150, 255)},
            {new Pixel(200, 200, 200, 255),
            new Pixel(50, 50, 50, 255),
            new Pixel(50, 50, 50, 255),
            new Pixel(150, 150, 150, 255),
            new Pixel(150, 150, 150, 255)}};

    ImageInterface originalImage =
            new Image(originalPixels.length, originalPixels[0].length);
    originalImage.imageFill(originalPixels);
    model.storeImage("original", originalImage);

    PixelInterface[][] downscaledPixels = {
            {new Pixel(100, 100, 100, 255),
            new Pixel(150, 150, 150, 255),
            new Pixel(150, 150, 150, 255),
            new Pixel(150, 150, 150, 255),
            new Pixel(150, 150, 150, 255)},
            {new Pixel(200, 200, 200, 255),
            new Pixel(50, 50, 50, 255),
            new Pixel(50, 50, 50, 255),
            new Pixel(150, 150, 150, 255),
            new Pixel(0, 0, 0, 255)},
    };
    ImageInterface expectedImage =
            new Image(downscaledPixels.length, downscaledPixels[0].length);
    expectedImage.imageFill(downscaledPixels);
    List<String> commandTokens =
            List.of("downscale", "5", "2", "original", "downscaleResult");
    model.operationsFactoryCall("downscale", commandTokens, model);
    ImageInterface downscaledImage = model.getImage("downscaleResult");
    assertEquals(expectedImage, downscaledImage);
  }

  /**
   * Test for downscaling with a height which is zero.
   * This test checks that an IllegalArgumentException is thrown when attempting
   * downscale to a height which is 0.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testDownscaling10() {
    List<String> commandTokens =
            List.of("downscale", "2", "0", "original", "downscaleResult");
    model.operationsFactoryCall("downscale", commandTokens, model);
  }

  /**
   * Test for downscaling with a width which is zero.
   * This test checks that an IllegalArgumentException is thrown when attempting
   * downscale to a width which is 0.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testDownscaling11() {
    List<String> commandTokens =
            List.of("downscale", "0", "1", "original", "downscaleResult");
    model.operationsFactoryCall("downscale", commandTokens, model);
  }
}