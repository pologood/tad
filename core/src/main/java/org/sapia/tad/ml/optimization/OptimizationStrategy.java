package org.sapia.tad.ml.optimization;

import org.sapia.tad.Vector;
import org.sapia.tad.matrix.Matrix;

/**
 * Specifies the behavior common to all optimization strategies.
 *
 * @author yduchesne
 */
public interface OptimizationStrategy {

  public Vector iterate(Vector ideal, Vector actual);
}
