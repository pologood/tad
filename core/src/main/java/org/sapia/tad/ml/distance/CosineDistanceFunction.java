package org.sapia.tad.ml.distance;

import org.sapia.tad.Vector;
import org.sapia.tad.util.Checks;

/**
 * Implements the cosine distance function.
 *
 * @author yduchesne
 */
public class CosineDistanceFunction implements DistanceFunction {

  @Override
  public double computeDistanceBetween(Vector v1, Vector v2) {
    Checks.isTrue(v1.size() == v2.size(), "Vectors do not have same length. Got %s vs %s", v1.size(), v2.size());
    return v1.product(v2) / (v1.norm() * v2.norm());
  }
}
