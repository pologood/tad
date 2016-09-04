package org.sapia.tad.impl;

import org.sapia.tad.*;
import org.sapia.tad.Vector;
import org.sapia.tad.algo.Criteria;
import org.sapia.tad.transform.slice.Slices;
import org.sapia.tad.util.Checks;
import org.sapia.tad.value.Value;

import java.util.*;

/**
 * Default implementation of the {@link Dataset} interface.
 * 
 * @author yduchesne
 *
 */
public class DefaultDataset implements Dataset {
  
  private ColumnSet columns;
  private RowSet    rows;

  public DefaultDataset(ColumnSet columns, List<Vector> rows) {
    this(columns, new DefaultRowSet(rows));
  }
  
  public DefaultDataset(Collection<Column> columns, List<Vector> rows) {
    this(new DefaultColumnSet(new ArrayList<>(columns)), rows);
  }
  
  public DefaultDataset(ColumnSet columns, RowSet rows) {
    this.columns = columns;
    this.rows    = rows;
  }
  
  @Override
  public ColumnSet getColumnSet() {
    return columns;
  }
  
  @Override
  public Vector getRow(int rowIndex) throws IllegalArgumentException {
    Checks.isTrue(rowIndex < rows.size(), "Invalid row index: %s. Got %s rows", rowIndex, rows.size());
    return rows.get(rowIndex);
  }
  
  @Override
  public int size() {
    return rows.size();
  }
  
  @Override
  public Vector getColumn(String name) throws IllegalArgumentException {
    Column col = this.columns.get(name);
    List<Value> items = new ArrayList<>(rows.size());
    for (Vector row : rows) {
      items.add(row.get(col.getIndex()));
    }
    return new DefaultVector(items);
  }
  
  @Override
  public Vector getColumn(int colIndex) throws IllegalArgumentException {
    columns.get(colIndex);
    List<Value> items = new ArrayList<>(rows.size());
    for (Vector row : rows) {
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
    Column original = columns.get(colIndex);
    Column copy     = original.copy(0);
    return new DefaultDataset(Collections.singleton(copy), result);
  }
  
  @Override
  public Dataset getColumnSubset(String colName, Criteria<Value> filter)
      throws IllegalArgumentException {
    Column col = columns.get(colName);
    return getColumnSubset(col.getIndex(), filter);
  }

  @Override
  public Dataset getSubset(Criteria<RowResult> filter) {
    List<Vector> result = new ArrayList<>();
    
    DefaultRowResult rowResult = new DefaultRowResult(this.columns);
    for (Vector row : rows) {
      rowResult.setVector(row);
      if (filter.matches(rowResult)) {
        result.add(row);
      }
    }
    return new DefaultDataset(columns, result);
  }
  
  @Override
  public Iterator<Vector> iterator() {
    return rows.iterator();
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
  public Dataset head() {
    return Slices.head(this);
  }
  
  @Override
  public Dataset tail() {
    return Slices.tail(this);
  }
  
  @Override
  public String toString() {
    return Datasets.toString(head());
  }
}
