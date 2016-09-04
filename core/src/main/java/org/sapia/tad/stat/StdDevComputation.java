package org.sapia.tad.stat;

import org.sapia.tad.Column;
import org.sapia.tad.RowSet;
import org.sapia.tad.computation.Computation;
import org.sapia.tad.computation.ComputationResult;
import org.sapia.tad.computation.ComputationResults;
import org.sapia.tad.value.NumericValue;

/**
 * Computes the mean, variance, and standard deviation.
 * 
 * @author yduchesne
 *
 */
public class StdDevComputation implements Computation {
  
  @Override
  public void compute(ComputationResults context, RowSet rows) {
    
    ComputationResult varResults  = context.get(Stats.VARIANCE);
    ComputationResult stdResults  = context.get(Stats.STDDEV);
    
    // ------------------------------------------------------------------------
    // computing stddev
    
    for (Column col : varResults.getColumnSet()) {
      stdResults.set(col, new NumericValue(Math.sqrt(varResults.get(col).get())));
    }
    
  }

}
