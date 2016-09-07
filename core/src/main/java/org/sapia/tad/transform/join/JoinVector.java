package org.sapia.tad.transform.join;

import org.sapia.tad.Vector;
import org.sapia.tad.Vectors;
import org.sapia.tad.impl.DefaultVector;
import org.sapia.tad.transform.join.VectorTable.VectorType;
import org.sapia.tad.value.Value;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Implements the {@link Vector} interface over two other vectors.
 * 
 * @author yduchesne
 *
 */
class JoinVector implements Vector {

  private VectorTable  table;
  private Vector left, right;
  private int totalSize;
  
  JoinVector(VectorTable table, Vector left, Vector right) {
    this.table = table;
    this.left  = left;
    this.right = right;
    totalSize = left.size() + right.size();
  }
  
  @Override
  public Value get(int index) throws IllegalArgumentException {
    if (table.resolveVectorType(index) == VectorType.LEFT) {
      return left.get(table.resolveVectorIndex(index));
    } else {
      return right.get(table.resolveVectorIndex(index));
    }
  }
  
  @Override
  public Iterator<Value> iterator() {
    return new Iterator<Value>() {
      private int index;
      @Override
      public boolean hasNext() {
        return index < totalSize;
      }
      
      @Override
      public Value next() {
        if (index >= totalSize) {
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
  public int size() {
    return totalSize;
  }
  
  @Override
  public Vector subset(int... indices) throws IllegalArgumentException {
    Value[] values = new Value[indices.length];
    for (int i = 0; i < indices.length; i++) {
      int index = indices[i];
      Value value = get(index);
      values[i] = value;
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
    Value[] values = new Value[totalSize];
    for (int i = 0; i < totalSize; i++) {
      Value value = get(i);
      values[i] = value;
    }
    return values;
  }
}
