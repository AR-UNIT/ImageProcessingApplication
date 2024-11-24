package ime.model;

/**
 * The {@code KernelOperation} interface defines the structure for transformation
 * operations that use a kernel matrix, such as convolution operations in image processing.
 * Implementing classes are expected to provide a specific kernel matrix used in these operations.
 */
interface KernelOperation {
  /**
   * Returns the kernel matrix for transformation.
   *
   * @return a 2D array representing the kernel
   */
  float[][] getKernel();

  /**
   * Returns the padding size based on the kernel dimensions.
   *
   * @return the padding size
   */
  default int getPadding() {
    return getKernel().length / 2;
  }
}
