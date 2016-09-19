package org.sapia.tad.impl;

import org.sapia.tad.Vector;
import org.sapia.tad.Vectors;
import org.sapia.tad.util.Checks;
import org.sapia.tad.util.Objects;
import org.sapia.tad.value.NumericValue;
import org.sapia.tad.value.Value;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Provides method to help implementing {@link Vector}s.
 */
public final class VectorImplHelper {

  private VectorImplHelper() {
    // noop
  }

  /**
   * Computes the inner product with another vector. This method will treat non-numeric values as 0.
   *
   * @param v1 a {@link Vector}
   * @param v2 another {@link Vector}
   * @return the scalar value corresponding to the inner product of this instance with the given vector.
   */
  public static double innerProduct(Vector v1, Vector v2) {
    Checks.isTrue(v1.size() == v2.size(), "Vectors do not have same length (%s vs %s)", v1.size(), v2.size());
    double product = 0;
    for (int i = 0; i < v1.size(); i++) {
      Value thisValue = v1.get(i);
      Value otherValue = v2.get(i);
      if (thisValue.isNumeric() && otherValue.isNumeric()) {
        product += thisValue.get() * otherValue.get();
      }
    }
    return product;
  }

  /**
   * Computes the product of a given vector with another. This method will treat non-numeric values as 0.
   * <p>
   * DO NOT mistaken this operation with the inner product: this method will do a multiplication of each corresponding
   * values in this vector and the vector provided as input, returning these values in a new vector.
   * </p>
   *
   * @param v1 a {@link Vector}
   * @param v2 another {@link Vector}
   * @return a new {@link Vector}, with each column holding the product of the corresponding values
   * in both vectors.
   */
  public static Vector product(Vector v1, Vector v2) {
    Checks.isTrue(v1.size() == v2.size(), "Vectors do not have same length (%s vs %s)", v1.size(), v2.size());
    Value[] product = new Value[v1.size()];
    for (int i = 0; i < v1.size(); i++) {
      Value thisValue = v1.get(i);
      Value otherValue = v2.get(i);
      product[i] = NumericValue.product(thisValue, otherValue);
    }
    return new DefaultVector(product);
  }

  /**
   * Computes the vector-scalar product between the given vector and value.
   *
   * @param v a {@link Vector}.
   * @param val a {@link Value} to multiply with the given vector.
   * @return a new {@link Vector}, holding the results of operation.
   */
  public static Vector product(Vector v, Value val) {
    Value[] product = new Value[v.size()];
    for (int i = 0; i < v.size(); i++) {
      Value thisValue = v.get(i);
      product[i] = NumericValue.product(thisValue, val);
    }
    return new DefaultVector(product);
  }

  /**
   * Performs the sum of a vector with another. This method will treat non-numeric values as 0.
   *
   * @param v1 a {@link Vector}
   * @param v2 another {@link Vector}
   * @return a new {@link Vector}, with each column holding the sum of the corresponding values
   * in this instance, and the given vector.
   */
  public static Vector sum(Vector v1, Vector v2) {
    Checks.isTrue(v1.size() == v2.size(), "Vectors do not have same length (%s vs %s)", v1.size(), v2.size());
    Value[] sum = new Value[v1.size()];
    for (int i = 0; i < v1.size(); i++) {
      Value thisValue = v1.get(i);
      Value otherValue = v2.get(i);
      sum[i] = NumericValue.sum(thisValue, otherValue);
    }
    return new DefaultVector(sum);
  }

  /**
   * Performs an element-wise sum of the values in the given vector with the value provided.
   *
   * @param v a {@link Vector}.
   * @param val a {@link Value} to add to each element in the given vector.
   * @return a new {@link Vector}, holding the results of the element-wise sum.
   */
  public static Vector sum(Vector v, Value val) {
    Value[] sum = new Value[v.size()];
    for (int i = 0; i < v.size(); i++) {
      Value thisValue = v.get(i);
      sum[i] = NumericValue.sum(thisValue, val);
    }
    return new DefaultVector(sum);
  }

  /**
   * Calculates the difference between a vector and another. This method will treat non-numeric values as 0.
   *
   * @param v1 a {@link Vector}
   * @param v2 another {@link Vector}
   * @return a new {@link Vector}, with each column holding the sum of the corresponding values
   * in this instance, and the given vector.
   */
  public static Vector difference(Vector v1, Vector v2) {
    Checks.isTrue(v1.size() == v2.size(), "Vectors do not have same length (%s vs %s)", v1.size(), v2.size());
    Value[] difference = new Value[v1.size()];
    for (int i = 0; i < v1.size(); i++) {
      Value thisValue = v1.get(i);
      Value otherValue = v2.get(i);
      difference[i] = NumericValue.difference(thisValue, otherValue);
    }
    return new DefaultVector(difference);
  }

  /**
   * Performs an element-wise subtraction of the value provided from the values in the given vector.
   *
   * @param v a {@link Vector}.
   * @param val a {@link Value} to subtract from each element in the given vector.
   * @return a new {@link Vector}, holding the results of the element-wise difference.
   */
  public static Vector difference(Vector v, Value val) {
    Value[] diff = new Value[v.size()];
    for (int i = 0; i < v.size(); i++) {
      Value thisValue = v.get(i);
      diff[i] = NumericValue.difference(thisValue, val);
    }
    return new DefaultVector(diff);
  }

  /**
   * Calculates the sum of the values in the given vector and returns it.
   * This method will treat non-numeric values as 0.
   *
   * @param v a {@link Vector}.
   * @return the sum of the vector's values.
   */
  public static double sum(Vector v) {
    double sum = 0;
    for (int i = 0; i < v.size(); i++) {
      Value val = v.get(i);
      if (val.isNumeric()) {
        sum += val.get();
      }
    }
    return sum;
  }

  /**
   * Raises the values in a vector to a given power. Returns the respective resulting values in a new vector.
   *
   * @param vector a {@link Vector} whose values should be raised to the given power.
   * @param power the power to raise the vector to.
   * @return a new {@link Vector}, holding the values of the given vector raised to the specified power.
   */
  public static Vector pow(Vector vector, double power) {
    Value[] pow = new Value[vector.size()];
    for (int i = 0; i < vector.size(); i++) {
      Value v = vector.get(i);
      if (v.isNumeric()) {
        pow[i] = NumericValue.of(Math.pow(v.get(), power));
      } else {
        pow[i] = NumericValue.zero();
      }
    }
    return new DefaultVector(pow);
  }

  /**
   * @param v a {@link Vector} whose norm is to be computed.
   * @return the norm of the given vector.
   */
  public static double norm(Vector v) {
    double total = 0;
    for (int i = 0; i < v.size(); i++) {
      Value val = v.get(i);
      if (val.isNumeric()) {
        total += Math.pow(val.get(), 2);
      }
    }
    return Math.sqrt(total);
  }

  /**
   * @param vector a {@link Vector}.
   * @return an {@link Iterator} over the given vector.
   */
  public static Iterator<Value> iterator(Vector vector) {
    return new Iterator<Value>() {
      int index = 0;
      @Override
      public boolean hasNext() {
        return index < vector.size();
      }

      @Override
      public Value next() {
        if (index >= vector.size()) {
          throw new NoSuchElementException();
        }
        return vector.get(index++);
      }
    };
  }

  public static Vector subset(Vector vector, int... indices) throws IllegalArgumentException {
    Value[] values = new Value[indices.length];
    for (int i = 0; i < indices.length; i++) {
      int index = indices[i];
      Value value = vector.get(index);
      values[i] = value;
    }
    return new DefaultVector(values);
  }

  public static Value[] toArray(Vector vector) {
    Value[] values = new Value[vector.size()];
    for (int i = 0; i < vector.size(); i++) {
      Value value = vector.get(i);
      values[i] = value;
    }
    return values;
  }

  public static boolean equals(Vector vector, Object obj) {
    if (obj instanceof Vector) {
      Vector other = (Vector) obj;
      if (vector.size() != other.size()) {
        return false;
      }
      for (int i = 0; i < vector.size(); i++) {
        if (!Objects.safeEquals(vector.get(i), other.get(i))) {
          return false;
        }
      }
      return true;
    }
    return false;
  }

  public static int hashCode(Vector v) {
    int hashCode = 0;
    for (int i = 0; i < v.size(); i++) {
      Value val = v.get(i);
      hashCode += Objects.safeHashCode(val) * Objects.PRIME;
    }
    return hashCode;
  }

  public static String toString(Vector vector) {
    StringBuilder s = new StringBuilder("[");
    for (int i = 0; i < vector.size(); i++) {
      if (i > 0) {
        s.append(",");
      }
      s.append(vector.get(i));
    }
    return s.append("]").toString();
  }
}
