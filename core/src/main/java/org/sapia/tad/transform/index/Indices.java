package org.sapia.tad.transform.index;

import org.sapia.tad.*;
import org.sapia.tad.computation.*;
import org.sapia.tad.concurrent.ThreadInterruptedException;
import org.sapia.tad.concurrent.Threading;
import org.sapia.tad.conf.Conf;
import org.sapia.tad.help.Doc;
import org.sapia.tad.impl.DefaultDataset;
import org.sapia.tad.impl.DefaultVector;
import org.sapia.tad.math.SumComputation;
import org.sapia.tad.stat.MaxComputation;
import org.sapia.tad.stat.MeanComputation;
import org.sapia.tad.stat.MinComputation;
import org.sapia.tad.value.NullValue;
import org.sapia.tad.value.Value;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides aggregate operations over {@link IndexedDataset}s.
 * 
 * @author yduchesne
 *
 */
public class Indices {

  private Indices() {
  }

  /**
   * Performs the given computation over the rows corresponding to the given indexed dataset's keys
   * and aggregates the results in a single row, for each key.
   * 
   * @param dataset an {@link IndexedDataset}.
   * @param computation a {@link Computation} to apply.
   * @return the {@link IndexedDataset} resulting from the aggregation.
   * @throws ThreadInterruptedException if the calling thread is interrupted while performing
   * this operation.
   */
  @Doc("Performs the given computation over each group of rows in the provided indexed dataset")
  public static IndexedDataset aggregate(IndexedDataset dataset, Computation computation) throws ThreadInterruptedException {
    
    ComputationTask task = Computations.parallel(Threading.getThreadPool(), Conf.getTaskTimeout());
    task.add(computation);
    List<Vector> aggregatedRows = new ArrayList<>();
    for (VectorKey k : dataset.getKeys()) {
      RowSet rows = dataset.getRowset(k);
      try {
        ComputationResults results = task.compute(dataset.getColumnSet(), rows);
        ComputationResult result = results.get(results.getResultNames().get(0));
        Value[] rowValues = new Value[dataset.getColumnSet().size()];
        for (Column c : dataset.getColumnSet()) {
          Value v = result.get(c, NullValue.getInstance());
          if (NullValue.isNull(v)) {
            rowValues[c.getIndex()] = null;
          } else {
            rowValues[c.getIndex()] = v;
          }
        }
        aggregatedRows.add(new DefaultVector(rowValues));
      } catch (InterruptedException e) {
        throw new ThreadInterruptedException(e);
      }
    }
    return new DefaultDataset(dataset.getColumnSet(), aggregatedRows).index(dataset.getIndexedColumnSet().getColumnNames());
  }
  
  /**
   * Performs an aggregated sum over the rows of this given dataset - on a per-index key basis.
   * <p>
   * This method internally calls {@link #aggregate(IndexedDataset, Computation)}.
   * 
   * @param dataset the {@link IndexedDataset} over which to perform an aggregated sum.
   * @return the {@link IndexedDataset} resulting from the aggregated sum operation.
   * @throws ThreadInterruptedException if the calling thread is interrupted while invoking this method.
   * @see #aggregate(IndexedDataset, Computation)
   */
  @Doc("Computes the sum for each group of rows in the provided indexed dataset")
  public static IndexedDataset sum(IndexedDataset dataset) throws ThreadInterruptedException {
    return aggregate(dataset, new SumComputation());
  }
  
  /**
   * Computes the average (mean) for the rows of this given dataset - on a per-index key basis.
   * <p>
   * This method internally calls {@link #aggregate(IndexedDataset, Computation)}.
   * 
   * @param dataset the {@link IndexedDataset} over which to perform an average.
   * @return the {@link IndexedDataset} resulting from the averaging operation.
   * @throws ThreadInterruptedException if the calling thread is interrupted while invoking this method.
   * @see #aggregate(IndexedDataset, Computation)
   */
  @Doc("Computes the average for each group of rows in the provided indexed dataset")
  public static IndexedDataset avg(IndexedDataset dataset) throws ThreadInterruptedException {
    return aggregate(dataset, new MeanComputation());
  }
  
  /**
   * Computes the minimum for the rows of this given dataset - on a per-index key basis.
   * <p>
   * This method internally calls {@link #aggregate(IndexedDataset, Computation)}.
   * 
   * @param dataset the {@link IndexedDataset} for which to compute the minimum.
   * @return the {@link IndexedDataset} resulting from the min computation.
   * @throws ThreadInterruptedException if the calling thread is interrupted while invoking this method.
   * @see #aggregate(IndexedDataset, Computation)
   */
  @Doc("Computes the min for each group of rows in the provided indexed dataset")
  public static IndexedDataset min(IndexedDataset dataset) throws ThreadInterruptedException {
    return aggregate(dataset, new MinComputation());
  }
  
  /**
   * Computes the maximum for the rows of this given dataset - on a per-index key basis.
   * <p>
   * This method internally calls {@link #aggregate(IndexedDataset, Computation)}.
   * 
   * @param dataset the {@link IndexedDataset} for which to compute the maximum.
   * @return the {@link IndexedDataset} resulting from the max computation.
   * @throws ThreadInterruptedException if the calling thread is interrupted while invoking this method.
   * @see #aggregate(IndexedDataset, Computation)
   */
  @Doc("Computes the max for each group of rows in the provided indexed dataset")
  public static IndexedDataset max(IndexedDataset dataset) throws ThreadInterruptedException {
    return aggregate(dataset, new MaxComputation());
  }
}
