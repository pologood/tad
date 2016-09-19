package org.sapia.tad;

import org.sapia.tad.impl.DefaultMutableVector;

/**
 * @author yduchesne
 */
public final class MutableVectors {

  private MutableVectors() {
  }

  /**
   * @param other another {@link Vector}.
   * @return a copy of the given {@link Vector}.
   */
  public static MutableVector copyOf(Vector other) {
    return new DefaultMutableVector(other.toArray());
  }

}
