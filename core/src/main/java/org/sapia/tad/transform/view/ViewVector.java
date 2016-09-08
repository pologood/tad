package org.sapia.tad.transform.view;

import org.sapia.tad.Vector;
import org.sapia.tad.Vectors;
import org.sapia.tad.impl.DefaultVector;
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
class ViewVector implements Vector {
  
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
  
  @Override
  public Iterator<Value> iterator() {
    return new Iterator<Value>() {
      private int index;
      @Override
      public boolean hasNext() {
        return index < columnIndices.length;
      }
      @Override
      public Value next() {
        if (index >= columnIndices.length) {
          throw new NoSuchElementException();
        }
        return get(index++);
      }
      
      @Override
      public void remove() {
      }
    };
  }
  
  @Override
  public Vector subset(int... indices) throws IllegalArgumentException {
    Value[] values = new Value[indices.length];
    for (int i = 0; i < indices.length; i++) {
      values[i] = get(indices[i]);
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
  public Value[] toArray() {
    Value[] values = new Value[columnIndices.length];
    for (int i = 0; i < columnIndices.length; i++) {
      values[i] = get(i);
    }
    return values;
  }

}
