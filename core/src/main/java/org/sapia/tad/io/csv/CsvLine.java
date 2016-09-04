package org.sapia.tad.io.csv;

import org.sapia.tad.func.ArgFunction;
import org.sapia.tad.io.helpers.Line;
import org.sapia.tad.util.Checks;
import org.sapia.tad.util.Strings;


public class CsvLine implements Line {
  
  private String[] line;
  
  public CsvLine(String[] line) {
    this.line = line;
  }
  
  @Override
  public String get(int index) {
    Checks.bounds(
        index, 
        line, 
        "Invalid index %s. Got %s array in line: %s",
        index, 
        line.length, 
        Strings.toString(line, new ArgFunction<String, String>() {
          public String call(String arg) {
            return arg;
          }
        })
    );
    return line[index];
  }
  
  @Override
  public int length() {
    return line.length;
  }
  
  @Override
  public String toString() {
    return Strings.toString(this.line, new ArgFunction<String, String>() {
      @Override
      public String call(String arg) {
        return arg;
      }
    });
  }
  

}
