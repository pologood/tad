package org.sapia.tad.transform.join;

import org.sapia.tad.Vector;
import org.sapia.tad.Vectors;
import org.sapia.tad.impl.DefaultVector;
import org.sapia.tad.impl.VectorImpl;
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
class JoinVector extends VectorImpl {

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
  public int size() {
    return totalSize;
  }

}
