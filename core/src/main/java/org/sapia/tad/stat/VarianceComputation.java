package org.sapia.tad.stat;

import org.sapia.tad.Column;
import org.sapia.tad.Datatype;
import org.sapia.tad.RowSet;
import org.sapia.tad.Vector;
import org.sapia.tad.computation.Computation;
import org.sapia.tad.computation.ComputationResult;
import org.sapia.tad.computation.ComputationResults;
import org.sapia.tad.func.NoArgFunction;
import org.sapia.tad.value.MutableNumericValue;
import org.sapia.tad.value.NullValue;
import org.sapia.tad.value.NumericValue;
import org.sapia.tad.value.Value;

/**
 * Computes variance for dataset columns.
 * 
 * @author yduchesne
 *
 */
public class VarianceComputation implements Computation {
  
  @Override
  public void compute(ComputationResults context, RowSet rows) {
    
    ComputationResult meanResults = context.get(Stats.MEAN);
    ComputationResult varResults  = context.get(Stats.VARIANCE);

    for (Vector row : rows) {
      for (Column col : varResults.getColumnSet()) {
        
        MutableNumericValue variance = (MutableNumericValue) varResults.get(col, new NoArgFunction<Value>() {
          @Override
          public Value call() {
            return new MutableNumericValue();
          }
        });

        if (col.getType() == Datatype.NUMERIC) {
          Object value = row.get(col.getIndex());
          if (NullValue.isNotNull(value)) {
            double meanValue = NumericValue.doubleOrZero(meanResults.get(col)).get();
            double colValue  = NumericValue.doubleOrZero(row.get(col.getIndex())).get();
            variance.increase(Math.pow(colValue - meanValue, 2));
          }
        }
      }
    }
    
  }

}
