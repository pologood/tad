package org.sapia.tad.transform.pivot;

import org.sapia.tad.*;
import org.sapia.tad.Vector;
import org.sapia.tad.help.Doc;
import org.sapia.tad.impl.*;
import org.sapia.tad.util.Checks;
import org.sapia.tad.util.Data;
import org.sapia.tad.value.NullValue;
import org.sapia.tad.value.NumericValue;
import org.sapia.tad.value.StringValue;
import org.sapia.tad.value.Value;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Given a dataset with the following structure:
 * 
 * <pre>country,year,gdp,military,internet</pre>
 * 
 * Consisting of GDP, military spending, and internet usage per country, per year. One
 * could wish to display GDP per year per country, as follows:
 * <pre>          
 *                 2000 2001 2002 2003
 * -----------------------------------
 *   Angola     
 *         GDP      120  100  105   95
 *   Antigua     
 *         GDP       12   10   10  9.5
 *   ...
 * </pre>
 * 
 * 
 * 
 * @author yduchesne
 *
 */
public class Pivots {
  
  private Pivots() {
  }

  /**
   * @param datasets one or more {@link Dataset}s to merge.
   * @return the {@link IndexedDataset} resulting from the merge operation.
   */
  @Doc("Merges one or more pivot datasets")
  public static IndexedDataset merge(@Doc("pivot datasets to merge") List<PivotDataset> datasets) {
    Checks.isFalse(datasets.size() == 0, "No datasets specified: one must at least be provided");
    if (datasets.size() == 1) {
      return datasets.get(0);
    }
    PivotDataset toMergeTo = datasets.get(0);
    List<Index> toMerge = new ArrayList<Index>();
    
    for (PivotDataset ds : Data.slice(datasets, 1, datasets.size())) {
      toMerge.add(ds.getIndex());
    }
    Index newIndex = toMergeTo.getIndex().mergeWith(toMerge);
    return new PivotDataset(newIndex);
  }
  
  /**
   * @param datasets one or more {@link Dataset}s to merge.
   * @return the {@link IndexedDataset} resulting from the merge operation.
   */
  @Doc("Merges one or more pivot datasets")
  public static IndexedDataset merge(@Doc("pivot datasets to aggregate") PivotDataset...datasets) {
    return merge(Data.list(datasets));
  }

  /**
   * @param dataset a {@link Dataset} for which to create a pivot.
   * @param summaryColumnName the name of the column acting as summary (or pivot) column.
   * @param dimensionColumnNames the names of the columns acting as so-called "dimensions".
   * @return the {@link IndexedDataset} resulting from the "pivoting" of the given dataset.
   */
  public static IndexedDataset pivot(
      @Doc("dataset for which to create a pivot") Dataset dataset, 
      @Doc("summary column name") String summaryColumnName,
      @Doc("dimension (or fact) column names") List<String> dimensionColumnNames) {
    return pivot(dataset, summaryColumnName, dimensionColumnNames.toArray(new String[dimensionColumnNames.size()]));
  }

  /**
   * @param dataset a {@link Dataset} for which to create a pivot.
   * @param summaryColumnName the name of the column acting as summary column.
   * @param dimensionColumnNames the names of the columns acting as so-called "dimensions", or fact columns.
   * @return the {@link IndexedDataset} resulting from the "pivoting" of the given dataset.
   */
  public static IndexedDataset pivot(
      @Doc("dataset for which to create a pivot") Dataset dataset, 
      @Doc("summary column name") String summaryColumnName,
      @Doc("dimensions/fact column names") String...dimensionColumnNames) {
    List<String>   indexColumnNames    = Arrays.asList(dimensionColumnNames);
    IndexedDataset indexed             = dataset.index(dimensionColumnNames);
    String         valueColumnName     = dimensionColumnNames[dimensionColumnNames.length - 1];
    Map<VectorKey, Map<Value, Value>> dimensionValuesBySummaryColumnValues = new TreeMap<>();
    Set<Value>    summaryColumnValues = new HashSet<>();
    
    for (VectorKey key : indexed.getKeys()) {
      for (Vector row : indexed.getRowset(key)) {
        Value summaryColumnValue = row.get(indexed.getColumnSet().get(summaryColumnName).getIndex());
        if (NullValue.isNotNull(summaryColumnValue)) {
          Map<Value, Value> dimensionValues = dimensionValuesBySummaryColumnValues.get(key);
          if (dimensionValues == null) {
            dimensionValues = new HashMap<>();
            dimensionValuesBySummaryColumnValues.put(key, dimensionValues);
          }
          summaryColumnValues.add(summaryColumnValue);
          Value dimensionColumnValue = row.get(indexed.getColumnSet().get(valueColumnName).getIndex());
          Value currentValue = dimensionValues.get(summaryColumnValue);
          if (currentValue == null) {
            currentValue = NumericValue.zero();
            dimensionValues.put(summaryColumnValue, currentValue);
          }
          currentValue = NumericValue.sum(currentValue, dimensionColumnValue);
          dimensionValues.put(summaryColumnValue, currentValue);
        }
      }
    }
    
    AtomicInteger columnCount = new AtomicInteger();
    List<Column> columns = new ArrayList<>();
    for (String dimensionColumnName : dimensionColumnNames) {
      Column toCopy = indexed.getColumnSet().get(dimensionColumnName);
      if (dimensionColumnName.equals(dimensionColumnNames[dimensionColumnNames.length -1])) {
        columns.add(new DefaultColumn(columnCount.getAndIncrement(), Datatype.STRING, "fact_column"));
      } else {
        columns.add(new DefaultColumn(columnCount.getAndIncrement(), toCopy.getType(), toCopy.getName()));
      }
    }
    final Column summaryColumn = indexed.getColumnSet().get(summaryColumnName);
    Map<Value, Column> columnsBySummaryColumnValues = new HashMap<>();
    List<Value> sortedSummaryColumnValues = new ArrayList<>(summaryColumnValues);
    Collections.sort(sortedSummaryColumnValues, new Comparator<Value>() {
      @Override
      public int compare(Value o1, Value o2) {
        return summaryColumn.getType().strategy().compareTo(o1, o2);
      }
    });
    for (Value summaryColumnValue : sortedSummaryColumnValues) {
      Column newColumn = new DefaultColumn(
          columnCount.getAndIncrement(), 
          Datatype.NUMERIC, 
          summaryColumn.getFormat().formatValue(
              summaryColumn.getType(), 
              summaryColumnValue
          ).trim()
      );
      columns.add(newColumn);
      columnsBySummaryColumnValues.put(summaryColumnValue, newColumn);
    }
    
    ColumnSet    columnSet = new DefaultColumnSet(columns);
    List<Vector> rows      = new ArrayList<>(dimensionValuesBySummaryColumnValues.size());
    for (Map.Entry<VectorKey, Map<Value, Value>> entry : dimensionValuesBySummaryColumnValues.entrySet()) {
      Value[] keyValues    = entry.getKey().getValues();
      Value[] vectorValues = new Value[columns.size()];
      for (int i = 0; i < keyValues.length; i++) {
        if (i == keyValues.length - 1) {
          vectorValues[i] = new StringValue(dimensionColumnNames[i]);
        } else {
          vectorValues[i] = keyValues[i];
        }
      }
      Map<Value,Value> dimensionValues = entry.getValue();
      for (Map.Entry<Value, Value> dimensionValue : dimensionValues.entrySet()) {
        Column dimensionValueCol = columnsBySummaryColumnValues.get(dimensionValue.getKey());
        vectorValues[dimensionValueCol.getIndex()] = dimensionValue.getValue();
      }
      rows.add(new DefaultVector(vectorValues));
    }
    
    List<String> newIndexColumnNames = Data.slice(indexColumnNames, indexColumnNames.size() - 1);
    newIndexColumnNames.add("fact_column");

    Index index = new DefaultIndex(new DefaultRowSet(rows), columnSet, columnSet.includes(newIndexColumnNames));
    return new PivotDataset(index);
  }
  
}
