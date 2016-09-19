package org.sapia.tad.impl;

import org.sapia.tad.Vector;
import org.sapia.tad.value.Value;

import java.util.Iterator;

/**
 * Abstract vector implementation.
 *
 * @author yduchesne
 */
public abstract class VectorImpl implements Vector {

  @Override
  public double innerProduct(Vector other) {
    return VectorImplHelper.innerProduct(this, other);
  }

  @Override
  public Vector product(Vector other) {
    return VectorImplHelper.product(this, other);
  }

  @Override
  public Vector product(Value value) {
    return VectorImplHelper.product(this, value);
  }

  @Override
  public Vector sum(Vector other) {
    return VectorImplHelper.sum(this, other);
  }

  @Override
  public Vector sum(Value value) {
    return VectorImplHelper.sum(this, value);
  }

  @Override
  public Vector difference(Vector other) {
    return VectorImplHelper.difference(this, other);
  }

  @Override
  public Vector difference(Value value) {
    return VectorImplHelper.difference(this, value);
  }

  @Override
  public Vector pow(double power) {
    return VectorImplHelper.pow(this, power);
  }

  @Override
  public double norm() {
    return VectorImplHelper.norm(this);
  }

  @Override
  public double sum() {
    return VectorImplHelper.sum(this);
  }

  @Override
  public Vector subset(int... indices) throws IllegalArgumentException {
    return VectorImplHelper.subset(this, indices);
  }

  @Override
  public Value[] toArray() {
    return VectorImplHelper.toArray(this);
  }

  @Override
  public Iterator<Value> iterator() {
    return VectorImplHelper.iterator(this);
  }

  @Override
  public boolean equals(Object obj) {
    return VectorImplHelper.equals(this, obj);
  }

  @Override
  public int hashCode() {
    return VectorImplHelper.hashCode(this);
  }

  @Override
  public String toString() {
    return VectorImplHelper.toString(this);
  }
}
