package org.sapia.tad.stat;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MinValueTest {
  
  @Test
  public void testSet() {
    MinValue min = new MinValue();
    min.set(5);
    min.set(10);
    min.set(3);
    assertEquals(3d, min.get(), 0);
  }

}
