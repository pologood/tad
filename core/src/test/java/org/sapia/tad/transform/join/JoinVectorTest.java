package org.sapia.tad.transform.join;

import org.junit.Before;
import org.junit.Test;
import org.sapia.tad.Vector;
import org.sapia.tad.Vectors;
import org.sapia.tad.transform.join.VectorTable.VectorType;
import org.sapia.tad.util.Numbers;
import org.sapia.tad.value.NumericValue;

import static org.junit.Assert.assertEquals;

public class JoinVectorTest {
  
  private Vector left, right;
  private Vector join;

  @Before
  public void setUp() {
    left  = Vectors.withNumbers(0, 1, 2);
    right = Vectors.withNumbers(3, 4);
    join = new JoinVector(
        new VectorTable(
            new int[] {0, 1, 2, 0, 1}, 
            new VectorType[]{
                VectorType.LEFT,
                VectorType.LEFT,
                VectorType.LEFT,
                VectorType.RIGHT,
                VectorType.RIGHT
            }
        ),
        left, 
        right
    );
  }

  @Test
  public void testGet() {
    for (int i : Numbers.range(5)) {
      assertEquals(NumericValue.of(i), join.get(i));
    }
  }

  @Test
  public void testIterator() {
    int count = 0;
    for (Object value : join) {
      assertEquals(NumericValue.of(count++), value);
    }
    assertEquals(5, count);
  }

  @Test
  public void testSize() {
    assertEquals(5, join.size());
  }

  @Test
  public void testSubset() {
    Vector subset = join.subset(new int[]{3,4});
    assertEquals(NumericValue.of(3), subset.get(0));
    assertEquals(NumericValue.of(4), subset.get(1));
  }

  @Test
  public void testToArray() {
    Object[] values = join.toArray();
    for (int i : Numbers.range(5)) {
      assertEquals(join.get(i), values[i]);
    }
  }

}
