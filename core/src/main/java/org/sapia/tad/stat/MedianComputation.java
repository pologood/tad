package org.sapia.tad.stat;

import org.sapia.tad.Column;
import org.sapia.tad.RowSet;
import org.sapia.tad.Vector;
import org.sapia.tad.computation.Computation;
import org.sapia.tad.computation.ComputationResult;
import org.sapia.tad.computation.ComputationResults;
import org.sapia.tad.value.NumericValue;
import org.sapia.tad.value.Value;

/**
 * Computes the median for dataset columns.
 * 
 * @author yduchesne
 *
 */
public class MedianComputation implements Computation {
  
  @Override
  public void compute(ComputationResults context, RowSet rows) {
    if (rows.size() > 0) {
      ComputationResult median = context.get("median");
      if (rows.size() % 2 == 0) {
        for (Column col : context.getColumnSet()) {
          double lower  = NumericValue.doubleOrZero(rows.get(rows.size() / 2 - 1).get(col.getIndex())).get();
          double higher = NumericValue.doubleOrZero(rows.get(rows.size() / 2).get(col.getIndex())).get();
          median.set(col, new MeanValue().increase(lower).increase(higher));
        }
      } else {
        for (Column col : context.getColumnSet()) {
          Vector medianRow   = rows.get(rows.size() / 2);
          Value  medianValue = NumericValue.doubleOrZero(medianRow.get(col.getIndex()));
          median.set(col, medianValue);
        }
      }
    }
  }

}
