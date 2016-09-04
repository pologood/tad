package org.sapia.tad.stat;

import org.sapia.tad.Column;
import org.sapia.tad.Datatype;
import org.sapia.tad.RowSet;
import org.sapia.tad.Vector;
import org.sapia.tad.computation.Computation;
import org.sapia.tad.computation.ComputationResult;
import org.sapia.tad.computation.ComputationResults;
import org.sapia.tad.func.NoArgFunction;
import org.sapia.tad.value.NumericValue;

/**
 * A {@link Computation} that computes the maximum for dataset columns.
 * 
 * @author yduchesne
 *
 */
public class MaxComputation implements Computation {
  
  @Override
  public void compute(ComputationResults context, RowSet rows) {
    
    NoArgFunction<MaxValue> maxFunc = new NoArgFunction<MaxValue>() {
      @Override
      public MaxValue call() {
        return new MaxValue();
      }
    };
    
    ComputationResult maxResult = context.get(Stats.MAX);
    
    for (Vector row : rows) {
      for (Column col : context.getColumnSet()) {
        if (col.getType() == Datatype.NUMERIC) {
          MaxValue maxVal = maxResult.get(col, maxFunc);
          maxVal.set(NumericValue.doubleOrZero(row.get(col.getIndex())).get());
        }
      }
    }
  }
}
