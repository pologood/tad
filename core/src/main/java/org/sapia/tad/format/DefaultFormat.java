package org.sapia.tad.format;

import org.sapia.tad.Datatype;
import org.sapia.tad.value.NumericValue;
import org.sapia.tad.value.Value;

import java.text.DecimalFormat;

/**
 * Holds formatting configuration.
 * 
 * @author yduchesne
 */
public class DefaultFormat implements Format {

  private static final double MAX_DOUBLE = 9999999.9999;
  
  private static final DecimalFormat STD_DECIMAL_FORMAT = new DecimalFormat("#######.####");
  
  private static final DecimalFormat EXT_DECIMAL_FORMAT = new DecimalFormat("0.###E0");
  
  
  @Override
  public String formatHeader(String colName) {
    return colName;
  }
  
  @Override
  public String formatValue(Datatype datatype, Value value) {
    if (value instanceof SelfFormattable) {
      return ((SelfFormattable) value).format(datatype, this);
    } else if (datatype == Datatype.NUMERIC || value.isNumeric()) {
      synchronized (STD_DECIMAL_FORMAT) {
        Value doubleVal = NumericValue.doubleOrZero(value);
        if (doubleVal.get() <= MAX_DOUBLE) {
          return STD_DECIMAL_FORMAT.format(doubleVal.get());
        } else {
          return EXT_DECIMAL_FORMAT.format(doubleVal.get());
        }
      }
    } else {
      return String.format("%s", value);
    }
  }
  
  /**
   * @return a new {@link DefaultFormat}.
   */
  public static final DefaultFormat getInstance() {
    return new DefaultFormat();
  }
}
