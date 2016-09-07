package org.sapia.tad.ml.distance;

import org.sapia.tad.Vector;
import org.sapia.tad.util.Checks;
import org.sapia.tad.value.NumericValue;
import org.sapia.tad.value.Value;

/**
 * Computes the Chebyshev distance between 2 vectors.
 *
 * @author yduchesne
 */
public class ChebyshevDistanceFunction implements DistanceFunction {

  @Override
  public double computeDistanceBetween(Vector v1, Vector v2) {
    Checks.isTrue(v1.size() == v2.size(), "Vectors do not have same length. Got %s vs %s", v1.size(), v2.size());
    double result = 0;

    for (int i = 0; i < v1.size(); i++) {
      Value val1 = v1.get(i);
      Value val2 = v2.get(i);
      double diff = Math.abs(NumericValue.difference(val1, val2).get());
      result = Math.max(result, diff);
    }
    return result;
  }
}
