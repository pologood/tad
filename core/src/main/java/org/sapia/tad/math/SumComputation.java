package org.sapia.tad.math;

import org.sapia.tad.Column;
import org.sapia.tad.RowSet;
import org.sapia.tad.Vector;
import org.sapia.tad.computation.Computation;
import org.sapia.tad.computation.ComputationResult;
import org.sapia.tad.computation.ComputationResults;
import org.sapia.tad.func.NoArgFunction;

/**
 * Computes sums over dataset rows.
 * 
 * @author yduchesne
 *
 */
public class SumComputation implements Computation {
  
  @Override
  public void compute(ComputationResults context, RowSet rowSet) {
    ComputationResult sum = context.get("sum");
    
    NoArgFunction<SumValue> func = new NoArgFunction<SumValue>() {
      @Override
      public SumValue call() {
        return new SumValue();
      }
    };
    
    for (Vector row : rowSet) {
      for (Column col : context.getColumnSet()) {
        SumValue sumValue = sum.get(col, func);
        sumValue.add(row.get(col.getIndex()));
      }
    }
  }

}
