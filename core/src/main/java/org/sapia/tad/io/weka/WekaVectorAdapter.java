package org.sapia.tad.io.weka;

import org.sapia.tad.Vector;
import org.sapia.tad.impl.DefaultVector;
import org.sapia.tad.util.Checks;
import org.sapia.tad.util.Objects;
import org.sapia.tad.value.*;
import weka.core.Attribute;
import weka.core.Instance;

import java.util.Date;
import java.util.Iterator;

/**
 * Implements a {@link Vector} over the Weka {@link Instance} interface.
 * 
 * @author yduchesne
 */
public class WekaVectorAdapter implements Vector {
  
  private Instance instance;
  private int      hashCode  = -1;
  
  /**
   * @param instance the {@link Instance} to wrap.
   */
  public WekaVectorAdapter(Instance instance) {
    this.instance = instance;
  }
  
  @Override
  public Value get(int index) throws IllegalArgumentException {
    return doGet(index);
  }
  
  @Override
  public int size() {
    return instance.numAttributes();
  }
  
  @Override
  public Vector subset(int... indices) throws IllegalArgumentException {
    Value[] values = new Value[indices.length];
    for (int i = 0; i < indices.length; i++) {
      values[i] = doGet(indices[i]); 
    }
    return new DefaultVector(values);
  }
  
  private Value doGet(int index) {
    Checks.isFalse(index >= instance.numAttributes(), "Invalid index: %s. Got %s array", index, instance.numAttributes());
    Attribute attr = instance.attribute(index);
    Value value;
    if (instance.isMissing(attr.index())) {
      value = NullValue.getInstance();
    } else if (attr.isNominal()) {
      int realIndex = (int) instance.value(index);
      value = new StringValue(attr.value(realIndex));
    } else {
      value = new NumericValue(instance.value(attr.index()));
    }
    if (!NullValue.isNull(value) && attr.type() == Attribute.DATE) {
      value = new DateValue(new Date((long) value.get()));
    } 
    return value;
  }
  
  @Override
  public Iterator<Value> iterator() {
    return new Iterator<Value>() {
      private int index = 0;
      @Override
      public boolean hasNext() {
        return index < instance.numAttributes();
      }
      
      @Override
      public Value next() {
        return doGet(index++);
      }
      
      @Override
      public void remove() {
      }
    };
  }

  @Override
  public double product(Vector other) {
    double product = 0;
    for (int i = 0; i < size(); i++) {
      Value thisValue = get(i);
      Value otherValue = other.get(i);
      if (thisValue.isNumeric() && otherValue.isNumeric()) {
        product += thisValue.get() * otherValue.get();
      }
    }
    return product;
  }

  @Override
  public Vector sum(Vector other) {
    Checks.isTrue(size() == other.size(), "Vectors do not have same length (%s vs %s)", size(), other.size());
    Value[] sum = new Value[size()];
    for (int i = 0; i < size(); i++) {
      Value thisValue = get(i);
      Value otherValue = other.get(i);
      sum[i] = NumericValue.sum(thisValue, otherValue);
    }
    return new DefaultVector(sum);
  }

  @Override
  public Value[] toArray() {
    Value[] toReturn = new Value[instance.numAttributes()];
    for (int i = 0; i < toReturn.length; i++) {
      toReturn[i] = get(i);
    }
    return toReturn;
  }
  
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Vector) {
      Vector other = (Vector) obj;
      if (instance.numAttributes() != other.size()) {
        return false;
      }
      for (int i = 0; i < instance.numAttributes(); i++) {
        Attribute attr = instance.attribute(i);
        if (!Objects.safeEquals(doGet(attr.index()), other.get(i))) {
          return false;
        }
      }
      return true;
    }
    return false;
  }
  
  @Override
  public int hashCode() {
    if (hashCode < 0) {
      hashCode = Objects.safeHashCode(iterator());
    }
    return hashCode;
  }
  
  @Override
  public String toString() {
    StringBuilder s = new StringBuilder("[");
    for (int i = 0; i < instance.numAttributes(); i++) {
      if (i > 0) {
        s.append(",");
      }
      s.append(doGet(instance.attribute(i).index()));
    }
    return s.append("]").toString();
  }
}
