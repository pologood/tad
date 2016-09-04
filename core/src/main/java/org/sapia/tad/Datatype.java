package org.sapia.tad;

import org.sapia.tad.type.DateDatatypeStrategy;
import org.sapia.tad.type.GeometryDatatypeStrategy;
import org.sapia.tad.type.NumericDatatypeStrategy;
import org.sapia.tad.type.StringDatatypeStrategy;
import org.sapia.tad.value.Value;

import java.util.Comparator;

public enum Datatype {

  STRING(new StringDatatypeStrategy()),
  NUMERIC(new NumericDatatypeStrategy()),
  DATE(new DateDatatypeStrategy()),
  GEOMETRY(new GeometryDatatypeStrategy());
  
  private DatatypeStrategy strategy;
  private Comparator<Value> comparator;
  
  private Datatype(final DatatypeStrategy strategy) {
    this.strategy   = strategy;
    this.comparator = new Comparator<Value>() {
      @Override
      public int compare(Value v1, Value v2) {
        return strategy.compareTo(v1, v2);
      }
    };
  }
  
  /**
   * @return this instance's {@link DateDatatypeStrategy}.
   */
  public DatatypeStrategy strategy() {
    return strategy;
  }
  
  /**
   * @see DatatypeStrategy#compareTo(Object, Object)
   * @return this instance's comparator.
   */
  public Comparator<Value> comparator() {
    return comparator;
  }
  
}
