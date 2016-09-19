package org.sapia.tad.computation;

import org.sapia.tad.ColumnSet;
import org.sapia.tad.RowSet;

/**
 * A {@link ComputationTask} implementations whose instance perform their
 * computations sequentially, in the order in which they where added.
 * 
 * @author yduchesne
 *
 */
public class SequentialComputationTask implements ComputationTask {

  private CompositeComputation computation = new CompositeComputation();
  
  @Override
  public ComputationTask add(Computation computation) {
    this.computation.add(computation);
    return this;
  }
  
  @Override
  public ComputationResults compute(ColumnSet columns, RowSet rows)
      throws InterruptedException {
    ComputationResults results = ComputationResults.newInstance(columns);
    computation.compute(results, rows);
    return results;
  }
}