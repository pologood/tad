package org.sapia.tad.stat;

import org.junit.Before;
import org.junit.Test;
import org.sapia.tad.value.NumericValue;

import static org.junit.Assert.assertEquals;

public class MeanValueTest {
  
  private MeanValue value;
  
  @Before
  public void setUp() {
    value = new MeanValue();
  }

  @Test
  public void testIncreaseAndGet() {
    value.increase(6);
    value.increase(4);
    assertEquals(5d, value.get(), 0);
  }

  @Test
  public void testGetWhenCountZero() {
    value.set(5);
    assertEquals(0d, value.get(), 0);
  }

  @Test
  public void testEqualsObject() {
    value.increase(2);
    assertEquals(new NumericValue(2), value);
  }

}
