package org.sapia.tad.type;

import org.sapia.tad.DatatypeStrategy;
import org.sapia.tad.value.Value;

public class GeometryDatatypeStrategy implements DatatypeStrategy {
  
  @Override
  public boolean isAssignableFrom(Object value) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isType(Value value) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Value getValueFor(Object obj) {
    throw new UnsupportedOperationException();
  }

  @Override
  public int compareTo(Value value, Value operand) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Object add(Object currentValue, Object toAdd) {
    throw new UnsupportedOperationException();
  }
}
