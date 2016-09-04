package org.sapia.tad.parser;

import org.sapia.tad.util.Strings;
import org.sapia.tad.value.NumericValue;
import org.sapia.tad.value.Value;

/**
 * Parses numeric content.
 * 
 * @author yduchesne
 *
 */
public class NumericParser implements Parser {
  
  @Override
  public Value parse(String content) {
    if (Strings.isNullOrEmpty(content)) {
      return NumericValue.zero();
    }
    try {
      return new NumericValue(Double.parseDouble(content));
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException(String.format("Could not parse value: %s", content));
    }
  }
  
}
