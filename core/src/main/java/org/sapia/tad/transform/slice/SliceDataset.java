package org.sapia.tad.transform.slice;

import org.sapia.tad.*;
import org.sapia.tad.Vector;
import org.sapia.tad.algo.Criteria;
import org.sapia.tad.impl.*;
import org.sapia.tad.util.Checks;
import org.sapia.tad.value.Value;

import java.util.*;

class SliceDataset implements Dataset {
  
  protected final Dataset delegate;
  private int start, end, size;
  
  SliceDataset(Dataset delegate, int start, int end) {
    Checks.isTrue(
        start <= end, 
        "Slice start index must be lower than or equal to slice end index (got start = %s, end = %s)", 
        start, end);
    this.delegate = delegate;
    this.start = start;
    this.end = end;
    this.size = end - start;
  }
  
  @Override
  public ColumnSet getColumnSet() {
    return delegate.getColumnSet();
  }
  
  @Override
  public Vector getColumn(String name) throws IllegalArgumentException {
    return getColumn(delegate.getColumnSet().get(name).getIndex());
  }
  
  @Override
  public Vector getColumn(int colIndex) throws IllegalArgumentException {
    delegate.getColumnSet().get(colIndex);
    List<Value> items = new ArrayList<>(size);
    for (int i = 0; i < size; i++) {
      Vector row = getRow(i);
      items.add(row.get(colIndex));
    }
    return new DefaultVector(items);
  }

  @Override
  public Dataset getColumnSubset(int colIndex, Criteria<Value> filter)
      throws IllegalArgumentException {
    Vector       col    = getColumn(colIndex);
    List<Vector> result = new ArrayList<>();
    for (Value rowItem : col) {
      if (filter.matches(rowItem)) {
        Vector row = new DefaultVector(Collections.singletonList(rowItem));
        result.add(row);
      }
    }
    Column original = delegate.getColumnSet().get(colIndex);
    Column copy     = new DefaultColumn(0, original.getType(), original.getName());
    return new DefaultDataset(Collections.singleton(copy), result);
  }
  
  @Override
  public Dataset getColumnSubset(String colName, Criteria<Value> filter)
      throws IllegalArgumentException {
    Column col = delegate.getColumnSet().get(colName);
    return getColumnSubset(col.getIndex(), filter);
  }

  @Override
  public Dataset getSubset(Criteria<RowResult> filter) {
    List<Vector> result = new ArrayList<>();
    
    DefaultRowResult rowResult = new DefaultRowResult(delegate.getColumnSet());
   
    for (int i = 0; i < delegate.size(); i++) {
      Vector row = delegate.getRow(i);
      rowResult.setVector(row);
      if (filter.matches(rowResult)) {
        result.add(row);
      }
    }
    return new DefaultDataset(delegate.getColumnSet(), result);
  }
  
  @Override
  public Vector getRow(int rowIndex) throws IllegalArgumentException {
    int realIndex = start + rowIndex;
    Checks.isFalse(realIndex < start || realIndex >= end, "Invalid index: %s. Dataset has %s rows", rowIndex, size);
    return delegate.getRow(realIndex);
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
    Index index = new DefaultIndex(new DatasetRowSetAdapter(this), delegate.getColumnSet(), delegate.getColumnSet().includes(colNames));
    return new IndexDatasetAdapter(index);
  }
  
  @Override
  public IndexedDataset index(List<String> colNames)
      throws IllegalArgumentException {
    return index(colNames.toArray(new String[colNames.size()]));
  }
  
  @Override
  public Iterator<Vector> iterator() {
    return new Iterator<Vector>() {
      private int index;
      @Override
      public boolean hasNext() {
        return index < (end - start);
      }
      
      @Override
      public Vector next() {
        if (index >= (end - start)) {
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
  public int size() {
    return size;
  }
  
  @Override
  public String toString() {
    return Datasets.toString(this);
  }
  
}
