package org.sapia.tad.ml.distance;

import org.junit.Before;
import org.junit.Test;
import org.sapia.tad.Vector;
import org.sapia.tad.Vectors;

import static org.junit.Assert.assertEquals;

public class ChebyshevDistanceFunctionTest {

  private ChebyshevDistanceFunction func;

  @Before
  public void setUp() {
    func = new ChebyshevDistanceFunction();
  }

  @Test
  public void testComputeDistanceBetween() throws Exception {
    Vector v1 = Vectors.vector(1, 1, 1);
    Vector v2 = Vectors.vector(4, 5, 6);

    double distance = func.computeDistanceBetween(v1, v2);

    assertEquals(5, distance, 0);
  }
}