package org.sapia.tad.ml.error;

import org.junit.Before;
import org.junit.Test;
import org.sapia.tad.Vector;
import org.sapia.tad.Vectors;
import org.sapia.tad.util.Numbers;

import static org.junit.Assert.*;

/**
 * @author yduchesne
 */
public class RmsCumulativeErrorTest {

  private RmsCumulativeError error;

  @Before
  public void setUp() {
    error = new RmsCumulativeError();
  }

  @Test
  public void testGet() throws Exception {
    Vector total   = Vectors.withValues(Numbers.repeatDouble(0, 5));

    Vector ideal1  = Vectors.withRandomNumbers(0, 5, 5);
    Vector actual1 = Vectors.withRandomNumbers(0, 5, 5);
    Vector diff1   = ideal1.difference(actual1).pow(2);
    error.accumulate(ideal1, actual1);
    total = total.sum(diff1);

    Vector ideal2  = Vectors.withRandomNumbers(0, 5, 5);
    Vector actual2 = Vectors.withRandomNumbers(0, 5, 5);
    Vector diff2   = ideal2.difference(actual2).pow(2);
    error.accumulate(ideal2, actual2);
    total = total.sum(diff2);

    assertEquals(Math.sqrt(1d/2d * total.sum()), error.get(), 0);
  }
}