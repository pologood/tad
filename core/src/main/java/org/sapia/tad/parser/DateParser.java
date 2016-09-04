package org.sapia.tad.parser;

import org.sapia.tad.conf.Conf;
import org.sapia.tad.value.DateValue;
import org.sapia.tad.value.Value;

import java.text.DateFormat;
import java.text.ParseException;

/**
 * Parses date content.
 * 
 * @author yduchesne
 *
 */
public class DateParser implements Parser {
  
  @Override
  public Value parse(String content) {
    for (DateFormat format : Conf.getDateFormats()) {
      synchronized (format) {
        try {
          return new DateValue(format.parse(content));
        } catch (ParseException e) {
          // noop: trying next
        }
      }
    }
    throw new IllegalArgumentException("Could not parse date: " + content);
  }

}
