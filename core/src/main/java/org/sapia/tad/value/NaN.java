package org.sapia.tad.value;

/**
 * Implements the result of a division by 0.
 *
 * @author yduchesne
 */
public class NaN implements Value {

  private static final NaN INSTANCE = new NaN();

  @Override
  public boolean isNumeric() {
    return false;
  }

  @Override
  public double get() throws NonNumericValueException {
    throw new NonNumericValueException("NaN is not a numeric value");
  }

  @Override
  public Object getInternalValue() {
    return 0;
  }

  @Override
  public String toString() {
    return "NaN";
  }

  public static NaN getInstance() {
    return INSTANCE;
  }
}
