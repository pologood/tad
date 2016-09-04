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
 * A {@link Computation} that computes the minimum for dataset columns.
 * 
 * @author yduchesne
 *
 */
public class MinComputation implements Computation {
  
  @Override
  public void compute(ComputationResults context, RowSet rows) {
    
    NoArgFunction<MinValue> minFunc = new NoArgFunction<MinValue>() {
      @Override
      public MinValue call() {
        return new MinValue();
      }
    };
    
    ComputationResult minResult = context.get(Stats.MIN);
    
    for (Vector row : rows) {
      for (Column col : context.getColumnSet()) {
        if (col.getType() == Datatype.NUMERIC) {
          MinValue minVal = minResult.get(col, minFunc);
          minVal.set(NumericValue.doubleOrZero(row.get(col.getIndex())).get());
        }
      }
    }
  }
}
