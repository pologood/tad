package org.sapia.tad;

import org.sapia.tad.impl.DefaultVector;
import org.sapia.tad.util.Checks;
import org.sapia.tad.value.NumericValue;
import org.sapia.tad.value.Value;
import org.sapia.tad.value.Values;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Vectors {

  private Vectors() {
  }
  
  /**
   * @param values the values with which to create a {@link Vector}.
   * @return the {@link Vector} that was created.
   */
  public static Vector withValues(Value...values) {
    return new DefaultVector(values);
  }

  /**
   * @param values a {@link List} of values.
   * @return the {@link Vector} that was created, holding the given values.
   */
  public static Vector withValues(List<Value> values) {
    return new DefaultVector(values);
  }

  /**
   * @param objects some objects with which to create a {@link Vector}.
   * @return the {@link Vector} that was created.
   */
  public static Vector with(Object...objects) {
    return new DefaultVector(Values.with(objects));
  }

  /**
   * @param values one or more double values to vectorize.
   * @return the {@link Vector} that was created using the given values.
   */
  public static Vector withNumbers(double...values) {
    return new DefaultVector(Values.withNumbers(values));
  }

  /**
   * @param values one or more of double values to vectorize.
   * @return the {@link Vector} that was created using the given values.
   */
  public static Vector withNumbers(int...values) {
    return new DefaultVector(Values.withNumbers(values));
  }

  /**
   * @param c the {@link Comparator} to use to sort the given vector's values.
   * @param toSort the {@link Vector} to sort.
   * @return a new {@link Vector}, with its values sorted.
   */
  public static Vector sort(Comparator<Value> c, Vector toSort) {
    Value[] values = toSort.toArray();
    Arrays.sort(values, c);
    return new DefaultVector(values);
  }

  /**
   * Computes the inner product with another vector. This method will treat non-numeric values as 0.
   *
   * @param v1 a {@link Vector}
   * @param v2 another {@link Vector}
   * @return the scalar value corresponding to the inner product of this instance with the given vector.
   */
  public static double product(Vector v1, Vector v2) {
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

}
