package org.sapia.tad.ml.error;

import org.sapia.tad.Vector;

/**
 * Specifies the behavior for accumulating error output and obtaining the final error value at the end of the
 * accumulation.
 *
 * @author yduchesne
 */
public interface CumulativeError {

  /**
   * @param idealOutput a {@link Vector} corresponding to an ideal output.
   * @param actualOutput a {@link Vector} corresponding to an actual output.
   * @return this instance.
   */
  public CumulativeError accumulate(Vector idealOutput, Vector actualOutput);

  /**
   * @return the error value.
   */
  public double get();
}
