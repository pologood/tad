package org.sapia.tad.matrix.impl;

import org.sapia.tad.TadContext;
import org.sapia.tad.impl.VectorImpl;
import org.sapia.tad.matrix.Dimensions;
import org.sapia.tad.matrix.Matrices;
import org.sapia.tad.matrix.Matrix;
import org.sapia.tad.RowSet;
import org.sapia.tad.Vector;
import org.sapia.tad.Vectors;
import org.sapia.tad.concurrent.TaskExecutor;
import org.sapia.tad.impl.ArrayRowset;
import org.sapia.tad.impl.DefaultRowSet;
import org.sapia.tad.util.Checks;
import org.sapia.tad.util.Objects;
import org.sapia.tad.value.NumericValue;
import org.sapia.tad.value.Value;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * Default implementation of the {@link Matrix interface}.
 */
public class DefaultMatrix extends MatrixImpl {

  private TadContext  context;
  private RowSet      delegate;
  private Dimensions  dimensions;

  public DefaultMatrix(TadContext context, int columnCount, List<Vector> rows) {
    this(context, columnCount, new DefaultRowSet(rows));
  }

  public DefaultMatrix(TadContext context, int columnCount, RowSet rowSet) {
    this.context = context;
    this.delegate = rowSet;
    if (rowSet.size() > 0) {
      Checks.isTrue(
              rowSet.get(0).size() == columnCount,
              "Provided rowset has %s columns. This does not match the column count that this matrix supports: %s",
              rowSet.get(0).size(), columnCount
      );
    }
    dimensions = new Dimensions(rowSet.size(), columnCount);
  }

  @Override
  public Matrix transpose() {
    return context.getMatrixModule().transpose(this);
  }

  @Override
  public Vector getColumn(int columnIndex) {
    return new VectorImpl() {

      @Override
      public int size() {
        return delegate.size();
      }

      @Override
      public Value get(int index) throws IllegalArgumentException {
        int targetRowIndex = (columnIndex + index * dimensions.getColumnCount()) / dimensions.getColumnCount();
        int targetColIndex = (columnIndex + index * dimensions.getColumnCount()) % dimensions.getColumnCount();
        Checks.isTrue(targetRowIndex < delegate.size(), "Invalid index: %s (expected < %s)", targetRowIndex, delegate.size());
        return delegate.get(targetRowIndex).get(targetColIndex);
      }
    };
  }

  @Override
  public Vector getRow(int index) {
    return delegate.get(index);
  }

  @Override
  public Dimensions getDimensions() {
    return dimensions;
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
  public Matrix apply(MatrixTransform operation) {
    return context.getMatrixModule().matrixOp(this, operation);
  }
}
