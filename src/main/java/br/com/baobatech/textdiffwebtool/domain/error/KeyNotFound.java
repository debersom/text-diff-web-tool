package br.com.baobatech.textdiffwebtool.domain.error;

/**
 * Used to notify there is no text added for any side for the key
 */
public class KeyNotFound extends RuntimeException {

  public KeyNotFound(String message) {
    super(message);
  }

  @Override
  public synchronized Throwable fillInStackTrace() {
    // Don't fill the stack trace for performance reason
    return this;
  }
}
