package org.sapia.tad.transform.range;

import org.sapia.tad.Datatype;
import org.sapia.tad.format.Format;
import org.sapia.tad.format.SelfFormattable;
import org.sapia.tad.util.Checks;
import org.sapia.tad.value.NumericValue;
import org.sapia.tad.value.Value;

import java.util.Comparator;

/**
 * Models a range, which is composed of a minimum bound and a maximum bound. 
 * The minimum is inclusive, as the maximum is exclusive. Therefore, a value v will
 * be considered "within" the range if it's greater than or equal to the minimum, or
 * lower than the maximum.
 *  
 * @author yduchesne
 *
 */
public class Range implements Value, SelfFormattable {
  
  private Comparator<Value> comp;
  private Value min, max;
  
  /**
   * @param comp the {@link Comparator} to use to compare the min and max values.
   * @param min a minimum value.
   * @param max a maximum value.
   */
  public Range(Comparator<Value> comp, Value min, Value max) {
    this.comp = comp;
    int c = compare(min, max);
    Checks.isTrue(c <= 0, "Min must be lower than or equal to max (got: min = %s, max = %s)", min, max);
    this.min  = min;
    this.max  = max;
  }

  @Override
  public double get() {
    return NumericValue.sum(min, max).get() / 2;
  }

  @Override
  public boolean isNumeric() {
    return min.isNumeric() && max.isNumeric();
  }

  @Override
  public Object getInternalValue() {
    return get();
  }

  @Override
  public String format(Datatype type, Format format) {
    return "[" + format.formatValue(type, min) 
        + " - " + format.formatValue(type, max) + "]";
  }
  
  @Override
  public String toString() {
    return "[" + min  + " - " + max + "]";
  }
  
  private int compare(Value left, Value right) {
    if (left == null) {
      return 1;
    } else if (right == null) {
      return -1;
    } 
    return comp.compare(left, right);
  }
}
