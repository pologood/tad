package org.sapia.tad.impl;

import org.sapia.tad.*;
import org.sapia.tad.Vector;
import org.sapia.tad.func.ArgFunction;
import org.sapia.tad.util.Checks;
import org.sapia.tad.util.Data;
import org.sapia.tad.util.Strings;
import org.sapia.tad.value.NullValue;
import org.sapia.tad.value.Value;

import java.util.*;
import java.util.Map.Entry;

public class DefaultIndex implements Index {
  
  private Map<VectorKey, RowSet>   rowsets;
  private ColumnSet                columns, indexedColumns;
  private int                      size;
  private TreeMap<Integer, RowSet> rowsetsByIndex;
  
  private DefaultIndex(DefaultIndex other) {
    rowsets = new TreeMap<>();
    rowsets.putAll(other.rowsets);
    rowsetsByIndex = new TreeMap<>();
    rowsetsByIndex.putAll(other.rowsetsByIndex);
    this.columns = other.columns;
    this.indexedColumns = other.indexedColumns;
    this.size = other.size;
  }
  
  public DefaultIndex(RowSet rows, ColumnSet columns, ColumnSet indexedColumns) {
    if (rows.size() > 0) {
      Checks.isTrue(rows.get(0).size() == columns.size(), 
          "Invalid row length: got %s item(s), expecting: %s", 
          rows.get(0).size(), columns.size());
    }
    init(rows, indexedColumns);
    this.columns        = columns;
    this.indexedColumns = indexedColumns;
  }
  
  @Override
  public ColumnSet getColumnSet() {
    return columns;
  }
  
  @Override
  public ColumnSet getIndexedColumnSet() {
    return indexedColumns;
  }
  
  @Override
  public Collection<VectorKey> getKeys() {
    return Collections.unmodifiableCollection(rowsets.keySet());
  }
  
  @Override
  public int size() {
    return size;
  }
  
  @Override
  public RowSet getRowSet() {
    if (size == 0) {
      return new NullRowset();
    }
    
    return new RowSet() {
      @Override
      public Iterator<Vector> iterator() {
        return new CompositeIterator<Vector>(new Iterator<Iterator<Vector>>() {
          
          private Iterator<RowSet> iterator = rowsets.values().iterator();
          
          @Override
          public boolean hasNext() {
            return iterator.hasNext();
          }
          @Override
          public Iterator<Vector> next() {
            return iterator.next().iterator();
          }
          @Override
          public void remove() {
          }
        });
      }
      
      @Override
      public int size() {
        return size;
      }
      
      @Override
      public Vector get(int index) throws IllegalArgumentException {
        return DefaultIndex.this.get(index);
      }
    };
  }
  
  public Vector get(int index) {
    Integer                indexKey = new Integer(index);
    Entry<Integer, RowSet> entry    = rowsetsByIndex.floorEntry(indexKey);
    return entry.getValue().get(index - entry.getKey());
  }
  
  @Override
  public Set<Value> getValueSet(String columnName) {
    Set<Value> values = new HashSet<>();
    Column col = columns.get(columnName);
    for (Vector v : getRowSet()) {
      Value value = v.get(col.getIndex());
      if (NullValue.isNull(value)) {
        values.add(NullValue.getInstance());
      } else {
        values.add(value);
      }
    }
    return values;
  }
  
  @Override
  public RowSet getRowSet(String[] columnNames, Value[] values) {
    Checks.isTrue(columnNames.length == indexedColumns.size(), 
        "Expected column names %s. Got: %s", 
        Strings.toString(indexedColumns.getColumns(), new ArgFunction<Column, String>() {
          @Override
          public String call(Column arg) {
            return arg.getName();
          }
        }), 
        Strings.toString(Data.list(columnNames), new ArgFunction<String, String>() {
          @Override
          public String call(String arg) {
            return arg;
          }
        })
    );
    VectorKey key = new VectorKey(indexedColumns, values);
    RowSet rowset = rowsets.get(key);
    if (rowset == null) {
      return new NullRowset();
    }
    return rowset;
  }
  
  @Override
  public RowSet getRowset(VectorKey key) {
    RowSet rowset = rowsets.get(key);
    if (rowset == null) {
      return new NullRowset();
    }
    return rowset;
  }
  
  @Override
  public Index mergeWith(Index... indices) {
    return mergeWith(Arrays.asList(indices));
  }
  
  @Override
  public Index mergeWith(List<Index> indices) {
    DefaultIndex index = new DefaultIndex(this);
    for (Index i : indices) {
      Checks.isTrue(index.columns.equals(i.getColumnSet()), "Indices must have the same columns");
      Checks.isTrue(index.indexedColumns.equals(i.getIndexedColumnSet()), "Indices must have the same index columns");
      for (VectorKey key : i.getKeys()) {
        RowSet rows = i.getRowset(key);
        index.rowsets.put(key, rows);
      }
    }
    index.rowsetsByIndex = new TreeMap<>();
    int size = 0;
    for (Map.Entry<VectorKey, RowSet> entry : index.rowsets.entrySet()) {
      rowsetsByIndex.put(new Integer(size), entry.getValue());
      size += entry.getValue().size();
    }
    return index;
  }
  
  private void init(RowSet rows, ColumnSet columns) {
    VectorKey current = null;
    Map<VectorKey, List<Vector>> rowsByGroupKey = new TreeMap<>();
    for (Vector row : rows) {
      if (current == null) {
        current = new VectorKey(columns, row);
      } else {
        VectorKey key = new VectorKey(columns, row);
        if (!key.equals(current)) {
          current = key;
        } 
      }
      List<Vector> groupRows = rowsByGroupKey.get(current);
      if (groupRows == null) {
        groupRows = new ArrayList<>();
        rowsByGroupKey.put(current, groupRows);
      }
      groupRows.add(row);
    }
    
    rowsets = new TreeMap<>();
    rowsetsByIndex = new TreeMap<>();
    for (Map.Entry<VectorKey, List<Vector>> entry : rowsByGroupKey.entrySet()) {
      RowSet rowset = new DefaultRowSet(entry.getValue());
      rowsetsByIndex.put(new Integer(size), rowset);
      size += entry.getValue().size();
      rowsets.put(entry.getKey(), rowset);
    }
  }

}
