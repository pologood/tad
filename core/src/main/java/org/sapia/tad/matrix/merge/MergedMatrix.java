package org.sapia.tad.matrix.merge;

import lombok.RequiredArgsConstructor;
import org.sapia.tad.TadContext;
import org.sapia.tad.concurrent.TaskExecutor;
import org.sapia.tad.impl.VectorImpl;
import org.sapia.tad.matrix.Dimensions;
import org.sapia.tad.matrix.Matrix;
import org.sapia.tad.Vector;
import org.sapia.tad.Vectors;
import org.sapia.tad.matrix.impl.MatrixImpl;
import org.sapia.tad.util.Checks;
import org.sapia.tad.value.NumericValue;
import org.sapia.tad.value.Value;

import java.util.Iterator;
import java.util.List;

/**
 * Combines multiple matrices into one (given matrices must have same number of columns).
 *
 * @author yduchesne
 */
public class MergedMatrix extends MatrixImpl {

  private TadContext      context;
  private Matrix[]        delegates;
  private MatrixPointer[] pointers;
  private Dimensions      dims;

  public MergedMatrix(TadContext context, List<Matrix> delegates) {
    this.context    = context;
    this.delegates  = delegates.toArray(new Matrix[delegates.size()]);
    int columnCount = 0;
    for (Matrix m : delegates) {
      columnCount += m.getDimensions().getColumnCount();
    }
    pointers        = new MatrixPointer[columnCount];
    int columnIndex = 0;
    int determinedRowCount = -1;
    for (int i = 0; i < this.delegates.length;  i++) {
      int tmpDeterminedRowCount = this.delegates[i].getDimensions().getRowCount();
      if (determinedRowCount == -1) {
        determinedRowCount = tmpDeterminedRowCount;
      } else {
        Checks.isTrue(
                tmpDeterminedRowCount == determinedRowCount,
                "Matrices should have equal number of rows: found one matrix with %s rows - expected %s",
                tmpDeterminedRowCount, determinedRowCount
        );
      }
      for (int j = 0; j < this.delegates[i].getDimensions().getColumnCount(); j++) {
        pointers[columnIndex++] = new MatrixPointer(i, j);
      }
    }
    dims = new Dimensions(this.delegates.length > 0 ? this.delegates[0].getDimensions().getRowCount() : 0, columnCount);
  }

  @Override
  public Dimensions getDimensions() {
    return dims;
  }

  @Override
  public Vector getColumn(int index) {
    MatrixPointer pointer = pointers[index];
    Matrix delegate = delegates[pointer.matrixIndex];
    return delegate.getColumn(pointer.columnIndex);
  }

  @Override
  public Vector getRow(int rowIndex) {
    return new VectorImpl() {
      @Override
      public int size() {
        return dims.getColumnCount();
      }

      @Override
      public Value get(int index) throws IllegalArgumentException {
        MatrixPointer pointer = pointers[index];
        Matrix delegate = delegates[pointer.matrixIndex];
        return delegate.getRow(rowIndex).get(pointer.columnIndex);
      }

      @Override
      public Vector subset(int... indices) throws IllegalArgumentException {
        Value[] values = new Value[indices.length];
        for (int i = 0; i < indices.length; i++) {
          values[i] = get(indices[i]);
        }
        return Vectors.withValues(values);
      }

      @Override
      public Value[] toArray() {
        Value[] values = new Value[size()];
        for (int i = 0; i < values.length; i++) {
          values[i] = get(i);
        }
        return values;
      }

      @Override
      public Iterator<Value> iterator() {
        return new Iterator<Value>() {
          int index = 0;
          @Override
          public boolean hasNext() {
            return index < size();
          }

          @Override
          public Value next() {
            return get(index++);
          }
        };
      }
    };
  }

  @Override
  public Matrix product(Value value) {
    return context.getMatrixModule().product(this, value);
  }

  @Override
  public Vector product(Vector vector) {
    return context.getMatrixModule().product(this, vector);
  }

  @Override
  public Matrix product(Matrix other) {
    return context.getMatrixModule().product(this, other);
  }

  @Override
  public Matrix sum(Matrix other) {
    return context.getMatrixModule().sum(this, other);
  }

  @Override
  public Matrix transpose() {
    return context.getMatrixModule().transpose(this);
  }

  @Override
  public Matrix apply(MatrixTransform operation) {
    return null;
  }

  @RequiredArgsConstructor
  class MatrixPointer {

    private final int matrixIndex;
    private final int columnIndex;

  }


}
