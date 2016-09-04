package org.sapia.tad;

import org.sapia.tad.value.Value;

/**
 * A {@link Vector} is a simple, immutable array-like data structure. Vectors are typically used
 * as rows in {@link Dataset}s or {@link RowSet}s.
 * 
 * @author yduchesne
 *
 */
public interface Vector extends Iterable<Value> {

  /**
   * @return this instance's number of items.
   */
  public int size();
  
  /**
   * @param index the index of the item to return.
   * @return the {@link Object} at the given index.
   * @throws IllegalArgumentException if the index is invalid.
   */
  public Value get(int index) throws IllegalArgumentException;
  
  /**
   * @param indices the indices whose corresponding array should be returned.
   * @return a new {@link Vector} holding the array at the given indices.
   * @throws IllegalArgumentException if anyone of the given indices is invalid.
   */
  public Vector subset(int...indices) throws IllegalArgumentException;
 
  /**
   * @return an array holding this instance's array.
   */
  public Value[] toArray();

  /**
   * Computes the inner product with another vector. This method will treat non-numeric values as 0.
   *
   * @param other another {@link Vector}
   * @return the scalar value corresponding to the inner product of this instance with the given vector.
   */
  public double product(Vector other);

  /**
   * Performs the sum of this instance and a given vector. This method will treat non-numeric values as 0.
   *
   * @param other another {@link Vector}
   * @return a new {@link Vector}, with each column holding the sum of the corresponding values
   * in this instance, and the given vector.
   */
  public Vector sum(Vector other);
}
