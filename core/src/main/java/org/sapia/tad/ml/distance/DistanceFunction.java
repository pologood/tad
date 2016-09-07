package org.sapia.tad.ml.distance;

import org.sapia.tad.Vector;

/**
 * Abstracts vector distance computation.
 *
 * @author yduchesne
 */
public interface DistanceFunction {

  /**
   * Computes the distance between 2 vectors.
   *
   * @param v1 a {@link Vector}.
   * @param v2 another {@link Vector}.
   * @return the distance that was computed.
   */
  public double computeDistanceBetween(Vector v1, Vector v2);
}
