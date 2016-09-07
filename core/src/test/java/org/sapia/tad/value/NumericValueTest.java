package org.sapia.tad.value;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NumericValueTest {

  @Test
  public void testOf_double_value() throws Exception {
    Value v = NumericValue.of(2d);

    assertEquals(2.0, v.get(), 0);
  }

  @Test
  public void testOf_integer_object() throws Exception {
    Value v = NumericValue.of(new Integer(2));

    assertEquals(2.0, v.get(), 0);
  }

  @Test
  public void testZero() throws Exception {
    Value v = NumericValue.zero();

    assertEquals(0, v.get(), 0);
  }

  @Test
  public void testDoubleOrZero_with_string_value() throws Exception {
    Value v = NumericValue.doubleOrZero(StringValue.of("test"));

    assertEquals(0, v.get(), 0);
  }

  @Test
  public void testDoubleOrZero_with_numeric_value() throws Exception {
    Value v = NumericValue.doubleOrZero(NumericValue.of(2));

    assertEquals(2, v.get(), 0);
  }

  @Test
  public void testSum() throws Exception {
    Value v1 = NumericValue.of(2);
    Value v2 = NumericValue.of(3);

    assertEquals(NumericValue.of(5), NumericValue.sum(v1, v2));
  }

  @Test
  public void testSum_with_first_value_string() throws Exception {
    Value v1 = StringValue.of("test");
    Value v2 = NumericValue.of(3);

    assertEquals(NumericValue.of(3), NumericValue.sum(v1, v2));
  }

  @Test
  public void testSum_with_second_value_string() throws Exception {
    Value v2 = StringValue.of("test");
    Value v1 = NumericValue.of(3);

    assertEquals(NumericValue.of(3), NumericValue.sum(v1, v2));
  }

  @Test
  public void testDifference() throws Exception {
    Value v1 = NumericValue.of(2);
    Value v2 = NumericValue.of(3);

    assertEquals(NumericValue.of(-1), NumericValue.difference(v1, v2));
  }

  @Test
  public void testDifference_with_first_value_string() throws Exception {
    Value v1 = StringValue.of("test");
    Value v2 = NumericValue.of(3);

    assertEquals(NumericValue.of(-3), NumericValue.difference(v1, v2));
  }

  @Test
  public void testDifference_with_second_value_string() throws Exception {
    Value v2 = StringValue.of("test");
    Value v1 = NumericValue.of(3);

    assertEquals(NumericValue.of(3), NumericValue.difference(v1, v2));
  }

  @Test
  public void testProduct() throws Exception {
    Value v1 = NumericValue.of(2);
    Value v2 = NumericValue.of(3);

    assertEquals(NumericValue.of(6), NumericValue.product(v1, v2));
  }

  @Test
  public void testProduct_with_first_value_zero() throws Exception {
    Value v1 = NumericValue.of(0);
    Value v2 = NumericValue.of(3);

    assertEquals(NumericValue.of(0), NumericValue.product(v1, v2));
  }

  @Test
  public void testProduct_with_second_value_zero() throws Exception {
    Value v1 = NumericValue.of(3);
    Value v2 = NumericValue.of(0);

    assertEquals(NumericValue.of(0), NumericValue.product(v1, v2));
  }
}