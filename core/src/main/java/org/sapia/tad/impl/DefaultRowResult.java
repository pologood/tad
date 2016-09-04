package org.sapia.tad.impl;

import org.sapia.tad.ColumnSet;
import org.sapia.tad.RowResult;
import org.sapia.tad.Vector;
import org.sapia.tad.value.Value;

/**
 * Implements the {@link RowResult} interface by wrapping a {@link ColumnSet} and
 * a {@link Vector}.
 * <p>
 * An instance of this class is not meant to be shared among multiple threads: its state
 * is mutable by allowing to set the {@link Vector} instance that it wraps.
 * 
 * @author yduchesne
 *
 */
public class DefaultRowResult implements RowResult {
  
  private ColumnSet columns;
  private Vector    vector;
  
  /**
   * @param columns a {@link ColumnSet}.
   */
  public DefaultRowResult(ColumnSet columns) {
    this.columns = columns;
  }
  
  /**
   * @param vector a {@link Vector}.
   */
  public void setVector(Vector vector) {
    this.vector = vector;
  }
  
  /**
   * @return this instance's {@link Vector}.
   */
  public Vector getVector() {
    return vector;
  }
  
  @Override
  public Value get(int index) throws IllegalArgumentException {
    return vector.get(index);
  }
  
  @Override
  public Value get(String name) throws IllegalArgumentException {
    return vector.get(columns.get(name).getIndex());
  }
  
}
