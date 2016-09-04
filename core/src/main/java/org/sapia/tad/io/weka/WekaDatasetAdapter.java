package org.sapia.tad.io.weka;

import org.sapia.tad.*;
import org.sapia.tad.algo.Criteria;
import org.sapia.tad.impl.*;
import org.sapia.tad.transform.slice.Slices;
import org.sapia.tad.util.Checks;
import org.sapia.tad.value.Value;
import weka.core.Attribute;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Adapts Weka's {@link Instances} interface to the {@link Dataset} interface.
 * 
 * @author yduchesne
 *
 */
public class WekaDatasetAdapter implements Dataset {
  
  private Instances instances;
  private ColumnSet columnSet;
  
  /**
   * @param instances the {@link Instances} to wrap.
   */
  public WekaDatasetAdapter(Instances instances) {
    this.instances = instances;
    
    List<Column> columns = new ArrayList<>();
    for (int i = 0; i < instances.numAttributes(); i++) {
      Attribute attr = instances.attribute(i);
      Datatype type;
      if (attr.type() == Attribute.DATE) {
        type = Datatype.DATE;
      } else if (attr.type() == Attribute.NOMINAL) {
        type = Datatype.STRING;
      } else if (attr.type() == Attribute.RELATIONAL) {
        type = Datatype.NUMERIC;
      } else if (attr.type() == Attribute.NUMERIC) {
        type = Datatype.NUMERIC;
      } else if (attr.type() == Attribute.STRING) {
        type = Datatype.STRING;
      } else {
        throw new IllegalArgumentException("Unknown Weka data type: " + attr.type() + " for attribute " + attr.name());
      }
      columns.add(new DefaultColumn(i, type, attr.name()));
    }
    this.columnSet = new DefaultColumnSet(columns);
  }
  
  @Override
  public Vector getColumn(int colIndex) throws IllegalArgumentException {
    return null;
  }
  
  @Override
  public Vector getColumn(String name) throws IllegalArgumentException {
    
    return null;
  }
  
  @Override
  public ColumnSet getColumnSet() {
    return columnSet;
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
    Column original = columnSet.get(colIndex);
    Column copy     = new DefaultColumn(0, original.getType(), original.getName());
    return new DefaultDataset(Collections.singleton(copy), result);
  }
  
  @Override
  public Dataset getColumnSubset(String colName, Criteria<Value> filter)
      throws IllegalArgumentException {
    Column col = columnSet.get(colName);
    return getColumnSubset(col.getIndex(), filter);
  }
  
  @Override
  public Vector getRow(int rowIndex) throws IllegalArgumentException {
    Checks.isTrue(rowIndex < instances.numInstances(), "Invalid index: %s. Got %s rows", rowIndex, instances.numInstances());
    return new WekaVectorAdapter(instances.instance(rowIndex));
  }
  
  @Override
  public Dataset getSubset(Criteria<RowResult> filter) {
    List<Vector> result = new ArrayList<>();
    for (int i = 0; i < instances.numInstances(); i++) {
      Vector row = getRow(i);
      DefaultRowResult rowResult = new DefaultRowResult(columnSet);
      rowResult.setVector(row);
      if (filter.matches(rowResult)) {
        result.add(row);
      }
    }
    return new DefaultDataset(columnSet, result);
  }
  
  @Override
  public IndexedDataset index(String... colNames) throws IllegalArgumentException {
    Index index = new DefaultIndex(new DatasetRowSetAdapter(this), columnSet, columnSet.includes(colNames));
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
        return index < instances.numInstances();
      }
      
      @Override
      public Vector next() {
        return new WekaVectorAdapter(instances.instance(index++));
      }
      
      @Override
      public void remove() {
      }
    };
  }
  
  @Override
  public int size() {
    return instances.numInstances();
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
