package org.sapia.tad;

import org.sapia.tad.value.Value;

/**
 * Specifies the behavior for mutable vectors, whose values can be modified.
 *
 * @author yduchesne
 */
public interface MutableVector extends Vector {

  /**
   * @param index the index at which the given value should be set.
   * @param value an arbitrary {@link Value}, which is internally converted to a {@link Value} instance.
   * @return this instance.
   */
  public MutableVector set(int index, Value value);

  /**
   * @param index the index at which the given value should be set.
   * @param value an arbitrary object, which is internally converted to a {@link Value} instance.
   * @return this instance.
   */
  public MutableVector set(int index, Object value);
}
