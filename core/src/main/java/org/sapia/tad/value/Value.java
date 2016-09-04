package org.sapia.tad.value;

/**
 * Specifies the behavior for array held in {@link org.sapia.tad.Vector}s.
 *
 * @author yduchesne
 */
public interface Value {

  /**
   * @return this instance's numeric value (if indeed it holds such a value).
   * @throws NonNumericValueException if this instance does not correspond to a numeric value.
   * @see #isNumeric()
   */
  public double get() throws NonNumericValueException;

  /**
   * @return <code>true</code> if this instance in fact consists of numeric data,
   * or is able to convert itself to numeric data.
   */
  public boolean isNumeric();

  /**
   * This method returns the object wrapped by this instance: typically, applications should not use this method. It
   * is generally meant for internal framework usage.
   *
   * @return the {@link Object} instance corresponding to the value that this instance wraps.
   */
  public Object getInternalValue();

}
