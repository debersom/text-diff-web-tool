package br.com.baobatech.textdiffwebtool.domain.error;

/**
 * Used to notify when one text was not added for a side for a specific comparison key.
 */
public class MissingText extends RuntimeException {

  public MissingText(String message) {
    super(message);
  }

  @Override
  public synchronized Throwable fillInStackTrace() {
    // Don't fill the stack trace for performance reason
    return this;
  }
}
