package org.sapia.tad.value;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DoubleValueTest {

  @Test
  public void testDoubleValueNumber() {
    NumericValue value = new NumericValue(new Integer(1));
    assertEquals(1d, value.get(), 0);
  }
  
  @Test
  public void testDoubleValueNumberNull() {
    NumericValue value = new NumericValue(null);
    assertEquals(0d, value.get(), 0);
  }

  @Test
  public void testDoubleValueDouble() {
    NumericValue value =  new NumericValue(1);
    assertEquals(1d, value.get(), 0);
  }


  @Test
  public void testEqualsObject() {
    NumericValue expected = new NumericValue(1);
    NumericValue actual   = new NumericValue(1);
    assertEquals(expected, actual);
  }

}
