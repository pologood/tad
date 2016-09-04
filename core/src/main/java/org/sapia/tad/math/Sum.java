package org.sapia.tad.math;

import org.sapia.tad.Dataset;
import org.sapia.tad.computation.ComputationResults;
import org.sapia.tad.computation.ComputationTask;
import org.sapia.tad.computation.Computations;
import org.sapia.tad.concurrent.Threading;
import org.sapia.tad.help.Doc;
import org.sapia.tad.impl.DatasetRowSetAdapter;
import org.sapia.tad.stat.MedianComputation;
import org.sapia.tad.stat.MinMaxComputation;
import org.sapia.tad.stat.SpreadStatsComputation;

import java.util.List;

/**
 * Holds methods for performing arithmetic computations over datasets.
 * 
 * @author yduchesne
 *
 */
@Doc("Holds methods for performing arithmetic computations over datasets")
public class Sum {

  private Sum() {
  }
  
  /**
   * Adds summary stats computations with the given {@link ComputationTask}.
   * 
   * @param task a {@link ComputationTask}.
   * @see SpreadStatsComputation
   * @see MedianComputation
   * @see MinMaxComputation
   */
  @Doc("Internally registers sum computation with the given task")
  public static void sum(ComputationTask task) {
    task.add(new SumComputation());
  }
  
  /**
   * @param dataset the {@link Dataset} for which to perform sum computation.
   * @param columnNames the names of the columns to use.
   * @return the {@link ComputationResults}.
   * @throws IllegalArgumentException if an invalid argument has been passed.
   * @throws InterruptedException if the calling thread is interrupted while waiting
   * for the end of the summary computation.
   */
  @Doc("Computes a sum for the given dataset - and for the specified columns")
  public static ComputationResults sum(@Doc("a dataset") Dataset dataset, @Doc("the column names") List<String> columnNames) 
      throws IllegalArgumentException, InterruptedException {
    return sum(dataset, columnNames.toArray(new String[columnNames.size()]));
  }
  
  /**
   * @param dataset the {@link Dataset} for which to perform sum computation.
   * @param columnNames the names of the columns to use.
   * @return the {@link ComputationResults}.
   * @throws IllegalArgumentException if an invalid argument has been passed.
   * @throws InterruptedException if the calling thread is interrupted while waiting
   * for the end of the summary computation.
   */
  @Doc("Computes a sum for the given dataset - and for the specified columns")
  public static ComputationResults sum(@Doc("a dataset") Dataset dataset, @Doc("the column names") String...columnNames) 
      throws IllegalArgumentException, InterruptedException {
    ComputationTask task = Computations.parallel(Threading.getThreadPool(), Threading.getTimeout());
    sum(task);
    return task.compute(dataset.getColumnSet().includes(columnNames), new DatasetRowSetAdapter(dataset));
  }

  /**
   * @param dataset the dataset for which to perform sum computation.
   * @return the {@link ComputationResults}.
   * @throws IllegalArgumentException if an invalid argument has been passed.
   * @throws InterruptedException if the calling thread is interrupted while waiting
   * for the end of the summary computation.
   */
  @Doc("Computes a sum for all columns in the given dataset")
  public static ComputationResults sum(@Doc("a dataset") Dataset dataset) 
      throws IllegalArgumentException, InterruptedException {
    ComputationTask task = Computations.parallel(Threading.getThreadPool(), Threading.getTimeout());
    sum(task);
    return task.compute(dataset.getColumnSet(), new DatasetRowSetAdapter(dataset));
  }
}
