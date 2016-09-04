package org.sapia.tad.transform.range;

import org.sapia.tad.Column;
import org.sapia.tad.Dataset;
import org.sapia.tad.Datatype;
import org.sapia.tad.Vector;
import org.sapia.tad.func.ArgFunction;
import org.sapia.tad.help.Doc;
import org.sapia.tad.impl.DefaultDataset;
import org.sapia.tad.impl.DefaultVector;
import org.sapia.tad.util.Checks;
import org.sapia.tad.util.Data;
import org.sapia.tad.util.Strings;
import org.sapia.tad.value.Value;

import java.util.*;

/**
 * Performs replacement of continuous array with corresponding ranges.
 * 
 * @author yduchesne
 *
 */
@Doc("Performs replacement of continuous array with corresponding ranges")
public class Ranges {

  private Ranges() {
  }
  
  /**
   * @param dataset the dataset whose given column array must be converted to a range.
   * @param partitionSize the size of each range partition.
   * @param columnNames the names of the columns whose array should be transformed into ranges.
   * @return a new {@link Dataset}, with the desired column array converted to ranges.
   */
  @Doc("Replaces continuous array with corresponding ranges, returns the thus processed dataset")
  public static Dataset range(
      @Doc("dataset to process") Dataset dataset, 
      @Doc("size of each range partitions") int partitionSize, 
      @Doc("names of the columns whose array should be replaced by ranges") String...columnNames) {

    Map<Column, List<RangeRef>> rangeValuesByColumn = new HashMap<>(columnNames.length);
    
    // building range array, for each column.
    for (String cn : columnNames) {
      Column col = dataset.getColumnSet().get(cn);
      Checks.isTrue(
          col.getType() == Datatype.NUMERIC || col.getType() == Datatype.DATE, 
          "Invalid type for column %s. Got %s, expected either: %s", 
          col.getName(), col.getType(), Strings.toString(Data.array(Datatype.NUMERIC, Datatype.DATE), 
              new ArgFunction<Datatype, String>() {
                @Override
                public String call(Datatype arg) {
                  return arg.name();
                }
              }
          )
      );

      Vector columnValues = dataset.getColumn(cn);
      List<RowValue> rowValues = new ArrayList<>(columnValues.size());
      for (int i = 0; i < columnValues.size(); i++) {
        rowValues.add(new RowValue(i, col, columnValues.get(i)));
      }
      Collections.sort(rowValues);
      
      // partitioning ---------------------------------------------------------
      List<List<RowValue>> partitions = Data.partition(rowValues, partitionSize);
      
      // substituting each value with its corresponding range -----------------
      List<RangeRef> ranges = new ArrayList<>(rowValues.size());
      
      for (List<RowValue> partition : partitions) {
        Range range = new Range(
            col.getType().comparator(), 
            Data.first(partition).get(),
            Data.last(partition).get()
        );
        for (int i = 0; i < partition.size(); i++) {
          RowValue val = partition.get(i);
          ranges.add(new RangeRef(val.index, range));
        }
      }
      Collections.sort(ranges, new Comparator<RangeRef>() {
        @Override
        public int compare(RangeRef o1, RangeRef o2) {
          return o1.rowIndex - o2.rowIndex;
        }
      });
      rangeValuesByColumn.put(col, ranges);
    }
    
    // populating new dataset with range array, for relevant columns ---------
    List<Vector> newRows = new ArrayList<>();
    for (int i = 0; i < dataset.size(); i++) {
      Vector row = dataset.getRow(i);
      Value[] newRowValues = new Value[dataset.getColumnSet().size()];
      for (Column col : dataset.getColumnSet()) {
        List<RangeRef> ranges = rangeValuesByColumn.get(col);
        if (ranges != null) {
          newRowValues[col.getIndex()] = ranges.get(i).getRange();
        } else {
          newRowValues[col.getIndex()] = row.get(col.getIndex());
        }
      }
      newRows.add(new DefaultVector(newRowValues));
    }
    return new DefaultDataset(dataset.getColumnSet(), newRows);
  }
  
  // ==========================================================================

  static class RangeRef {
    private int           rowIndex;
    private Range range;
    
    RangeRef(int rowIndex, Range range) {
      this.rowIndex = rowIndex;
      this.range = range;
    }
    
    Range getRange() {
      return range;
    }
    
    int getRowIndex() {
      return rowIndex;
    }
  }
  
  // --------------------------------------------------------------------------
  
  static class RowValue implements Comparable<RowValue> {
    
    private int    index;
    private Column column;
    private Value  value;
    
    RowValue(int index, Column col, Value value) {
      this.index  = index;
      this.column = col;
      this.value  = value;
    }
    
    int getIndex() {
      return index;
    }
    
    Column getColumn() {
      return column;
    }
    
    Value get() {
      return value;
    }
    
    @Override
    public String toString() {
      return Strings.toString("index", index, "value", value);
    }
    
    @Override
    public int compareTo(RowValue other) {
      return column.getType().comparator().compare(this.value,  other.value);
    }
    
  }

}
