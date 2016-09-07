package org.sapia.tad.ml.distance;

import org.sapia.tad.Vector;
import org.sapia.tad.util.Checks;
import org.sapia.tad.value.NumericValue;
import org.sapia.tad.value.Value;

/**
 * Computes the so-called "manhattan distance" between 2 vectors.
 *
 * @author yduchesne
 */
public class ManhattanDistanceFunction implements DistanceFunction {

  @Override
  public double computeDistanceBetween(Vector v1, Vector v2) {
    Checks.isTrue(v1.size() == v2.size(), "Vectors do not have same length. Got %s vs %s", v1.size(), v2.size());
    double totalAbsDistance = 0;

    for (int i = 0; i < v1.size(); i++) {
      Value val1 = v1.get(i);
      Value val2 = v2.get(i);
      Value diff = NumericValue.difference(val1, val2);

      totalAbsDistance += Math.abs(diff.get());
    }

    return totalAbsDistance;
  }
}
