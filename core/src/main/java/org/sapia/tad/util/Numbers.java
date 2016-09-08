package org.sapia.tad.util;

import org.sapia.tad.help.Doc;
import org.sapia.tad.value.NumericValue;
import org.sapia.tad.value.Value;

/**
 * Provides number-related utility methods.
 * 
 * @author yduchesne
 */
public class Numbers {
  
  private Numbers() {
  }
  
  /**
   * @param from a "from" inclusive value.
   * @param to a "to" exclusive value.
   * @return the values of <code>int</code>s corresponding to the given range.
   */
  public static int[] range(int from, int to) {
    Checks.isTrue(from <= to, "Invalid values: 'from' must be lower than or equal to 'to'. Got %s and %s", from, to);
    int[] range = new int[to - from];
    for (int i = 0, val = from; i < range.length; i++) {
      range[i] = val++;
    }
    return range;
  }
  
  /**
   * @param from a "from" inclusive value.
   * @param to a "to" exclusive value.
   * @return the values of <code>double</code>s corresponding to the given range.
   */
  public static double[] range(double from, double to) {
    Checks.isTrue(from <= to, "Invalid values: 'from' must be lower than or equal to 'to'. Got %s and %s", from, to);
    double[] range = new double[(int) (to - from)];
    double val = from;
    for (int i = 0; i < range.length; i++) {
      range[i] = val;
    }
    return range;
  }

  /** 
   * @param to the upperbound value (exclusive) of the range to return.
   * @return the values of <code>int</code>s starting with 0 at the first position, and <code>to- 1</code>
   * at the last position.
   */
  @Doc("Returns an values of integers, given an (exclusive) upper limit")
  public static int[] range(int to) {
    return range(0, to);
  }
  
  /** 
   * @param to the upperbound value (exclusive) of the range to return.
   * @return the values of <code>double</code>s starting with 0 at the first position, and <code>to- 1</code>
   * at the last position.
   */
  public static double[] range(double to) {
    return range(0, to);
  }

  /**
   * @param value a <code>int</code> value.
   * @param repetitions the number of time to repeat it.
   * @return the values of <code>int</code>s resulting from the operation.
   */
  public static int[] repeat(int value, int repetitions) {
    int[] values = new int[repetitions];
    for (int i = 0; i < repetitions; i++) {
      values[i] = value;
    }
    return values;
  }
  
  /**
   * @param value a <code>int</code> value.
   * @param repetitions the number of time to repeat it.
   * @return the values of {@link Integer}s resulting from the operation.
   */
  public static Integer[] repeatInteger(int value, int repetitions) {
    Integer[] values = new Integer[repetitions];
    for (int i = 0; i < repetitions; i++) {
      values[i] = value;
    }
    return values;
  }
  
  /**
   * @param value a <code>double</code> value.
   * @param repetitions the number of time to repeat it.
   * @return the values of <code>double</code>s resulting from the operation.
   */
  public static double[] repeat(double value, int repetitions) {
    double[] values = new double[repetitions];
    for (int i = 0; i < repetitions; i++) {
      values[i] = value;
    }
    return values;
  }
  
  /**
   *
   * @param value a <code>double</code> value.
   * @param repetitions the number of time to repeat it.
   * @return the values of {@link Value}s resulting from the operation.
   */
  public static Value[] repeatDouble(double value, int repetitions) {
    Value[] values = new Value[repetitions];
    for (int i = 0; i < repetitions; i++) {
      values[i] = new NumericValue(value);
    }
    return values;
  }
 
} 
