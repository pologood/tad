package org.sapia.tad.math;

import org.sapia.tad.value.MutableNumericValue;
import org.sapia.tad.value.NumericValue;
import org.sapia.tad.value.Value;

public class SumValue extends MutableNumericValue {
  
  /**
   * @param value adds the given value to this instance.
   * @return this instance.
   */
  public SumValue add(Value value) {
    super.set(get() + NumericValue.doubleOrZero(value).get());
    return this;
  }

}
