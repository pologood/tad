package org.sapia.tad.impl;

import org.sapia.tad.Vector;
import org.sapia.tad.Vectors;
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
public class DefaultVector implements Vector {
  
  private Value[] values;
  
  /**
   * @param values the values to wrap.
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
   * @param values an values of arbitrary objects for which to create an instance of this class.
   */
  public DefaultVector(Object...values) {
    this(Values.with(values));
  }

  public DefaultVector(List<Value> values) {
    this(values.toArray(new Value[values.size()]));
  }
  
  @Override
  public Value get(int index) throws IllegalArgumentException {
    if (index < 0 || index >= values.length) {
      throw new IllegalArgumentException(String.format("Invalid index: %s. Got %s values", index, values.length));
    }
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
  public double product(Vector other) {
    return Vectors.product(this, other);
  }

  @Override
  public Vector sum(Vector other) {
    return Vectors.sum(this, other);
  }

  @Override
  public double norm() {
    return Vectors.norm(this);
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
  
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Vector) {
      Vector other = (Vector) obj;
      if (values.length != other.size()) {
        return false;
      }
      for (int i = 0; i < values.length; i++) {
        if (!Objects.safeEquals(values[i], other.get(i))) {
          return false;
        }
      }
      return true;
    }
    return false;
  }
  
  @Override
  public int hashCode() {
    return Objects.safeHashCode(values);
  }
  
  @Override
  public String toString() {
    StringBuilder s = new StringBuilder("[");
    for (int i = 0; i < values.length; i++) {
      if (i > 0) {
        s.append(",");
      }
      s.append(values[i]);
    }
    return s.append("]").toString();
  }
}
