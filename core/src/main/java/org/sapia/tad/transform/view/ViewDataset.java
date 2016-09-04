package org.sapia.tad.transform.view;

import org.sapia.tad.*;
import org.sapia.tad.algo.Criteria;
import org.sapia.tad.impl.*;
import org.sapia.tad.transform.slice.Slices;
import org.sapia.tad.util.Checks;
import org.sapia.tad.value.Value;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * An instance of this class wraps another {@link Dataset}, only showing columns
 * that have been predetermined.
 * 
 * @author yduchesne
 *
 */
public class ViewDataset implements Dataset {
  
  private Dataset   delegate;
  private ColumnSet columns;
  private int[]     columnIndices;

  ViewDataset(Dataset delegate, ColumnSet columns, int[] columnIndices) {
    this.delegate      = delegate;
    this.columns       = columns;
    this.columnIndices = columnIndices;
  }
  
  @Override
  public ColumnSet getColumnSet() {
    return columns;
  }
  
  @Override
  public Vector getColumn(int colIndex) throws IllegalArgumentException {
    return delegate.getColumn(realIndex(colIndex));
  }
  
  @Override
  public Vector getColumn(String name) throws IllegalArgumentException {
    return delegate.getColumn(realIndex(columns.get(name).getIndex()));
  }
  
  @Override
  public Dataset getColumnSubset(int colIndex, Criteria<Value> filter)
      throws IllegalArgumentException {
    return delegate.getColumnSubset(realIndex(colIndex), filter);
  }
  
  @Override
  public Dataset getColumnSubset(String colName, Criteria<Value> filter)
      throws IllegalArgumentException {
    return delegate.getColumnSubset(realIndex(columns.get(colName).getIndex()), filter);
  }
  
  @Override
  public Vector getRow(int rowIndex) throws IllegalArgumentException {
    return new ViewVector(columnIndices, delegate.getRow(rowIndex));
  }
  
  @Override
  public Dataset getSubset(Criteria<RowResult> filter) {
    List<Vector> result = new ArrayList<>();
    
    DefaultRowResult rowResult = new DefaultRowResult(this.columns);
    for (Vector row : delegate) {
      ViewVector vrow = new ViewVector(columnIndices, row);
      rowResult.setVector(vrow);
      if (filter.matches(rowResult)) {
        result.add(vrow);
      }
    }
    return new DefaultDataset(columns, result);
  }
  
  @Override
  public Dataset head() {
    return Slices.head(this);
  }
  
  @Override
  public Dataset tail() {
    return Slices.tail(this);
  }

  @Override
  public IndexedDataset index(String... colNames) throws IllegalArgumentException {
    Index index = new DefaultIndex(new DatasetRowSetAdapter(this), columns, columns.includes(colNames));
    return new IndexDatasetAdapter(index);
  }
  
  @Override
  public IndexedDataset index(List<String> colNames)
      throws IllegalArgumentException {
    return index(colNames.toArray(new String[colNames.size()]));
  }
  
  @Override
  public int size() {
    return delegate.size();
  }
  
  @Override
  public Iterator<Vector> iterator() {
    return new Iterator<Vector>() {
      private int index;
      @Override
      public boolean hasNext() {
        return index < delegate.size();
      }
      @Override
      public Vector next() {
        if (index >= delegate.size()) {
          throw new NoSuchElementException();
        }
        return getRow(index++);
      }
      @Override
      public void remove() {
      }
    };
  }
  
  @Override
  public String toString() {
    return Datasets.toString(head());
  }
  
  private int realIndex(int i) {
    Checks.isTrue(i >= 0 && i < columnIndices.length, "Invalid column index: %s (dataset has %s columns)", i, columnIndices.length);
    return columnIndices[i];
  }
}
