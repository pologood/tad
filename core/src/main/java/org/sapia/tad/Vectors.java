package org.sapia.tad;

import org.sapia.tad.impl.DefaultVector;
import org.sapia.tad.util.Checks;
import org.sapia.tad.value.NaN;
import org.sapia.tad.value.NumericValue;
import org.sapia.tad.value.Value;
import org.sapia.tad.value.Values;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

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
   * @param min the minimum random value that should be generated.
   * @param max the maximum random value that should be generated.
   * @param len the length of the vector that should be returned.
   * @return a new {@link Vector}, fill with random integer (int) values.
   */
  public static Vector withRandomNumbers(int min, int max, int len) {
    Random random = new Random();
    int[] values = new int[len];
    for (int i = 0; i < values.length; i++) {
      values[i] = random.nextInt(max) + min;
    }
    return withNumbers(values);
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
   * Returns a vector containing the negative values of the vector passed in: use this method in the context
   * of matrix-vector subtraction (which is in fact a matrix-vector sum, involving the negative of the given vector).
   *
   * @param v a {@link Vector}.
   * @return a new {@link Vector}, holding the values that are the negative version of the values
   * if the given vector.
   */
  public static Vector negative(Vector v) {
    Value[] values = new Value[v.size()];
    for (int i = 0; i < values.length; i++) {
      Value val = v.get(i);
      if (val.isNumeric()) {
        values[i] = NumericValue.of(-val.get());
      } else {
        values[i] = val;
      }
    }
    return new DefaultVector(values);
  }

  /**
   * Returns a vector containing the inverse of the values of the vector passed in: use this method in the context
   * of matrix-vector division (which is in fact a matrix-vector multiplication, involving the inverse of the given vector).
   *
   * @param v a {@link Vector}.
   * @return a new {@link Vector}, holding the values that are the negative version of the values
   * if the given vector.
   */
  public static Vector inverse(Vector v) {
    Value[] values = new Value[v.size()];
    for (int i = 0; i < values.length; i++) {
      Value val = v.get(i);
      if (val.isNumeric()) {
        if (val.get() < 0) {
          values[i] = NaN.getInstance();
        } else {
          values[i] = NumericValue.of(1 / val.get());
        }
      } else if (val instanceof NaN) {
        values[i] = val;
      } else {
        values[i] = val;
      }
    }
    return new DefaultVector(values);
  }

}
