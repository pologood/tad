package org.sapia.tad.ml.error;

import org.sapia.tad.Vector;
import org.sapia.tad.util.Checks;
import org.sapia.tad.value.NumericValue;
import org.sapia.tad.value.Value;

/**
 * Implements the MSE error algorithm.
 *
 * @author yduchesne
 */
public class MseCumulativeError implements CumulativeError {

  private int    observations;
  private double[] total;


  @Override
  public CumulativeError accumulate(Vector idealOutput, Vector actualOutput) {
    Checks.isTrue(idealOutput.size() == actualOutput.size(), "Vectors do not have same length. Got %s vs %s", idealOutput.size(), actualOutput.size());
    if (total == null) {
      total = new double[idealOutput.size()];
    }
    observations++;
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
    if (observations > 0) {
      double error = 0;
      for (int i = 0; i < total.length; i++) {
        error += total[i];
      }
      return 1d / observations * error;
    }
    return 0;
  }
}
