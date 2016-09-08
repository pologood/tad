package org.sapia.tad.ml.distance;

/**
 * A convenient factory for the different distance functions.
 *
 * @author yduchesne
 */
public class DistanceFunctions {

  private DistanceFunctions() {
  }

  /**
   * @return a new {@link ChebyshevDistanceFunction} instance.
   */
  public static DistanceFunction chebyshev() {
    return new ChebyshevDistanceFunction();
  }

  /**
   * @return a new {@link CosineDistanceFunction} instance.
   */
  public static DistanceFunction cosine() {
    return new CosineDistanceFunction();
  }

  /**
   * @return a new {@link EuclidianDistanceFunction} instance.
   */
  public static DistanceFunction euclidian() {
    return new EuclidianDistanceFunction();
  }

  /**
   * @return a new {@link ManhattanDistanceFunction} instance.
   */
  public static DistanceFunction manhattan() {
    return new ManhattanDistanceFunction();
  }
}
