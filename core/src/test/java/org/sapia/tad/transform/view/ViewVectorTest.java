package org.sapia.tad.transform.view;

import org.junit.Before;
import org.junit.Test;
import org.sapia.tad.Vector;
import org.sapia.tad.Vectors;
import org.sapia.tad.util.Numbers;
import org.sapia.tad.value.NumericValue;
import org.sapia.tad.value.Value;

import static org.junit.Assert.assertEquals;

public class ViewVectorTest {
  
  private ViewVector vector;
  
  @Before
  public void setUp() {
    Vector delegate = Vectors.vector(0, 1, 2, 3, 4);
    vector = new ViewVector(new int[] {0, 2, 4}, delegate);
  }

  @Test
  public void testGet() {
    int[] expected = new int[] {0, 2, 4};
    for (int i : Numbers.range(2)) {
      assertEquals(NumericValue.of(expected[i]), vector.get(i));
    }
  }

  @Test
  public void testSize() {
    assertEquals(3, vector.size());
  }

  @Test
  public void testIterator() {
    int i = 0;
    int[] expected = new int[] {0, 2, 4};
    for (Value v : vector) {
      assertEquals(NumericValue.of(expected[i++]), v);
    }
  }

  @Test
  public void testSubset() {
    Vector subset = vector.subset(1);
    assertEquals(2.0, subset.get(0).get(), 0);
  }

  @Test
  public void testToArray() {
    Value[] values = vector.toArray();
    int[] expected = new int[] {0, 2, 4};
    for (int i : Numbers.range(vector.size())) {
      assertEquals((double) expected[i], values[i].get(), 0);
    }
  }

}
