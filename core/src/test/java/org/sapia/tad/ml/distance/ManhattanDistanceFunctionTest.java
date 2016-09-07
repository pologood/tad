package org.sapia.tad.ml.distance;

import org.junit.Before;
import org.junit.Test;
import org.sapia.tad.Vector;
import org.sapia.tad.Vectors;

import static org.junit.Assert.assertEquals;

public class ManhattanDistanceFunctionTest {

  private ManhattanDistanceFunction func;

  @Before
  public void setUp() {
    func = new ManhattanDistanceFunction();
  }

  @Test
  public void testComputeDistanceBetween() throws Exception {
    Vector v1 = Vectors.vector(1, 2, 3);
    Vector v2 = Vectors.vector(4, 5, 6);

    double distance = func.computeDistanceBetween(v1, v2);
    double expected = Math.abs(1-4) + Math.abs(2 - 5) + Math.abs(3 - 6);

    assertEquals(expected, distance, 0);
  }
}