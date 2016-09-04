package org.sapia.tad.value;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.util.Date;

/**
 * @author yduchesne.
 */
@RequiredArgsConstructor
@EqualsAndHashCode(of = "value")
public class DateValue implements Value, Comparable<DateValue> {

  private final Date value;

  // --------------------------------------------------------------------------
  // Value interface

  @Override
  public double get() throws NonNumericValueException {
    throw new NonNumericValueException("Value is a date: " + value);
  }

  @Override
  public boolean isNumeric() {
    return false;
  }

  @Override
  public Object getInternalValue() {
    return value;
  }

  // --------------------------------------------------------------------------
  // Comparable interface

  @Override
  public int compareTo(DateValue o) {
    return value.compareTo(o.value);
  }

  // --------------------------------------------------------------------------
  // Misc instance methods

  public Date getValue() {
    return value;
  }

  // --------------------------------------------------------------------------
  // Object overrides

  @Override
  public String toString() {
    return value.toString();
  }
}
