package org.sapia.tad.stat;

import org.sapia.tad.Dataset;
import org.sapia.tad.Vector;
import org.sapia.tad.Vectors;
import org.sapia.tad.computation.ComputationResult;
import org.sapia.tad.computation.ComputationResults;
import org.sapia.tad.computation.ComputationTask;
import org.sapia.tad.computation.Computations;
import org.sapia.tad.concurrent.Threading;
import org.sapia.tad.help.Doc;
import org.sapia.tad.impl.DatasetRowSetAdapter;
import org.sapia.tad.impl.DefaultDataset;
import org.sapia.tad.value.NumericValue;
import org.sapia.tad.value.Value;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds stats-related methods.
 *  
 * @author yduchesne
 *
 */
public class Stats {

  /**
   * Constant to which the mean {@link ComputationResult} is bound.
   */
  public static final String MEAN     = "mean";

  /**
   * Constant to which the variance {@link ComputationResult} is bound.
   */
  public static final String VARIANCE = "variance";
  
  /**
   * Constant to which the standard deviation {@link ComputationResult} is bound.
   */
  public static final String STDDEV   = "stddev";
  
  /**
   * Constant to which the min {@link ComputationResult} is bound.
   */
  public static final String MIN      = "min";
  
  /**
   * Constant to which the max {@link ComputationResult} is bound.
   */
  public static final String MAX      = "max";
  
  private Stats() {
  }

  /**
   * Adds summary stats computations with the given {@link ComputationTask}.
   * 
   * @param task a {@link ComputationTask}.
   * @see SpreadStatsComputation
   * @see MedianComputation
   * @see MinMaxComputation
   */
  @Doc("Internally registers summary stats computations with the given task")
  public static void summary(ComputationTask task) {
    task.add(new SpreadStatsComputation());
    task.add(new MedianComputation());
    task.add(new MinMaxComputation());
  }

  /**
   * @param dataset the dataset for which to perform summary stats computation.
   * @param columnNames the names of the columns to use.
   * @return the {@link ComputationResults}.
   * @throws IllegalArgumentException if an invalid argument has been passed.
   * @throws InterruptedException if the calling thread is interrupted while waiting
   * for the end of the summary computation.
   */
  @Doc("Computes summary statistics for the given dataset - and for the specified columns")
  public static ComputationResults summary(@Doc("a dataset") Dataset dataset, @Doc("the column names") List<String> columnNames) 
      throws IllegalArgumentException, InterruptedException {
    return summary(dataset, columnNames.toArray(new String[columnNames.size()]));
  }
  
  /**
   * @param dataset the dataset for which to perform summary stats computation.
   * @param columnNames the names of the columns to use.
   * @return the {@link ComputationResults}.
   * @throws IllegalArgumentException if an invalid argument has been passed.
   * @throws InterruptedException if the calling thread is interrupted while waiting
   * for the end of the summary computation.
   */
  @Doc("Computes summary statistics for the given dataset - and for the specified columns")
  public static ComputationResults summary(@Doc("a dataset") Dataset dataset, @Doc("the column names") String...columnNames) 
      throws IllegalArgumentException, InterruptedException {
    ComputationTask task = Computations.parallel(Threading.getThreadPool(), Threading.getTimeout());
    summary(task);
    return task.compute(dataset.getColumnSet().includes(columnNames), new DatasetRowSetAdapter(dataset));
  }

  /**
   * @param dataset the dataset for which to perform summary stats computation.
   * @return the {@link ComputationResults}.
   * @throws IllegalArgumentException if an invalid argument has been passed.
   * @throws InterruptedException if the calling thread is interrupted while waiting
   * for the end of the summary computation.
   */
  @Doc("Computes summary statistics for all columns in the given dataset")
  public static ComputationResults summary(@Doc("a dataset") Dataset dataset) 
      throws IllegalArgumentException, InterruptedException {
    ComputationTask task = Computations.parallel(Threading.getThreadPool(), Threading.getTimeout());
    summary(task);
    return task.compute(dataset.getColumnSet(), new DatasetRowSetAdapter(dataset));
  }

  @Doc("Performs unity-based normalization of a given dataset")
  public static Dataset normalize(@Doc("a dataset") Dataset input) {
    if (input.size() == 0) {
      return input;
    }

    Double[] minValues = new Double[input.getColumnSet().size()];
    Double[] maxValues = new Double[input.getColumnSet().size()];

    // finding min and max values.
    for (Vector v : input) {
      for (int i = 0; i < v.size(); i++) {
        Value toCheck = v.get(i);
        if (toCheck.isNumeric()) {
          if (minValues[i] == null || toCheck.get() < minValues[i]) {
            minValues[i] = toCheck.get();
          }
          if (maxValues[i] == null || toCheck.get() > maxValues[i]) {
            maxValues[i] = toCheck.get();
          }
        }
      }
    }

    List<Vector> normalizedVectors = new ArrayList<>(input.size());

    for (Vector v : input) {
      Value[] normalizedValues = new Value[input.getColumnSet().size()];
      for (int i = 0; i < v.size(); i++) {
        double min = minValues[i];
        double max = maxValues[i];
        Value toNormalize = v.get(i);
        if (toNormalize.isNumeric()) {
          normalizedValues[i] = NumericValue.of((toNormalize.get() - min) / (max - min));
        } else {
          normalizedValues[i] = NumericValue.zero();
        }
      }
      normalizedVectors.add(Vectors.withValues(normalizedValues));
    }

    return new DefaultDataset(input.getColumnSet(), normalizedVectors);
  }

}
