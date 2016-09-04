package org.sapia.tad.stat;

import org.sapia.tad.value.MutableNumericValue;

/**
 * Overrides the {@link MutableNumericValue} by allowing the setting of a new value
 * only if it is greater than the current one.
 * 
 * @author yduchesne
 *
 */
public class MaxValue extends MutableNumericValue {

  @Override
  public void set(double value) {
    if (value > super.get()) {
      super.set(value);
    }
  }
}
