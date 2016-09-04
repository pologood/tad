package org.sapia.tad.value;

/**
 * Implement a mutabe {@link Value} (this class is not thread-safe).
 * 
 * 
 * @author yduchesne
 *
 */
public class MutableNumericValue implements Value {
  
  private double value;

  // --------------------------------------------------------------------------
  // Value interface

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
  // Misc instance methods

  /**
   * @param by a <code>double</code> value by which to increase this instance's own value.
   */
  public MutableNumericValue increase(double by) {
    value += by;
    return this;
  }
  
  /**
   * @param value a <code>double</code> value to assign to this instance.
   */
  public void set(double value) {
    this.value = value;
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
      return ((Value) o).get() == value;
    }
    return false;
  }
  
  @Override
  public String toString() {
    return Double.toString(value);
  }

}
