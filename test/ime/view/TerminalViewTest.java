package ime.view;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the TerminalView class.
 * These tests verify the behavior of methods in the TerminalView class for handling
 * message and error display, including cases where I/O exceptions may occur.
 */
public class TerminalViewTest {

  /**
   * Tests the {@link TerminalView#displayMessage(String)} method.
   * Verifies that the correct message is displayed by appending it to the output
   * with a newline character.
   */
  @Test
  public void testDisplayMessage() {
    StringBuilder output = new StringBuilder();
    TerminalView view = new TerminalView(output);
    String message = "Hello, World!";
    view.displayMessage(message);
    assertEquals(message + "\n", output.toString());
  }

  /**
   * Tests the {@link TerminalView#displayError(String)} method.
   * Verifies that an error message is displayed correctly, prefixed with "Error: ".
   */
  @Test
  public void testDisplayError() {
    StringBuilder output = new StringBuilder();
    TerminalView view = new TerminalView(output);
    String errorMessage = "File not found";
    view.displayError(errorMessage);
    assertEquals("Error: " + errorMessage + "\n", output.toString());
  }

  /**
   * Tests the {@link TerminalView#displayError(String)} method when an IOException occurs.
   * This test verifies that the method handles the IOException by not outputting anything
   * when the append operation fails.
   *
   * @throws IOException Simulated IOException
   */
  @Test(expected = IOException.class)
  public void testDisplayErrorIOException() throws IOException {
    Appendable mockOutput = new Appendable() {
      @Override
      public Appendable append(CharSequence csq) throws IOException {
        throw new IOException("Simulated IO exception");
      }

      @Override
      public Appendable append(CharSequence csq, int start, int end) throws IOException {
        throw new IOException("Simulated IO exception");
      }

      @Override
      public Appendable append(char c) throws IOException {
        throw new IOException("Simulated IO exception");
      }
    };
    TerminalView view = new TerminalView(mockOutput);
    view.displayError("Test error message");
  }

  /**
   * Tests the {@link TerminalView#displayMessage(String)} method when an IOException occurs.
   * This test verifies that the method handles the IOException by not outputting anything
   * when the append operation fails.
   *
   * @throws IOException Simulated IOException
   */
  @Test(expected = IOException.class)
  public void testDisplayMessageIOException() throws IOException {
    Appendable mockOutput = new Appendable() {
      @Override
      public Appendable append(CharSequence csq) throws IOException {
        throw new IOException("Simulated IO exception");
      }

      @Override
      public Appendable append(CharSequence csq, int start, int end) throws IOException {
        throw new IOException("Simulated IO exception");
      }

      @Override
      public Appendable append(char c) throws IOException {
        throw new IOException("Simulated IO exception");
      }
    };
    TerminalView view = new TerminalView(mockOutput);
    view.displayMessage("Test message");
  }
}