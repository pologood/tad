package org.sapia.tad.transform.filter;

import org.mvel2.MVEL;
import org.sapia.tad.*;
import org.sapia.tad.Vector;
import org.sapia.tad.algo.Criteria;
import org.sapia.tad.func.ArgFunction;
import org.sapia.tad.help.Doc;
import org.sapia.tad.help.Example;
import org.sapia.tad.impl.DefaultColumn;
import org.sapia.tad.impl.DefaultDataset;
import org.sapia.tad.impl.DefaultVector;
import org.sapia.tad.transform.slice.Slices;
import org.sapia.tad.util.Checks;
import org.sapia.tad.util.Data;
import org.sapia.tad.util.MultiMap;
import org.sapia.tad.util.SetMultiMap;
import org.sapia.tad.value.NullValue;
import org.sapia.tad.value.Value;

import java.io.Serializable;
import java.util.*;

/**
 * Provides methods for filtering data.
 * 
 * @author yduchesne
 *
 */
@Doc("Provides methods for filtering data")
public class Filters {

  private Filters() {
  }
  
  /**
   * Removes <code>null</code> array from the given dataset, for the specified columns.
   * 
   * @param dataset the {@link Dataset} to filter.
   * @param columnNames the {@link List} of column names of the columns whose corresponding rows 
   * with <code>null</code> array should be removed.
   * @return a {@link Dataset} with the <code>null</code> array removed, for the specified
   * columns.
   */
  @Doc("Removes all rows with a null value in at least one of the columns specified")
  public static Dataset removeNulls(
      @Doc("a dataset") Dataset dataset, 
      @Doc("the name of the columns to check for null") List<String> columnNames) {
    final Set<String> nameSet = new HashSet<>(columnNames);
    return dataset.getSubset(new Criteria<RowResult>() {
      @Override
      public boolean matches(RowResult v) {
        for (String n : nameSet) {
          if (NullValue.isNull(v.get(n))) {
            return false;
          }
        }
        return true;
      }
    });
  }
  
  /**
   * Applies the given replacement function to the array of the specified column. The
   * function takes the given value of a column row as input, and returns the replacement value.
   * <p>
   * The new array must all be of the same data type (which also must be provided to this method).
   * That data type will be assigned to the column that's been processed, in the returned
   * dataset.
   * 
   * @param dataset the {@link Dataset} to process.
   * @param datatype the {@link Datatype} of the new array.
   * @param function the {@link ArgFunction} instance to call for performing the replacement.
   * @return a new {@link Dataset}, with the relevant array replaced.
   */
  @Doc("Applies the given replacement function to the array of the specified column")
  public static Dataset replace(      
      @Doc("a dataset") Dataset dataset, 
      @Doc("an array holding the name(s) of the column(s) to process") String[] colNames,
      @Doc("the datatype of the new array in the processed column") Datatype datatype,
      @Doc("the replacement function to use") ArgFunction<Value, Value> function) {
    
    List<Vector> newRows = new ArrayList<>();
    
    Set<Integer> indices = Data.setOfInts(dataset.getColumnSet().getColumnIndices(colNames));
    for (Vector row : dataset) {
      Value[] newValues = new Value[row.size()];
      for (int i = 0; i < row.size(); i++) {
        if (indices.contains(i)) {
          Value val = function.call(row.get(i));
          if (datatype.strategy().isType(val)) {
            newValues[i] = val;
          } else {
            throw new IllegalArgumentException(String.format("Value %s cannot be assigned to new column type %s", datatype));
          }
        } else {
          newValues[i] = row.get(i);
        }
      }
      newRows.add(new DefaultVector(newValues));     
    }
    
    List<Column> newColumns = new ArrayList<>();
    for (int i = 0; i < dataset.getColumnSet().size(); i++) {
      if (indices.contains(i)) {
        newColumns.add(new DefaultColumn(i, datatype, dataset.getColumnSet().get(i).getName()));
      } else {
        newColumns.add(dataset.getColumnSet().get(i));
      }
    }
    
    return new DefaultDataset(newColumns, newRows);
  }
  
  /**
   * @param dataset a dataset for which to convert certain columns to nominals.
   * @param columnNames the array of column names from.
   * @return the {@link Dataset}.
   */
  @Doc("Transform the array in the given columns to nominal array")
  public static Dataset replaceWithNominal(
      @Doc("the dataset to process") Dataset dataset, 
      @Doc("the names of the columns whose array should be converted to nominal array") String...columnNames) {
    
    List<Datatype> datatypes = Data.list(dataset.getColumnSet().getColumnTypes(columnNames));
    Checks.isTrue(
        Data.containsOnly(Datatype.STRING, datatypes), 
        "The type of the given columns should be %s. Got: %s for the respective columns",
        Datatype.STRING, datatypes
    );
    
    MultiMap<String, Value> nominalsByColumn = SetMultiMap.createTreeSetMultiMap();
    
    // extracting set of all array for each column
    for (Vector r : dataset) {
      for (String n : columnNames) {
        Column c     = dataset.getColumnSet().get(n);
        Value value = r.get(c.getIndex());
        if (!NullValue.isNull(value)) {
          nominalsByColumn.put(c.getName(), value);
        }
      }
    }
    
    // creating map on nominal sets - on a per-column basis
    Map<String, NominalSet> nominals = new HashMap<>();
    for (String colName : nominalsByColumn.keySet()) {
      NominalSet nominalSet = NominalSet.newInstance(nominalsByColumn.get(colName));
      nominals.put(colName, nominalSet);
    }
    
    // creating new colums
    List<Column> cols = new ArrayList<>();
    for (Column c : dataset.getColumnSet()) {
      if (nominals.containsKey(c.getName())) {
        cols.add(new DefaultColumn(nominals.get(c.getName()), c.getIndex(), Datatype.NUMERIC, c.getName()));
      } else {
        cols.add(c);
      }
    }
    
    // creating list of rows
    List<Vector> rows = new ArrayList<>();
    for (Vector r : dataset) {
      Value[] values = new Value[r.size()];
      for (Column c : cols) {
        if (!c.getNominalValues().isEmpty()) {
          String nominal = r.get(c.getIndex()).toString();
          values[c.getIndex()] = c.getNominalValues().getByName(nominal);
        } else {
          values[c.getIndex()] = r.get(c.getIndex());
        }
      }
      rows.add(new DefaultVector(values));
    }
    
    return new DefaultDataset(cols, rows);
  }
  
  /**
   * Removes <code>null</code> array from the given dataset, for the specified columns.
   * 
   * @param dataset the {@link Dataset} to filter.
   * @param columnNames the array of column names of the columns whose corresponding rows 
   * with <code>null</code> array should be removed.
   * @return a {@link Dataset} with the <code>null</code> array removed, for the specified
   * columns.
   */
  @Doc("Removes all rows with a null value in at least one of the columns specified")
  public static Dataset removeNulls(
      @Doc("a dataset") Dataset dataset, 
      @Doc("the name of the columns to check for null") String...columnNames) {
    return removeNulls(dataset, Arrays.asList(columnNames));
  }
  
  /**
   * Removes <code>null</code> array from the given dataset, for any column.
   * 
   * @param dataset the {@link Dataset} to filter.
   * @return a dataset, with any row having <code>null</code> array filtered out.
   */
  @Doc("Removes all rows with at least one null, in any column")
  public static Dataset removeAnyNulls(Dataset dataset) {
    return removeNulls(dataset, dataset.getColumnSet().getColumnNames());
  }
  
  /**
   * Removes the given number of rows from the head of the dataset.
   * 
   * @param dataset the {@link Dataset} whose head should be removed.
   * @param numberOfRows the number of rows to remove from the head of the dataset.
   * @return a new {@link Dataset}, with the desired number of rows removed.
   */
  @Doc("Removes the given number of rows from the head of the dataset")
  public static Dataset removeHead(
      @Doc("a dataset") Dataset dataset, 
      @Doc("number of rows") int numberOfRows) {
    return Slices.slice(dataset, numberOfRows, dataset.size());
  }
  
  /**
   * Removes the given number of rows from the tail of the dataset.
   * 
   * @param dataset the {@link Dataset} whose tail should be removed.
   * @param numberOfRows the number of rows to remove from the tail of the dataset.
   * @return a new {@link Dataset}, with the desired number of rows removed.
   */
  @Doc("Removes the given number of rows from the tail of the dataset")
  public static Dataset removeTail(
      @Doc("a dataset") Dataset dataset, 
      @Doc("number of rows") int numberOfRows) {
    return Slices.slice(dataset, 0, dataset.size() - numberOfRows);
  }
  
  /**
   * Removes the given percentage of rows from the top of the dataset.
   * 
   * @param dataset the {@link Dataset} whose top should be removed.
   * @param percentage a percentage (between 0 and 1, inclusively) of the dataset.
   * @return a new {@link Dataset}, with the top removed.
   */
  @Doc("Removes a percentage of the data in the given dataset from the top of it")  
  public static Dataset removeTop(
    @Doc("a dataset") Dataset dataset, 
    @Doc("a percentage") double percentage) {
    Checks.isTrue(percentage >= 0 && percentage <= 1, "Percentage must be between 0 and 1, inclusively. Got: %s", percentage);
    int numberOfRows = (int) (percentage * (double) dataset.size());
    return removeHead(dataset, numberOfRows);
  }
  
  /**
   * Removes the given percentage of rows from the bottom of the dataset.
   * 
   * @param dataset the {@link Dataset} whose bottom should be removed.
   * @param percentage a percentage (between 0 and 1, inclusively) of the dataset.
   * @return a new {@link Dataset}, with the bottom removed.
   */
  @Doc("Removes a percentage of the data in the given dataset from the bottom of it")
  public static Dataset removeBottom(
    @Doc("a dataset") Dataset dataset, 
    @Doc("a percentage") double percentage) {
    Checks.isTrue(percentage >= 0 && percentage <= 1, "Percentage must be between 0 and 1, inclusively. Got: %s", percentage);
    int numberOfRows = (int) (percentage * (double) dataset.size());
    return removeTail(dataset, numberOfRows);
  }
  
  /**
   * @param dataset the {@link Dataset} from which to select a subset.
   * @param expression the MVEL expression to use as criteria.
   * @return a new {@link Dataset} holding the rows that matched the given criteria.
   */
  @Doc(value = "Selects a subset of the given dataset, using the provided filter expression",
       examples=  {
         @Example(caption = "Selecting all that is greated than a given value", content = "salary >= 1000" )
       })
  public static Dataset select(
    @Doc("a dataset from which to select a subset of data") final Dataset dataset, 
    @Doc("a filter expression") final String expression) {
    
    final Map<String, Object> context  = new HashMap<>();
    final Serializable        compiled = MVEL.compileExpression(expression);
    
    return dataset.getSubset(new Criteria<RowResult>() {
      
      @Override
      public boolean matches(RowResult v) {
        context.clear();
        for (String colName : dataset.getColumnSet().getColumnNames()) {
          context.put(colName, v.get(colName).getInternalValue());
        }
        Object returnValue = MVEL.executeExpression(compiled, context);
        if (returnValue == null || !(returnValue instanceof Boolean)) {
          throw new IllegalArgumentException(
              String.format("Expression doest not evaluate to boolean: %s", expression)
          );
        } else {
          return ((Boolean) returnValue).booleanValue();
        }
      }
    });
  }
}
