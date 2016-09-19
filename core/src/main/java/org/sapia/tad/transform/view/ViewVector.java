package org.sapia.tad.transform.view;

import org.sapia.tad.Vector;
import org.sapia.tad.Vectors;
import org.sapia.tad.impl.DefaultVector;
import org.sapia.tad.impl.VectorImpl;
import org.sapia.tad.value.Value;

import javax.swing.text.View;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An instance of this class wraps another vector, only "showing" values
 * of that other vector that correspond to the columns selected as part of the
 * view.
 * 
 * @see View
 * @see ViewDataset
 * 
 * @author yduchesne
 *
 */
class ViewVector extends VectorImpl {
  
  private int[]  columnIndices;
  private Vector delegate;
  
  ViewVector(int[] columnIndices, Vector delegate) {
    this.columnIndices = columnIndices;
    this.delegate = delegate;
  }
  
  @Override
  public Value get(int index) throws IllegalArgumentException {
    int realIndex = columnIndices[index];
    return delegate.get(realIndex);
  }
  
  @Override
  public int size() {
    return columnIndices.length;
  }

}
