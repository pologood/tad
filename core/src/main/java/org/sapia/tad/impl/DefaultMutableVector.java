package org.sapia.tad.impl;

import org.sapia.tad.MutableVector;
import org.sapia.tad.util.Checks;
import org.sapia.tad.value.Value;
import org.sapia.tad.value.Values;

import java.util.List;

/**
 * Implements a {@link org.sapia.tad.Vector} with mutable state.
 *
 * @author yduchesne
 */
public class DefaultMutableVector extends DefaultVector implements MutableVector {

  /**
   * @param values the values to wrap.
   */
  public DefaultMutableVector(Value...values) {
    super(values);
  }

  /**
   * @param values an array of arbitrary objects for which to create an instance of this class.
   */
  public DefaultMutableVector(Object...values) {
    super(Values.with(values));
  }

  /**
   * @param values an list of arbitrary objects for which to create an instance of this class.
   */
  public DefaultMutableVector(List<Value> values) {
    super(values.toArray(new Value[values.size()]));
  }

  @Override
  public MutableVector set(int index, Value value) {
    Checks.bounds(index, values, "Invalid index: %s. Got %s values", index, values.length);
    values[index] = value;
    return this;
  }

  @Override
  public MutableVector set(int index, Object value) {
    return set(index, Values.of(value));
  }
}
