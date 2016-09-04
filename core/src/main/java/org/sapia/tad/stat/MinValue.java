package org.sapia.tad.stat;

import org.sapia.tad.value.MutableNumericValue;

/**
 * Overrides the {@link MutableNumericValue} by allowing the setting of a new value
 * only if it is lower than the current one.
 * 
 * @author yduchesne
 *
 */
public class MinValue extends MutableNumericValue {
  
  private boolean isSet;
  
  public MinValue() {
  }
  
  @Override
  public void set(double value) {
    if (!isSet || value < super.get()) {
      super.set(value);
      isSet = true;
    }
  }

}
