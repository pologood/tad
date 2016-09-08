package org.sapia.tad.ml.distance;

import org.junit.Before;
import org.junit.Test;
import org.sapia.tad.Vector;
import org.sapia.tad.Vectors;

import static org.junit.Assert.assertEquals;

public class CosineDistanceFunctionTest {

  private CosineDistanceFunction func;

  @Before
  public void setUp() {
    func = new CosineDistanceFunction();
  }

  @Test
  public void testComputeDistanceBetween() throws Exception {
    Vector v1 = Vectors.withNumbers(1, 2, 3);
    Vector v2 = Vectors.withNumbers(4, 5, 6);

    double distance = func.computeDistanceBetween(v1, v2);
    double expected = v1.product(v2) / (v1.norm() * v2.norm());

    assertEquals(expected, distance, 0);
  }
}