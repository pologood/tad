package org.sapia.tad.value;

import org.sapia.tad.type.DataTypeStrategies;

/**
 * @author yduchesne
 */
public class Values {

  private Values() {
  }

  public static Value[] withNumbers(double...doubleValues) {
    Value[] values = new Value[doubleValues.length];
    for (int i = 0; i < values.length; i++) {
      values[i] = NumericValue.of(doubleValues[i]);
    }
    return values;
  }

  public static Value[] withNumbers(int...intValues) {
    Value[] values = new Value[intValues.length];
    for (int i = 0; i < values.length; i++) {
      values[i] = NumericValue.of(intValues[i]);
    }
    return values;
  }

  /**
   * @param objects some objects with which to create an values of {@link Value}s.
   * @return an array of {@link Value}s .
   */
  public static Value[] with(Object...objects) {
    Value[] values = new Value[objects.length];
    for (int i = 0; i < values.length; i++) {
      if (objects[i] instanceof Value) {
        values[i] = (Value) objects[i];
      } else if (objects[i] == null) {
        values[i] = NullValue.getInstance();
      } else {
        values[i] = DataTypeStrategies.getDataTypeStrategyFor(objects[i]).getValueFor(objects[i]);
      }
    }
    return values;
  }
}
