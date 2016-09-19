package org.sapia.tad.impl;

import org.sapia.tad.Vector;
import org.sapia.tad.Vectors;
import org.sapia.tad.util.Checks;
import org.sapia.tad.util.Objects;
import org.sapia.tad.value.NullValue;
import org.sapia.tad.value.Value;
import org.sapia.tad.value.Values;

import java.util.Iterator;
import java.util.List;

/**
 * Default imlementation of the {@link Vector} interface.
 * 
 * @author yduchesne
 *
 */
public class DefaultVector extends VectorImpl {
  
  protected Value[] values;
  
  /**
   * @param values the {@link Value}s to wrap.
   */
  public DefaultVector(Value...values) {
    this.values = values;
    for (int i = 0; i < values.length; i++) {
      if (values[i] == null) {
        values[i] = NullValue.getInstance();
      }
    }
  }

  /**
   * @param values an array of arbitrary objects for which to create an instance of this class.
   */
  public DefaultVector(Object...values) {
    this(Values.with(values));
  }

  /**
   * @param values a {@link List} of arbitrary objects for which to create an instance of this class.
   */
  public DefaultVector(List<Value> values) {
    this(values.toArray(new Value[values.size()]));
  }
  
  @Override
  public Value get(int index) throws IllegalArgumentException {
    Checks.bounds(index, values, "Invalid index: %s. Got %s value(s)", index, values.length);
    return values[index];
  }
  
  @Override
  public Vector subset(int...indices) throws IllegalArgumentException {
    Value[] values = new Value[indices.length];
    for (int i = 0; i < indices.length; i++) {
      int index = indices[i];
      Value value = get(index);
      values[i] = value;
    }
    return new DefaultVector(values);
  }

  @Override
  public int size() {
    return values.length;
  }
  
  @Override
  public Iterator<Value> iterator() {
    return new Iterator<Value>() {
      private int count;
      @Override
      public boolean hasNext() {
        return count < values.length;
      }
      
      @Override
      public Value next() {
        return values[count++];
      }
      
      @Override
      public void remove() {
      }
    };
  }
  
  @Override
  public Value[] toArray() {
    Value[] toReturn = new Value[values.length];
    System.arraycopy(values, 0, toReturn, 0, values.length);
    return toReturn;
  }

}
