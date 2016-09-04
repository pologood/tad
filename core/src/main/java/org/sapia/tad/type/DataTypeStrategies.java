package org.sapia.tad.type;

import org.sapia.tad.DatatypeStrategy;

import java.util.Arrays;
import java.util.List;

/**
 * @author yduchesne.
 */
public class DataTypeStrategies {

  private static List<DatatypeStrategy> strategies = Arrays.asList(
          new DateDatatypeStrategy(),
          //new GeometryDatatypeStrategy(),
          new NumericDatatypeStrategy(),
          new StringDatatypeStrategy()
  );

  private DataTypeStrategies() {
  }

  public static DatatypeStrategy getDataTypeStrategyFor(Object value) {
    for (DatatypeStrategy s : strategies) {
      if (s.isAssignableFrom(value)) {
        return s;
      }
    }
    throw new IllegalArgumentException("Not datatype strategy found for: " + value);
  }
}
