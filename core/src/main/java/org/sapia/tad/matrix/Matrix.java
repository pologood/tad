package org.sapia.tad.matrix;

import org.sapia.tad.Vector;
import org.sapia.tad.value.Value;

/**
 * @author yduchesne
 */
public interface Matrix {

  /**
   * @return this matrix' {@link Dimensions}.
   */
  public Dimensions getDimensions();

  /**
   * @param index a row index.
   * @return the {@link Vector} corresponding to the row at the given index.
   */
  public Vector getRow(int index);

  /**
   * @param index a column index.
   * @return the {@link Vector} corresponding to the column at the given index.
   */
  public Vector getColumn(int index);

  /**
   * Performs a matrix-scalar multiplication: each element in this matrix is multiplied
   * by the given scalar value: the results are returned in a new matrix.
   *
   * @param value a {@link Value} to multiple with this instance.
   * @return the new {@link Matrix} resulting from this operation.
   */
  public Matrix product(Value value);

  /**
   * Performs a matrix-vector multiplication.
   *
   * @param vector a {@link Vector} to multiply with this instance.
   * @return the new {@link Matrix} resulting from this operation.
   */
  public Vector product(Vector vector);

  /**
   * Performs a matrix-matrix multiplication.
   *
   * @param other another {@link Matrix) to multiply with this instance.
   * @return the new {@link Matrix} resulting from this operation.
   */
  public Matrix product(Matrix other);

  /**
   * Performs matrix addition: adds the elements in the given matrix to the elements of this instance, returning
   * the results in a new matrix. The matrix passed in must have the same dimensions as this instance - the
   * returned matrix will have the same dimensions as well.
   *
   * @param  other another {@link Matrix} to add to this instance}
   * @return the new {@link Matrix} resulting from this operation.
   */
  public Matrix sum(Matrix other);

  /**
   * Performs a row-by-row addition: values in the vector are subtracted from the corresponding values in
   * each row of this instance. The length of the vector passed in must be equal to this instance's number of columns.
   * The results are returned in a new matrix which will have the same dimensions as this instance.
   *
   * @param vector a {@link Vector} to add to this instance.
   * @return the difference between this instance and the given vector, as a new matrix.
   */
  //public Matrix sum(Vector vector);

  /**
   * @return a new {@link Matrix}, corresponding to this instance's transpose.
   */
  public Matrix transpose();

  /**
   * @param operation a {@link MatrixTransform} operation to apply to each row of this instance.
   * @return the new {@link Matrix} resulting from the row-wise operation.
   */
  public Matrix apply(MatrixTransform operation);

  public interface MatrixTransform {

    public Vector apply(int rowIndex, Vector input);
  }

}
