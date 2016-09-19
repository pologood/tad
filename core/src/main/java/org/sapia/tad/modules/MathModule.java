package org.sapia.tad.modules;

import org.sapia.tad.TadContext;
import org.sapia.tad.math.Sum;
import org.sapia.tad.util.Numbers;

/**
 * @author yduchesne.
 */
public class MathModule {

  public final Sum sums;
  public final Numbers numbers;

  public MathModule(TadContext context) {
    sums = new Sum(context);
    numbers = new Numbers();
  }
}
