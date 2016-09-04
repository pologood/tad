package org.sapia.tad.impl;

import org.sapia.tad.*;
import org.sapia.tad.algo.Criteria;
import org.sapia.tad.transform.slice.Slices;
import org.sapia.tad.value.Value;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Adapts an {@link Index} to the {@link Dataset} interface.
 * 
 * @author yduchesne
 *
 */
public class IndexDatasetAdapter implements IndexedDataset {
  
  private Index index;

  /**
   * @param index the {@link Index} that this instance should wrap.
   */
  public IndexDatasetAdapter(Index index) {
    this.index = index;
  }
  
  @Override
  public Vector getColumn(int colIndex) throws IllegalArgumentException {
    RowSet indexRows = index.getRowSet();
    List<Value> items = new ArrayList<>(indexRows.size());
    for (Vector row : indexRows) {
      items.add(row.get(colIndex));
    }
    return new DefaultVector(items);
  }
  
  @Override
  public Vector getColumn(String name) throws IllegalArgumentException {
    return getColumn(index.getColumnSet().get(name).getIndex());
  }

  @Override
  public ColumnSet getColumnSet() {
    return index.getColumnSet();
  }
  
  @Override
  public Dataset getColumnSubset(int colIndex, Criteria<Value> filter)
      throws IllegalArgumentException {
    RowSet indexRows = index.getRowSet();
    List<Vector> subsetRows = new ArrayList<>();
    for (Vector row : indexRows) {
      if (filter.matches(row.get(colIndex))) {
        subsetRows.add(new DefaultVector(row.get(colIndex)));
      }
    }
    Column col = index.getColumnSet().get(colIndex);
    ColumnSet subsetCols = new DefaultColumnSet(new DefaultColumn(0, col.getType(), col.getName()));
    return new DefaultDataset(subsetCols, subsetRows);
  }
  
  @Override
  public Dataset getColumnSubset(String colName, Criteria<Value> filter)
      throws IllegalArgumentException {
    return getColumnSubset(index.getColumnSet().get(colName).getIndex(), filter);
  }
  
  @Override
  public Vector getRow(int rowIndex) throws IllegalArgumentException {
    return index.getRowSet().get(rowIndex);
  }
  
  @Override
  public Dataset getSubset(Criteria<RowResult> filter) {
    RowSet indexRows = index.getRowSet();
    List<Vector> subsetRows = new ArrayList<>();
    DefaultRowResult rowResult = new DefaultRowResult(index.getColumnSet());
    for (Vector row : indexRows) {
      rowResult.setVector(row);
      if (filter.matches(rowResult)) {
        subsetRows.add(row);
      }
    }
    return new DefaultDataset(index.getColumnSet(), subsetRows);
  }
  
  @Override
  public IndexedDataset index(String... colNames) throws IllegalArgumentException {
    if (this.index.getIndexedColumnSet().contains(colNames)) {
      return this;
    } else {
      Index newIndex = new DefaultIndex(index.getRowSet(), index.getColumnSet(), index.getColumnSet().includes(colNames));
      return new IndexDatasetAdapter(newIndex);
    }
  }
  
  @Override
  public IndexedDataset index(List<String> colNames)
      throws IllegalArgumentException {
    return index(colNames.toArray(new String[colNames.size()]));
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
  public Iterator<Vector> iterator() {
    return index.getRowSet().iterator();
  }
  
  @Override
  public int size() {
    return index.size();    
  }

  @Override
  public ColumnSet getIndexedColumnSet() {
    return index.getIndexedColumnSet();
  }

  @Override
  public Collection<VectorKey> getKeys() {
    return index.getKeys();
  }
  
  @Override
  public RowSet getRowSet(String[] columNames, Value[] values) {
    return index.getRowSet(columNames, values);
  }
  
  @Override
  public RowSet getRowset(VectorKey key) {
    return index.getRowset(key);
  }
  
  @Override
  public String toString() {
    return Datasets.toString(head());
  }
  
  /**
   * @return this instance's internal {@link Index}.
   */
  public Index getIndex() {
    return index;
  }
  
}
