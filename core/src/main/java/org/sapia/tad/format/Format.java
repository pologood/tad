package org.sapia.tad.format;

import org.sapia.tad.Datatype;
import org.sapia.tad.Vector;
import org.sapia.tad.value.Value;

/**
 * Abstracts formatting of {@link Vector} array.
 * 
 * @author yduchesne
 *
 */
public interface Format {
  
  /**
   * @param colName
   * @return a formatter column header.
   */
  public String formatHeader(String colName);
  
  /**
   * @param datatype the {@link Datatype} of the the given value.
   * @param value a {@link Value} to format.
   * @return the formatted {@link String}.
   */
  public String formatValue(Datatype datatype, Value value);

}
