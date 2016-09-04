package org.sapia.tad.value;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

/**
 * Wraps a string.
 *
 * @author yduchesne.
 */
@RequiredArgsConstructor
@EqualsAndHashCode(of = "value")
public class StringValue implements Value, Comparable<StringValue> {

  private final String value;

  @Override
  public double get() throws NonNumericValueException {
    throw new NonNumericValueException("Value is not numeric: " + value);
  }

  @Override
  public boolean isNumeric() {
    return false;
  }

  @Override
  public Object getInternalValue() {
    return value;
  }

  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return value;
  }

  // --------------------------------------------------------------------------
  // Comparable interface

  @Override
  public int compareTo(StringValue o) {
    return value.compareTo(o.value);
  }

  // --------------------------------------------------------------------------
  // Static methods

  public static StringValue of(Number number) {
    return new StringValue(number.toString());
  }

  public static StringValue of(String value) {
    return new StringValue(value);
  }
}
