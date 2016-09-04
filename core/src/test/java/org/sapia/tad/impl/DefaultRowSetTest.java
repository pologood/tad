package org.sapia.tad.impl;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.sapia.tad.Vector;
import org.sapia.tad.util.Data;
import org.sapia.tad.util.Numbers;
import org.sapia.tad.value.NumericValue;
import org.sapia.tad.value.Values;

public class DefaultRowSetTest {
  
  private DefaultRowSet rowset;
  
  @Before
  public void setUp() {
    rowset = new DefaultRowSet(
        Data.list(
          (Vector) new DefaultVector(Values.array(0)),
          (Vector) new DefaultVector(Values.array(1)),
          (Vector) new DefaultVector(Values.array(2))
      )
    );
  }

  @Test
  public void testGet() {
    for (int index : Numbers.range(3)) {
      assertEquals(new NumericValue(index), rowset.get(index).get(0));
    }
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testGetIndexTooLarge() {
    rowset.get(3); 
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testGetIndexTooSmall() {
    rowset.get(-1); 
  }

  @Test
  public void testSize() {
    assertEquals(3, rowset.size());
  }

  @Test
  public void testIterator() {
    List<Vector> rows = Data.list(rowset.iterator());
    for (int index : Numbers.range(3)) {
      assertEquals(new NumericValue(index), rows.get(index).get(0));
    }
  }

}
