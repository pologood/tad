package org.sapia.tad.impl;

import org.sapia.tad.RowSet;
import org.sapia.tad.Vector;
import org.sapia.tad.util.Checks;

import java.util.Iterator;

/**
 * Implements the {@link RowSet} interface over an array of {@link Vector}s.
 *
 * @author yduchesne
 */
public class ArrayRowset implements RowSet {

  private Vector[] rows;

  public ArrayRowset(Vector[] rows) {
    this.rows = rows;
  }

  @Override
  public Vector get(int index) throws IllegalArgumentException {
    Checks.bounds(index, rows, "Invalid index: %s. Got %s rows", index, rows.length);
    return rows[index];
  }

  @Override
  public int size() {
    return rows.length;
  }

  @Override
  public Iterator<Vector> iterator() {
    return new Iterator<Vector>() {
      int count = 0;
      @Override
      public boolean hasNext() {
        return count < rows.length;
      }

      @Override
      public Vector next() {
        return get(count++);
      }
    };
  }
}
