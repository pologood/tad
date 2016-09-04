package org.sapia.tad.stat;

import org.sapia.tad.value.MutableNumericValue;
import org.sapia.tad.value.Value;

/**
 * Implements mean calculation.
 * 
 * @author yduchesne
 *
 */
public class MeanValue extends MutableNumericValue {

  private int count;
  
  @Override
  public MutableNumericValue increase(double by) {
    super.increase(by);
    count = count + 1;
    return this;
  }
  
  @Override
  public double get() {
    return count == 0 ? 0 : super.get() / count;
  }
  
  @Override
  public int hashCode() {
    return (int) get();
  }
  
  @Override
  public boolean equals(Object o) {
    if (o instanceof Value) {
      return ((Value) o).get() == get();
    }
    return false;
  }
  
  @Override
  public String toString() {
    return Double.toString(get());
  }
}
