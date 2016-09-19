package org.sapia.tad.impl;

import org.sapia.tad.Vector;

/**
 * Implements a {@link Vector} whose state can be reset to the original values it was given.
 *
 * @author yduchesne
 */
public class ResettableVector extends DefaultMutableVector {

  private Vector originVector;

  public ResettableVector(Vector originVector) {
    super(originVector.toArray());
    this.originVector = originVector;
  }

  /**
   * Resets this instance's state so that its values correspond to the one of the original vector from which
   * it was created.
   *
   * @return this instance.
   */
  public ResettableVector reset() {
    values = originVector.toArray();
    return this;
  }

  /**
   * @param originVector a {@link Vector} providing the values for the original state.
   * @return a new {@link ResettableVector}, with the provided vector serving as original state.
   */
  public static ResettableVector of(Vector originVector) {
    return new ResettableVector(originVector);
  }

}
