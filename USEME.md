# USEME File for Image Processing Application

## How to Run from IntelliJ

_Checkout to project directory_

_To Run using jar file:_
1. *(This will run whole script file)* Run this command: ***java -jar <_PathToJarFileWithName_>.jar -file <_scriptFilePathWithName_>***
2. OR *(This will run commands one by one)* Run this cmd: ***java -jar <_PathToJarFileWithName_>.jar -text***
3. OR *(This will launch the GUI)* Run this cmd: ***java -jar <_PathToJarFileWithName_>.jar***

_To run from "main":_
1. **ImageProcessor** is our main class inside src folder.
2. Just go to main and run it! It will launch the GUI.

### Supported Commands

#### Loading and Saving Images
- **load `<filepath>` `<imageName>`**
    - Loads an image from the specified file path and assigns it a name for further operations.
    - Example: `load res/Manas.png manas`

- **save `<filepath>` `<imageName>`**
    - Saves the specified image to the given file path.
    - Example: `save res/manas-sepia.png manas-sepia`

#### Color Adjustments
- **color-correct `<sourceImage>` `<destImage>`**
    - Applies color correction to the source image and saves it as a new image.
    - Example: `color-correct manas manas-color-corrected`

- **levels-adjust `<min>` `<mid>` `<max>` `<sourceImage>` `<destImage>`**
    - Adjusts the levels of the image based on the specified min, mid, and max values.
    - Example: `levels-adjust 20 100 255 manas manas-levels-adjusted`

#### Split View Operations
- **blur `<sourceImage>` `<destImage>` split `<percentage>`**
- **sharpen `<sourceImage>` `<destImage>` split `<percentage>`**
- **sepia `<sourceImage>` `<destImage>` split `<percentage>`**
- **color-correct `<sourceImage>` `<destImage>` split `<percentage>`**
- **levels-adjust `<min>` `<mid>` `<max>` `<sourceImage>` `<destImage>` split `<percentage>`**
    - Applies the specified operation to a portion of the image defined by the percentage.
    - Example: `blur manas manas-blur-split split 50`

#### Color Components
- **rgb-split `<sourceImage>` `<redDest>` `<greenDest>` `<blueDest>`**
    - Splits the image into its RGB components and saves each as a separate image.
    - Example: `rgb-split manas manas-red manas-green manas-blue`

- **rgb-combine `<destImage>` `<redSource>` `<greenSource>` `<blueSource>`**
    - Combines separate RGB component images into a single image.
    - Example: `rgb-combine manas-combined manas-red manas-green manas-blue`

- **red-component/green-component/blue-component/luma-component/intensity-component/value-component**
    - Extracts and saves the specified color or intensity component of an image.
    - Examples:
        - `red-component manas manas-red-component`
        - `luma-component manas manas-luma`

#### Brightness Adjustments
- **brighten `<amount>` `<sourceImage>` `<destImage>`**
    - Adjusts the brightness of an image by the specified amount.
    - Example: `brighten 50 manas manas-brighter`

#### Flipping Operations
- **vertical-flip/horizontal-flip**
    - Flips the image vertically or horizontally.
    - Examples:
        - `vertical-flip manas manas-vertical`
        - `horizontal-flip manas manas-horizontal`

#### Blur and Sharpen
- **blur/sharpen**
    - Applies blur or sharpen effects to an image multiple times for enhanced effect.
    - Examples:
        - `blur manas manas-blurred1`
        - `sharpen manas manas-sharpened1`

#### Compression
- **compress `<percentage>` `<sourceImage>` `<destImage>`**
    - Compresses an image by reducing its data size based on the specified percentage.
    - Example: `compress 100 Manas Manas-100`
  
#### Downscaling
- **downscale `<width>` `<height>` `<sourceImage>` `<destImage>`**
    - Downscales an image to the given width and height.
    - Example: `downscale 100 150 Manas Manas-downscaled`

### Conditions and Notes
- Ensure that images are loaded using the `load` command before applying any transformations or operations.
- The `save` command should be used after transformations to store results.
- Split view operations require specifying a percentage of the image to apply effects to.
- The compression command should be used with caution as high compression percentages may lead to significant loss of detail.

This guide provides an overview of commands supported by your application, along with examples for each operation.

## GUI

This document provides instructions on how to use the graphical user interface (GUI) for the image processing application.

### Starting the Application

1. Run the main application file to launch the GUI.
2. The main window will appear, displaying the application title and various control buttons.

### Loading an Image

1. Click the "Load Image" button on the left side of the application window.
2. A file chooser dialog will appear. Navigate to and select the image file you want to process.
3. The selected image will be displayed in the main panel of the application.

### Applying Image Operations

The following operations are available through buttons on the left side of the application:

- Color Correction
- Levels Adjustment
- Blur
- Sharpen
- Value Component
- Luma Component
- Intensity Component
- Red Component
- Blue Component
- Green Component
- Vertical Flip
- Horizontal Flip
- Sepia Tone
- Compress
- Brighten
- Downscaling

To apply an operation:

1. Click the corresponding button for the desired operation.
2. For operations that require additional input (e.g., Brighten, Compress, Levels Adjustment):
    - A dialog box will appear prompting for the required values.
    - Enter the requested information and click OK.
3. For operations with split view preview (e.g., Color Correction, Blur, Sharpen, Sepia):
    - A dialog will ask if you want to use the split view.
    - If you choose "Yes", a preview window will appear showing the effect of the operation.
    - Adjust the split percentage using the text field and "Update Preview" button.
    - Click "Apply" to confirm the operation or "Cancel" to discard it.
4. After each filter apply, the program offers you with a choice whether to save image or not. Here save means actually writing the image to a file/directory in your system.

### Navigating Image Versions

- Use the "Previous" and "Next" buttons at the top of the window to navigate between different versions of the image after applying operations.

### Viewing Histogram

- The histogram of the current image is displayed at the bottom of the application window.
- It updates automatically when a new image is loaded or an operation is applied.

### Saving an Image

1. After applying desired operations, click the "Save" button.
2. A file chooser dialog will appear.
3. Choose the location where you want to save the image, enter a file name, and click "Save".

### Error Handling

- If an error occurs during any operation, an error dialog will appear with a description of the problem.
- Click "OK" to dismiss the error message and continue using the application.

### Exiting the Application

- Close the main application window to exit the program.

Note: Always ensure that an image is loaded before attempting to apply any operations. Some operations may take a moment to process, especially for larger images.
