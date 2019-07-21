package br.com.baobatech.textdiffwebtool.domain.error;

/**
 * Keeps the information about the unexpected error occurred on comparison.
 */
public class ComparisonError extends RuntimeException {

  public ComparisonError(String message, Throwable cause) {
    super(message, cause);
  }
}
