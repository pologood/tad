package org.sapia.tad.value;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class MutableValueTest {

  private MutableNumericValue value;
  
  @Before
  public void setUp() {
    value = new MutableNumericValue();
    value.set(1);
  }
  
  @Test
  public void testGet() {
    assertEquals(1d, value.get(), 0);
  }

  @Test
  public void testIncrease() {
    value.increase(2);
    assertEquals(3d, value.get(), 0);
  }

  @Test
  public void testSet() {
    value.set(2);
    assertEquals(2d, value.get(), 0);
  }

  @Test
  public void testEqualsObject() {
    MutableNumericValue actual = new MutableNumericValue();
    actual.set(1);
    assertEquals(value, actual);
    actual.increase(2);
    assertNotSame(value, actual);
  }

}
