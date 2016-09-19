package org.sapia.tad.computation;

import org.sapia.tad.ColumnSet;
import org.sapia.tad.RowSet;
import org.sapia.tad.concurrent.ConcurrencyException;
import org.sapia.tad.util.Time;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * A {@link ComputationTask} that executes its registered {@link Computation}s 
 * in parallel.
 * 
 * @author yduchesne
 *
 */
public class ConcurrentComputationTask implements ComputationTask {
  
  private ExecutorService   executor;
  private List<Computation> computations = new ArrayList<>();

  /**
   * @param executor the {@link ExecutorService} to use to perform the computations.
   */
  public ConcurrentComputationTask(ExecutorService executor) {
    this.executor = executor;
  }
  
  @Override
  public ComputationTask add(Computation computation) {
    computations.add(computation);
    return this;
  }
  
  @Override
  public ComputationResults compute(ColumnSet columns, RowSet rows) throws InterruptedException {
    List<Future<ComputationResults>> resultsList = new ArrayList<>(computations.size());
    for (Computation c : computations) {
      resultsList.add(submit(columns, rows, c));
    }
    ComputationResults aggregated = ComputationResults.newInstance(columns);
    for (Future<ComputationResults> futureResults : resultsList) {
      try {
        aggregated.mergeWith(futureResults.get());
      } catch (ExecutionException e) {
        throw new ConcurrencyException("Error occured awaiting computation result", e);
      }
    }    
    return aggregated;
  }

  private Future<ComputationResults> submit(final ColumnSet columns, final RowSet rows, final Computation computation) {  
    Future<ComputationResults> future = executor.submit(new Callable<ComputationResults>() {
      @Override
      public ComputationResults call() throws Exception {
        ComputationResults results = ComputationResults.newInstance(columns);
        computation.compute(results, rows);
        return results;
      }
    });
    return future;
  }
}
