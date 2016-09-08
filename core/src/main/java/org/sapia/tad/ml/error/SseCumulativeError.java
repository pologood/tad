package org.sapia.tad.ml.error;

import org.sapia.tad.Vector;
import org.sapia.tad.Vectors;
import org.sapia.tad.util.Checks;
import org.sapia.tad.value.NumericValue;
import org.sapia.tad.value.Value;

/**
 * Implements SSE error computation.
 *
 * @author yduchesne
 */
public class SseCumulativeError implements CumulativeError {

  private double[] total;

  @Override
  public CumulativeError accumulate(Vector idealOutput, Vector actualOutput) {
    Checks.isTrue(idealOutput.size() == actualOutput.size(), "Vectors do not have same length. Got %s vs %s", idealOutput.size(), actualOutput.size());
    if (total == null) {
      total = new double[idealOutput.size()];
    }
    for (int i = 0; i < idealOutput.size(); i++) {
      Value ideal  = idealOutput.get(i);
      Value actual = actualOutput.get(i);
      Value diff = NumericValue.difference(ideal, actual);
      total[i] += Math.pow(diff.get(), 2);
    }
    return this;
  }

  @Override
  public double get() {
    return Vectors.withNumbers(total).norm();
  }
}
