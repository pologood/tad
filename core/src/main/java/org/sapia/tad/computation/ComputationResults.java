package org.sapia.tad.computation;

import org.sapia.tad.*;
import org.sapia.tad.Vector;
import org.sapia.tad.conf.Conf;
import org.sapia.tad.impl.DefaultColumn;
import org.sapia.tad.impl.DefaultColumnSet;
import org.sapia.tad.impl.DefaultDataset;
import org.sapia.tad.impl.DefaultVector;
import org.sapia.tad.io.table.Header;
import org.sapia.tad.io.table.Row;
import org.sapia.tad.io.table.Table;
import org.sapia.tad.stat.MeanValue;
import org.sapia.tad.value.NullValue;
import org.sapia.tad.value.NumericValue;
import org.sapia.tad.value.StringValue;
import org.sapia.tad.value.Value;

import java.util.*;

/**
 * An instance of this class holds computation results on a per-computation type basis.
 * 
 * @author yduchesne
 *
 */
public class ComputationResults {
  
  private Map<String, ComputationResult> results      = new HashMap<>();
  private List<String>                   orderedNames = new ArrayList<>();
  private ColumnSet                      columns;

  /**
   * @param columns a {@link ColumnSet} consisting of the columns over which
   * computations should be performed.
   */
  public ComputationResults(ColumnSet columns) {
    this.columns = columns;
  }
  
  /**
   * @return this instance's {@link ColumnSet}.
   */
  public ColumnSet getColumnSet() {
    return columns;
  }

  /**
   * @param computationName the name of the "computation" whose corresponding
   * {@link ComputationResult} should be returned.
   * @return the {@link ComputationResult} associated to the given computation name.
   */
  public ComputationResult get(String computationName) {
    ComputationResult result = results.get(computationName);
    if (result == null) {
      result = new ComputationResult(columns);
      results.put(computationName, result);
      orderedNames.add(computationName);
    }
    return result;
  }
  
  /**
   * @return the {@link List} of result names corresponding to the {@link ComputationResult}
   * instances that this instances holds.
   */
  public List<String> getResultNames() {
    return Collections.unmodifiableList(orderedNames);
  }
  
  
  /**
   * Merges this instance with the {@link ComputationResults} passed in. That other result
   * will have its data merged into this instance.
   * 
   * @param other some other {@link ComputationResults} to merge into this instance.
   * @return this instance;
   */
  public ComputationResults mergeWith(ComputationResults other) {
    for (String resultName : other.getResultNames()) {
      ComputationResult otherResult = other.get(resultName);
      ComputationResult thisResult  = this.results.get(resultName);
      if (thisResult == null) { 
        this.results.put(resultName, otherResult);
        this.orderedNames.add(resultName);
      } else {
        for (Column col : columns) {
          Value otherValue = otherResult.get(col, NullValue.getInstance());
          Value thisValue  = thisResult.get(col, NullValue.getInstance());
          Value meanOfBoth = new MeanValue().increase(otherValue.get()).increase(thisValue.get());
          thisResult.set(col, meanOfBoth);
        }
      }
    }
    return this;
  }
  
  /**
   * @param columns a {@link ColumnSet} consisting of the columns over which
   * computations should be performed.
   * 
   * @return a new {@link ComputationResults}.
   */
  public static ComputationResults newInstance(ColumnSet columns) {
    return new ComputationResults(columns);
  }
  
  /**
   * Returns a dataset holding this results for this instance's corresponding computations.
   * <p>
   * Each row in the dataset will hold the result of a computation, for each column array in that row.
   * 
   * @return a Dataset holding this instance's results.
   */
  public Dataset dataset() {
    List<Column> resultCols = new ArrayList<>();
    int index = 1;
    
    resultCols.add(new DefaultColumn(0, Datatype.STRING, "result_type"));
    for (Column col : columns) {
      resultCols.add(col.copy(index++));
    }

    ColumnSet resultColSet = new DefaultColumnSet(resultCols);
    
    List<Vector> rows = new ArrayList<>(results.size());
    for (String resultName : orderedNames) {
      Value[] rowValues = new Value[resultCols.size()];
      rowValues[0] = new StringValue(resultName);
      ComputationResult result = results.get(resultName);
      for (Column col : result.getColumnSet()) {
        if (col.getType() != Datatype.NUMERIC) {
          rowValues[col.getIndex() + 1] = NullValue.getInstance();
        } else {
          rowValues[col.getIndex() + 1] = result.get(col);
        }
      }
      rows.add(new DefaultVector(rowValues));
    }

    return new DefaultDataset(resultColSet, rows);
  }
    
  @Override
  public String toString() {
    Table table = Table.obj();
    table.header("", Conf.getCellWidth()).getStyle().alignRight();
    for (Column col : columns) {
      Header h = table.header(col.getFormat().formatHeader(col.getName()), Conf.getCellWidth());
      h.getStyle().alignRight();
    }
    for (String resultName : orderedNames) {
      Row row = table.row();
      row.cell(resultName + ": ");
      ComputationResult result = results.get(resultName);
      for (Column col : result.getColumnSet()) {
        if (col.getType() != Datatype.NUMERIC) {
          row.cell(col.getFormat().formatValue(col.getType(), NumericValue.zero()));
        } else {
          row.cell(col.getFormat().formatValue(col.getType(), result.get(col)));
        }
      }
    }
    return table.toString();
  }
}
