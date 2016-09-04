package org.sapia.tad.parser;


import org.sapia.tad.value.StringValue;
import org.sapia.tad.value.Value;

/**
 * Returns the given content.
 * 
 * @author yduchesne
 *
 */
public class StringParser implements Parser {
  
  @Override
  public Value parse(String content) {
    return new StringValue(content);
  }

}
