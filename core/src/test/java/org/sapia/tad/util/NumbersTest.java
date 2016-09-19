package org.sapia.tad.util;

import org.junit.Test;
import org.sapia.tad.value.Value;

import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.*;

public class NumbersTest {

  @Test
  public void testRange_double() throws Exception {
    double[] values = Numbers.range(5d);
    assertThat(values).hasSize(5);
    for (int i = 0; i < values.length; i++) {
      assertEquals(i, values[i], 0);
    }
  }

  @Test
  public void testRange_double_from_to() throws Exception {
    double[] values = Numbers.range(5d, 10d);
    assertThat(values).hasSize(5);
    for (int i = 0; i < values.length; i++) {
      assertEquals(i+5, values[i], 0);
    }
  }

  @Test
  public void testRange_int() throws Exception {
    int[] values = Numbers.range(5);
    assertThat(values).hasSize(5);
    for (int i = 0; i < values.length; i++) {
      assertEquals(i, values[i]);
    }
  }

  @Test
  public void testRange_int_from_to() throws Exception {
    int[] values = Numbers.range(5, 10);
    assertThat(values).hasSize(5);
    for (int i = 0; i < values.length; i++) {
      assertEquals(i+5, values[i]);
    }
  }

  @Test
  public void testRepeat_double() throws Exception {
    double[] values = Numbers.repeat(5d, 5);
    assertThat(values).hasSize(5);
    for (int i = 0; i < values.length; i++) {
      assertEquals(5, values[i], 0);
    }
  }

  @Test
  public void testRepeat_int() throws Exception {
    int[] values = Numbers.repeat(5, 5);
    assertThat(values).hasSize(5);
    for (int i = 0; i < values.length; i++) {
      assertEquals(5, values[i]);
    }
  }

  @Test
  public void testRepeatDouble() throws Exception {
    Value[] values = Numbers.repeatDouble(5d, 5);
    assertThat(values).hasSize(5);
    for (int i = 0; i < values.length; i++) {
      assertEquals(5, values[i].get(), 0);
    }
  }

  @Test
  public void testRepeatInteger() throws Exception {
    Integer[] values = Numbers.repeatInteger(5, 5);
    assertThat(values).hasSize(5);
    for (int i = 0; i < values.length; i++) {
      assertEquals(5, values[i].intValue(), 0);
    }
  }

  @Test
  public void testRepeatRandom() throws Exception {
    int[] values = Numbers.repeatRandom(5, 20, 100);
    assertThat(values).hasSize(100);
    for (int i = 0; i < values.length; i++) {
      assertThat(values[i]).isBetween(5, 20);
    }
  }

  @Test
  public void testRandomSpread() throws Exception {

  }
}