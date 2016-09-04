package org.sapia.tad.stat;

import org.sapia.tad.Column;
import org.sapia.tad.Datatype;
import org.sapia.tad.RowSet;
import org.sapia.tad.Vector;
import org.sapia.tad.computation.Computation;
import org.sapia.tad.computation.ComputationResult;
import org.sapia.tad.computation.ComputationResults;
import org.sapia.tad.func.NoArgFunction;
import org.sapia.tad.value.NullValue;
import org.sapia.tad.value.NumericValue;

/**
 * Computes the mean for dataset columns.
 * 
 * @author yduchesne
 *
 */
public class MeanComputation implements Computation {
  
  @Override
  public void compute(ComputationResults context, RowSet rows) {
    
    ComputationResult meanResults = context.get(Stats.MEAN);
    
    // computing mean for each given column
    for (Vector row : rows) {
      for (Column col : meanResults.getColumnSet()) {
        MeanValue mean = meanResults.get(col, new NoArgFunction<MeanValue>() {
          @Override
          public MeanValue call() {
            return new MeanValue();
          }
        });
        if (col.getType() == Datatype.NUMERIC) {
          Object value = row.get(col.getIndex());
          if (NullValue.isNotNull(value)) {
            double colValue = NumericValue.doubleOrZero(row.get(col.getIndex())).get();
            mean.increase(colValue);
          }
        }
      }
    }
    
  }

}
