package org.sapia.tad.matrix;

import org.sapia.tad.util.Objects;

/**
 * Holds the dimensions of a matrix.
 */
public class Dimensions {

  private int rowCount, columnCount;

  /**
   * @param rowCount the number of rows in the {@link Matrix} to which this instance corresponds.
   * @param columnCount the number of columns in the {@link Matrix} to which this instance corresponds.
   */
  public Dimensions(int rowCount, int columnCount) {
    this.rowCount = rowCount;
    this.columnCount = columnCount;
  }

  /**
   * @return the number of rows in this instance's corresponding matrix.
   */
  public int getRowCount() {
    return rowCount;
  }

  /**
   * @return of columns in this instance's corresponding matrix.
   */
  public int getColumnCount() {
    return columnCount;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Dimensions) {
      Dimensions other = (Dimensions) obj;
      return rowCount == other.rowCount && columnCount == other.columnCount;
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.safeHashCode(rowCount, columnCount);
  }

  @Override
  public String toString() {
    return "[rows=" + rowCount + ", columns=" + columnCount + "]";
  }
}
