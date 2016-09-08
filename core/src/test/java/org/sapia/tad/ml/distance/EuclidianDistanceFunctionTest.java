package org.sapia.tad.ml.distance;

import org.junit.Before;
import org.junit.Test;
import org.sapia.tad.Vector;
import org.sapia.tad.Vectors;

import static org.junit.Assert.assertEquals;

public class EuclidianDistanceFunctionTest {

  private EuclidianDistanceFunction func;

  @Before
  public void setUp() {
    func = new EuclidianDistanceFunction();
  }

  @Test
  public void testComputeDistanceBetween() throws Exception {
    Vector v1 = Vectors.withNumbers(1, 2, 3);
    Vector v2 = Vectors.withNumbers(4, 5, 6);

    double distance = func.computeDistanceBetween(v1, v2);

    double expected = Math.pow(1-4, 2) + Math.pow(2 - 5, 2) + Math.pow(3 - 6, 2);
    expected = Math.sqrt(expected);

    assertEquals(expected, distance, 0);
  }
}