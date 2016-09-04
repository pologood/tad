package org.sapia.tad.value;

import org.sapia.tad.util.Checks;

/**
 * A utility class to work with <code>null</code>s.
 * 
 * @author yduchesne
 *
 */
public final class NullValue implements Value, Comparable<Value> {
  
  private static final NullValue INSTANCE = new NullValue();

  private NullValue() {
  }
  
  @Override
  public double get() {
    return 0;
  }

  @Override
  public boolean isNumeric() {
    return true;
  }

  @Override
  public Object getInternalValue() {
    return null;
  }

  // --------------------------------------------------------------------------
  // Static methods

  /**
   * @param obj an object to test for nullity.
   * @return <code>true</code> if the given object is considered <code>null</code>.
   */
  public static boolean isNull(Object obj) {
    return obj == null || obj instanceof NullValue;
  }

  /**
   * @param value a {@link Value} to test for nullity.
   * @return <code>true</code> if the given object is considered <code>null</code>.
   */
  public static boolean isNull(Value value) {
    Checks.isFalse(value == null, "Passed in value cannot be null");
    return value instanceof NullValue;
  }

  /**
   * @param obj an object to test for nullity.
   * @return <code>true</code> if the given object is not considered <code>null</code>.
   */
  public static boolean isNotNull(Object obj) {
    return !isNull(obj);
  }
  
  /**
   * @return a {@link NullValue}.
   */
  public static NullValue getInstance() {
    return INSTANCE;
  }

  // --------------------------------------------------------------------------
  // Comparable interface

  @Override
  public int compareTo(Value o) {
    if (NullValue.isNull(o)) {
      return 0;
    } else {
      return -1;
    }
  }

  // --------------------------------------------------------------------------
  // Object overrides

  @Override
  public boolean equals(Object obj) {
    return obj == null || obj instanceof NullValue;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  @Override
  public String toString() {
    return "?";
  }
}
