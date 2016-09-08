package org.sapia.tad.parser;

import org.sapia.tad.Vector;
import org.sapia.tad.value.Value;

/**
 * Specifies the behavior for converting {@link Vector} values out of string content.
 * 
 * @author yduchesne
 *
 */
public interface Parser {

  /**
   * @param content some content.
   * @return the parsed content.
   */
  public Value parse(String content);
}
