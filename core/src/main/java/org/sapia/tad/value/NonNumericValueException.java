package org.sapia.tad.value;

/**
 * Thrown when a numeric conversion is attempted on a {@link Value} that is not numeric.
 */
public class NonNumericValueException extends RuntimeException {

  NonNumericValueException(String msg, Object...args) {
    super(String.format(msg, args));
  }
}
