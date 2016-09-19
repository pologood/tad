package org.sapia.tad;

import org.sapia.tad.value.Value;

/**
 * A {@link Vector} is a simple, immutable values-like data structure. Vectors are typically used
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
   * @param indices the indices whose corresponding values should be returned.
   * @return a new {@link Vector} holding the values at the given indices.
   * @throws IllegalArgumentException if anyone of the given indices is invalid.
   */
  public Vector subset(int...indices) throws IllegalArgumentException;
 
  /**
   * @return an values holding this instance's values.
   */
  public Value[] toArray();

  /**
   * @return the norm of this vector.
   */
  public double norm();

  /**
   * @return the sum of this instance's values.
   */
  public double sum();

  /**
   * Computes the inner product with another vector. This method will treat non-numeric values as 0.
   *
   * @param other another {@link Vector}
   * @return the scalar value corresponding to the inner product of this instance with the given vector.
   */
  public double innerProduct(Vector other);

  /**
   * Computes the product of this with another vector. This method will treat non-numeric values as 0.
   * <p>
   * DO NOT mistaken this operation with the inner product: this method will do a multiplication of each corresponding
   * values in this vector and the vector provided as input, returning these values in a new vector.
   * </p>
   *
   * @param other another {@link Vector}
   * @return a new {@link Vector}, with each column holding the product of the corresponding values
   * in this instance with the corresponding ones the given vector.
   */
  public Vector product(Vector other);

  /**
   * Performs the sum of this instance and a given vector. This method will treat non-numeric values as 0.
   *
   * @param other another {@link Vector}
   * @return a new {@link Vector}, with each column holding the sum of the corresponding values
   * in this instance, and the given vector.
   */
  public Vector sum(Vector other);

  /**
   * Calculates the difference between this instance and a given vector. This method will treat non-numeric values
   * as 0.
   *
   * @param other another {@link Vector}
   * @return a new {@link Vector}, with each column holding the difference of the corresponding values
   * in this instance, and the given vector.
   */
  public Vector difference(Vector other);

  /**
   * Computes the scalar product of the this vector and the given value.
   *
   * @param value the {@link Value} with which to compute the given product.
   * @return a new {@link Vector}, holding the results of the computation.
   */
  public Vector product(Value value);

  /**
   * Computes the element-wise sum of this vector and the given value.
   *
   * @param value the {@link Value} with which to compute the given sum.
   * @return a new {@link Vector}, holding the results of the computation.
   */
  public Vector sum(Value value);

  /**
   * Computes the element-wise difference of this vector and the given value.
   *
   * @param value the {@link Value} with which to compute the given difference.
   * @return a new {@link Vector}, holding the results of the computation.
   */
  public Vector difference(Value value);

  /**
   *  Raises this instance's values to the given power.
   *
   * @param power a power to raise this instance's values to.
   * @return a new {@link Vector}, holding the results of this operation.
   */
  public Vector pow(double power);

}
