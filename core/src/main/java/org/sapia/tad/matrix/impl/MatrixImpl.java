package org.sapia.tad.matrix.impl;

import org.sapia.tad.matrix.Matrix;

/**
 * Abstract class for implementing {@link Matrix} types.
 *
 * @author yduchesne
 */
public abstract class MatrixImpl implements Matrix {

  @Override
  public String toString() {
    return MatrixImplHelper.toString(this);
  }
}
