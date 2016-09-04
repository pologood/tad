package org.sapia.tad.stat;

import org.sapia.tad.computation.CompositeComputation;
import org.sapia.tad.computation.Computation;

/**
 * A composite {@link Computation} encapsulating a {@link MinComputation} and a {@link MaxComputation}.
 * 
 * @author yduchesne
 *
 */
public class MinMaxComputation extends CompositeComputation {

  public MinMaxComputation() {
    add(new MinComputation(), new MaxComputation());
  }
}
