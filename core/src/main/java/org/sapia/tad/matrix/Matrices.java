package org.sapia.tad.matrix;

import org.sapia.tad.*;
import org.sapia.tad.Vector;
import org.sapia.tad.concurrent.TaskExecutor;
import org.sapia.tad.help.Doc;
import org.sapia.tad.impl.ArrayRowset;
import org.sapia.tad.impl.DatasetRowSetAdapter;
import org.sapia.tad.impl.DefaultRowSet;
import org.sapia.tad.matrix.impl.DefaultMatrix;
import org.sapia.tad.matrix.merge.MergedMatrix;
import org.sapia.tad.util.Checks;
import org.sapia.tad.util.Data;
import org.sapia.tad.value.NumericValue;
import org.sapia.tad.value.Value;

import java.util.*;

/**
 * Groups non-standard matrix operations and transformations.
 *
 * @author yduchesne
 */
@Doc("Groups matrix-related operations")
public class Matrices {

  private TadContext context;

  public Matrices(TadContext context) {
    this.context = context;
  }

  /**
   * @param dataset the {@link Dataset} to create a {@link Matrix} from.
   * @return a new {@link Matrix}, wrapping the given {@link Dataset}.
   */
  @Doc("Creates a new matrix from the given dataset")
  public Matrix withDataset(Dataset dataset) {
    return new DefaultMatrix(context, dataset.getColumnSet().size(), new DatasetRowSetAdapter(dataset));
  }

  /**
   * @param row a {@link Vector}, consisting of the unique row of the matrix to return.
   * @return a new {@link Matrix}, holding the given vector as single row.
   */
  @Doc("Creates a new matrix with the given vector as a single row")
  public Matrix withRow(Vector row) {
    return new DefaultMatrix(context, row.size(), new DefaultRowSet(Data.list(row)));
  }

  /**
   * @param col a {@link Vector}, consisting of the unique column of the matrix to return.
   * @return a new {@link Matrix}, holding the given vector as single row.
   */
  @Doc("Creates a new matrix with the given vector as a single column")
  public Matrix withColumn(Vector col) {
    List<Vector> rows = new ArrayList<>();
    for (int i = 0; i < col.size(); i++) {
      rows.add(Vectors.with(col.get(i)));
    }
    return new DefaultMatrix(context, 1, new DefaultRowSet(rows));
  }

  /**
   * @param matrices one or more {@link Matrix} instance(s) to merge.
   * @return a new {@Matrix}, composed of the matrices passed in.
   */
  @Doc("Merges the provided matrices into a single one (provided matrices must have the same number of rows)")
  public Matrix merge(Matrix...matrices) {
    return merge(Arrays.asList(matrices));
  }

  /**
   * @param matrices one or more {@link Matrix} instance(s) to merge.
   * @return a new {@Matrix}, composed of the matrices passed in.
   */
  @Doc("Merges the provided matrices into a single one (provided matrices must have the same number of rows)")
  public Matrix merge(List<Matrix> matrices) {
    Checks.isTrue(matrices.size() > 0, "At least one array must be provided as input");
    if (matrices.size() == 1) {
      return matrices.get(0);
    }
    return new MergedMatrix(context, matrices);
  }

  /**
   * Performs a row-wise sum of the given matrix and vector: an element-wise sum of each row of the given
   * matrix and the specified vector will be performed. The results will be returned in a new matrix,
   * having the same dimensions as the given one.
   *
   * @param matrix a {@link Matrix}.
   * @param vector a {@link Vector}.
   * @return the matrix resulting from the sum of the given matrix and vector.
   */
  @Doc("Performs an element-size matrix-vector sum and returns the results in a new matrix " +
       "(the number of columns in the specified matrix must be equal to the vector's length)")
  public Matrix sum(Matrix matrix, Vector vector) {
    return matrix.apply((rowIndex, row) -> {
      Value[] values = new Value[vector.size()];
      for (int i = 0; i < vector.size(); i++) {
        values[i] = NumericValue.sum(row.get(i), vector.get(i));
      }
      return Vectors.withValues(values);
    });
  }

  /**
   * Computes the matrix-scalar product of the given matrix and scalar value. Returns a new matrix holding
   * the results (whose dimensions are the same as the given matrix' dimensions).
   *
   * @param matrix a {@link Matrix}.
   * @param value a scalar {@link Value}.
   * @return the new {@link Matrix} resulting from this operation.
   */
  @Doc("Computes the matrix-scalar product of the given matrix and value, returning the results in a new matrix")
  public Matrix product(Matrix matrix, Value value) {
    return matrixOp(matrix, (index, v) -> {
      Value[] newValues = new Value[v.size()];
      for (int i = 0; i < newValues.length; i++) {
        newValues[i] = NumericValue.product(v.get(i), value);
      }
      return Vectors.withValues(newValues);
    });
  }

  /**
   * Computes the matrix-vector product of the given matrix and vector - in fact performs the inner product
   * of each row in the given matrix with the provided vector. Returns the results in a new vector.
   *
   * @param matrix a {@link Matrix}.
   * @param vector a {@link Vector}.
   * @return a new {@link Vector} holding the results of this operation.
   */
  @Doc("Computes the matrix-vector product of the given matrix and vector, returning the results in a new vector")
  public Vector product(Matrix matrix, Vector vector) {
    Dimensions dims = matrix.getDimensions();
    Checks.isTrue(dims.getColumnCount() == vector.size(),
            "Matrix has %s columns while length of given vector is %s",
            dims.getColumnCount(), vector.size()
    );
    Value[] values = new Value[dims.getRowCount()];
    TaskExecutor executor = new TaskExecutor(context.getThreadPool());

    for (int i = 0; i < dims.getRowCount(); i++) {
      final int rowIndex = i;
      executor.addTask(() -> {
        Vector row = matrix.getRow(rowIndex);
        double rowProduct = row.innerProduct(vector);
        values[rowIndex] = NumericValue.of(rowProduct);
      });
    }
    try {
      executor.await();
      return Vectors.withValues(values);
    } catch (InterruptedException e) {
      throw new IllegalStateException("Thread interrupted while waiting for operation completion", e);
    }
  }

  /**
   * Computes the matrix-matrix product of the provided matrix and vector. Returns the result in a new matrix.
   *
   * @param m1 a {@link Matrix}.
   * @param m2 another {@link Matrix}.
   * @return a new {@link Matrix}, holding the results of this operation.
   */
  @Doc("Computes the matrix-matrix product of the given matrices, returning the results in a new matrix")
  public Matrix product(Matrix m1, Matrix m2) {
    Checks.isTrue(m1.getDimensions().getColumnCount() == m2.getDimensions().getRowCount(),
            "Matrix has %s columns while given matrix has %s rows. Matrix product requires given matrix " +
                    "to have number of columns equivalent to this matrix' number of columns",
            m1.getDimensions().getColumnCount(), m2.getDimensions().getRowCount());
    return matrixOp(m1, (rowIndex, row) -> {
      double[] total = new double[m2.getDimensions().getColumnCount()];
      for (int i = 0; i < m2.getDimensions().getColumnCount(); i++) {
        Vector otherCol = m2.getColumn(i);
        total[i] = row.innerProduct(otherCol);
      }
      return Vectors.withNumbers(total);
    });
  }

  /**
   * Computes the matrix-matrix sum of the provided matrices. Returns the results a new matrix having the same
   * dimensions as the provided ones.
   *
   * @param m1 a {@link Matrix}.
   * @param m2 another {@link Matrix}.
   * @return a new {@link Matrix}, holding the results of this operation.
   */
  @Doc("Computes the matrix-matrix sum of the given matrices, returning the results in a new matrix")
  public Matrix sum(Matrix m1, Matrix m2) {
    Checks.isTrue(
            m1.getDimensions().equals(m2.getDimensions()),
            "Matrices do not have same dimension (got %s vs %s)",
            m1.getDimensions(), m2.getDimensions()
    );
    return matrixOp(m1, (index, v) -> {
      Value[] newValues = new Value[v.size()];
      Vector otherVector = m2.getRow(index);
      for (int i = 0; i < newValues.length; i++) {
        newValues[i] = NumericValue.sum(v.get(i), otherVector.get(i));
      }
      return Vectors.withValues(newValues);
    });
  }

  /**
   * Performs the transpose of the given matrix.
   *
   * @param matrix the {@link Matrix} to transpose.
   * @return a new {@link Matrix}, corresponding to the transpose of the matrix provided as input.
   */
  @Doc("Returns the transpose of the given matrix, as a new matrix")
  public Matrix transpose(Matrix matrix) {
    RowSet transposedRowset = new RowSet() {
      @Override
      public int size() {
        return matrix.getDimensions().getColumnCount();
      }

      @Override
      public Vector get(int index) throws IllegalArgumentException {
        return matrix.getColumn(index);
      }

      @Override
      public Iterator<Vector> iterator() {
        return new Iterator<Vector>() {
          int count = 0;
          @Override
          public boolean hasNext() {
            return count < matrix.getDimensions().getColumnCount();
          }

          @Override
          public Vector next() {
            return matrix.getColumn(count++);
          }
        };
      }
    };
    return new DefaultMatrix(context, matrix.getDimensions().getRowCount(), transposedRowset);
  }

  @Doc("Returns the specified row-wise operation on each row of the matrix provided as input, returning the results in a new matrix")
  public Matrix matrixOp(Matrix matrix, Matrix.MatrixTransform vectorWiseOp) {
    Dimensions dims = matrix.getDimensions();
    Vector[] rows = new Vector[dims.getRowCount()];

    TaskExecutor executor = new TaskExecutor(context.getThreadPool());
    for (int i = 0; i < dims.getRowCount(); i++) {
      final int vectorIndex = i;
      executor.addTask(() -> {
        Vector v = matrix.getRow(vectorIndex);
        rows[vectorIndex] = vectorWiseOp.apply(vectorIndex, v);
      });
    }

    try {
      executor.await();
      return new DefaultMatrix(context, rows.length == 0 ? 0 : rows[0].size(), new ArrayRowset(rows));
    } catch (InterruptedException e) {
      throw new IllegalStateException("Thread interrupted while waiting for operation completion", e);
    }

  }
}
