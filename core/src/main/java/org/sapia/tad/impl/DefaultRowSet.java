package org.sapia.tad.impl;

import org.sapia.tad.RowSet;
import org.sapia.tad.Vector;
import org.sapia.tad.util.Checks;

import java.util.Iterator;
import java.util.List;

/**
 * An implementation of the {@link RowSet} interfaces (an instance of this class
 * wraps a {@link List} of {@link Vector}s).
 * 
 * @author yduchesne
 *
 */
public class DefaultRowSet implements RowSet {
  
  private List<Vector> rows;
  
  public DefaultRowSet(List<Vector> rows) {
    this.rows = rows;
  }
  
  @Override
  public Vector get(int index) throws IllegalArgumentException {
    Checks.bounds(index, rows, "Invalid index: %s. Got %s rows", index, rows.size());
    return rows.get(index);
  }
  
  @Override
  public int size() {
    return rows.size();
  }
  
  @Override
  public Iterator<Vector> iterator() {
    return new ImmutableIterator<>(rows.iterator());
  }
}
