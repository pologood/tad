package org.sapia.tad.value;

import org.sapia.tad.type.DataTypeStrategies;

/**
 * @author yduchesne
 */
public class Values {

  private Values() {
  }

  /**
   * @param objects some objects with which to create an array of {@link Value}s.
   * @return array of  {@link Value}s .
   */
  public static Value[] array(Object...objects) {
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
