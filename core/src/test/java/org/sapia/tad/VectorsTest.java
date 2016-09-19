package org.sapia.tad;

import org.junit.Before;
import org.junit.Test;
import org.sapia.tad.impl.DefaultVector;
import org.sapia.tad.util.Numbers;
import org.sapia.tad.value.NumericValue;
import org.sapia.tad.value.Values;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class VectorsTest {

  private Vector vector;

  @Before
  public void setUp() {
    vector = new DefaultVector(Values.with(0, 1, 2, 3));
  }

  @Test
  public void testVector_from_value_array() throws Exception {
    for (int i : Numbers.range(0, 4)) {
      assertEquals(NumericValue.of(i), vector.get(i));
    }
  }

  @Test
  public void testVector_from_objects() throws Exception {
    vector = Vectors.withNumbers(0, 1, 2, 3);
    for (int i : Numbers.range(0, 4)) {
      assertEquals(NumericValue.of(i), vector.get(i));
    }
  }

  @Test
  public void testVector_from_value_list() throws Exception {
    vector = Vectors.withValues(
       Arrays.asList(
            NumericValue.of(0),
            NumericValue.of(1),
            NumericValue.of(2),
            NumericValue.of(3)
      )
    );
    for (int i : Numbers.range(0, 4)) {
      assertEquals(NumericValue.of(i), vector.get(i));
    }
  }

  @Test
  public void testSort() throws Exception {
    vector = Vectors.withNumbers(3, 2, 1 ,0);
    vector = Vectors.sort((v1, v2) -> {
      if (v1.get() == v2.get()) {
        return 0;
      }  else if (v1.get() < v2.get()) {
        return -1;
      }
      return 1;
    }, vector);

    for (int i : Numbers.range(0, 4)) {
      assertEquals(NumericValue.of(i), vector.get(i));
    }
  }

  @Test
  public void testInnerProduct() {
    Vector otherVector = Vectors.withNumbers(4, 5, 6, 7);
    double product = vector.innerProduct(otherVector);
    assertEquals(0 * 4 + 1 * 5 + 2 * 6 + 3 * 7, product, 0);
  }

  @Test
  public void testSum() {
    Vector otherVector = Vectors.withNumbers(4, 5, 6, 7);
    Vector sum = vector.sum(otherVector);
    assertEquals(4, sum.get(0).get(), 0);
    assertEquals(6, sum.get(1).get(), 0);
    assertEquals(8, sum.get(2).get(), 0);
    assertEquals(10, sum.get(3).get(), 0);
  }

  @Test
  public void testNorm() {
    double expected = Math.sqrt(Math.pow(1, 2) + Math.pow(2, 2) + Math.pow(3, 2));
    assertEquals(expected, vector.norm(), 0);
  }
}