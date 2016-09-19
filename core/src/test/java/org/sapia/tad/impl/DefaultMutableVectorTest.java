package org.sapia.tad.impl;

import org.junit.Before;
import org.junit.Test;
import org.sapia.tad.value.NumericValue;
import org.sapia.tad.value.Values;

import static org.junit.Assert.*;

/**
 * @author yduchesne
 */
public class DefaultMutableVectorTest {

  private DefaultMutableVector vector;

  @Before
  public void setUp() {
    vector = new DefaultMutableVector(Values.with(0, 1, 2, 3));
  }

  @Test
  public void testSet_object() throws Exception {
    vector.set(1, 10);
    assertEquals(Values.of(10), vector.get(1));
  }

  @Test
  public void testSet_value() throws Exception {
    vector.set(1, NumericValue.of(10));
    assertEquals(Values.of(10), vector.get(1));

  }
}