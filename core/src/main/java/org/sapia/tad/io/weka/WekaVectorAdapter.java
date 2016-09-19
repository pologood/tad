package org.sapia.tad.io.weka;

import org.sapia.tad.Vector;
import org.sapia.tad.Vectors;
import org.sapia.tad.impl.DefaultVector;
import org.sapia.tad.impl.VectorImpl;
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
public class WekaVectorAdapter extends VectorImpl {
  
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
  
  private Value doGet(int index) {
    Checks.isFalse(index >= instance.numAttributes(), "Invalid index: %s. Got %s values", index, instance.numAttributes());
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
}
