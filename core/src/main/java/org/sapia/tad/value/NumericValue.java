package org.sapia.tad.value;

/**
 * Implements the {@link Value} interface over the <code>double</code> type.
 * 
 * @author yduchesne
 *
 */
public class NumericValue implements Value, Comparable<NumericValue> {

  private static final NumericValue ZERO = new NumericValue(0);

  private double value;
  
  /**
   * @param number a {@link Number}.
   */
  public NumericValue(Number number) {
    this(number == null ? 0 : number.doubleValue());
  }
  
  /**
   * @param value a <code>double</code> value.
   */
  public NumericValue(double value) {
    this.value = value;
  }
  
  @Override
  public double get() {
    return value;
  }

  @Override
  public boolean isNumeric() {
    return true;
  }

  @Override
  public Object getInternalValue() {
    return value;
  }

  // --------------------------------------------------------------------------
  // Comparable interface

  @Override
  public int compareTo(NumericValue o) {
    if (value < o.value) {
      return -1;
    } else if (value > o.value) {
      return 1;
    } else {
      return 0;
    }
  }

  // --------------------------------------------------------------------------
  // Static methods

  public static NumericValue of(double value) {
    return new NumericValue(value);
  }

  public static NumericValue of(Number value) {
    return new NumericValue(value);
  }

  public static NumericValue zero() {
    return ZERO;
  }

  /**
   * @param value a {@link Value}.
   * @return the passed in {@link Value} if it is numeric, or a {@link Value} corresponding to 0
   * otherwise.
   */
  public static Value doubleOrZero(Value value) {
    return value.isNumeric() ? value : ZERO;
  }

  /**
   * @param v1 a {@link Value}
   * @param v2 another {@link Value} to sum with the previous one.
   * @return a new {@link Value}, consisting of the sum of the array passed in.
   */
  public static Value sum(Value v1, Value v2) {
    if (v1.isNumeric() && v2.isNumeric()) {
      return new NumericValue(v1.get() + v2.get());
    } else if (v1.isNumeric()) {
      return v1;
    } else if (v2.isNumeric()) {
      return v2;
    }
    return ZERO;
  }

  /**
   * @param v1 a {@link Value}
   * @param v2 another {@link Value} to sum with the previous one.
   * @return a new {@link Value}, consisting of the sum of the array passed in.
   */
  public static Value difference(Value v1, Value v2) {
    if (v1.isNumeric() && v2.isNumeric()) {
      return new NumericValue(v1.get() - v2.get());
    } else if (v1.isNumeric()) {
      return v1;
    } else if (v2.isNumeric()) {
      // assuming v1 is 0
      return NumericValue.of(-v2.get());
    }
    return ZERO;
  }

  /**
   * @param v1 a {@link Value}
   * @param v2 another {@link Value} to sum with the previous one.
   * @return a new {@link Value}, consisting of product of the array passed in.
   */
  public static Value product(Value v1, Value v2) {
    if (v1.isNumeric() && v2.isNumeric()) {
      return new NumericValue(v1.get() * v2.get());
    }
    return ZERO;
  }

  // --------------------------------------------------------------------------
  // Object overrides

  @Override
  public int hashCode() {
    return (int) value;
  }
  
  @Override
  public boolean equals(Object o) {
    if (o instanceof Value) {
      Value other = (Value) o;
      if (other.isNumeric()) {
        return other.get() == value;
      }
      return false;
    }
    return false;
  }

  @Override
  public String toString() {
    return Double.toString(value);
  }
}
