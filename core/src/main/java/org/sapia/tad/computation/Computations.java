package org.sapia.tad.computation;

import org.sapia.tad.util.Time;

import java.util.concurrent.ExecutorService;

/**
 * Holds computation-related methods.
 * 
 * @author yduchesne
 *
 */
public class Computations {
  
  private Computations() {
  }

  /**
   * Returns a task that executes its registered computations in parallel.
   * 
   * @param executor the {@link ExecutorService} to be used by the returned task.
   * time given to perform all the task's computations.
   * 
   * @return a new {@link ComputationTask}.
   */
  public static ComputationTask parallel(ExecutorService executor) {
    return new ConcurrentComputationTask(executor);
  }
  
  /**
   * @return a {@link ComputationTask} that performs its computations in parallel.
   */
  public static ComputationTask sequential() {
    return new SequentialComputationTask();
  }
}
