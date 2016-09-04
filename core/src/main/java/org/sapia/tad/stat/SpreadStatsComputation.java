package org.sapia.tad.stat;

import org.sapia.tad.computation.CompositeComputation;
import org.sapia.tad.computation.Computation;

/**
 * A composite {@link Computation} that Computes the mean, variance, and standard deviation.
 * 
 * @see MeanComputation
 * @see VarianceComputation
 * @see StdDevComputation 
 *  
 * @author yduchesne
 *
 */
public class SpreadStatsComputation extends CompositeComputation {

  public SpreadStatsComputation() {
    add(new MeanComputation(), new VarianceComputation(), new StdDevComputation());
  }

}
