package org.sapia.tad.impl;

import org.sapia.tad.RowSet;
import org.sapia.tad.Vector;

import java.util.Iterator;
import java.util.NoSuchElementException;

public final class NullRowset implements RowSet {
  
  public java.util.Iterator<Vector> iterator() {
    return new Iterator<Vector>() {
      
      @Override
      public void remove() {
      }
      
      @Override
      public Vector next() {
        throw new NoSuchElementException();
      }
      
      @Override
      public boolean hasNext() {
        return false;
      }
    };
  }
  
  @Override
  public Vector get(int index) throws IllegalArgumentException {
    throw new IllegalArgumentException("Rowset is empty");
  }
  
  @Override
  public int size() {
    return 0;
  }

}
