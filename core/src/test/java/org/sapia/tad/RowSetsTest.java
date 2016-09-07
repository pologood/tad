package org.sapia.tad;

import org.junit.Before;
import org.junit.Test;
import org.sapia.tad.value.Value;
import org.sapia.tad.value.Values;

import static org.junit.Assert.assertEquals;

public class RowSetsTest {
  
  private RowSet rowset;
  
  @Before
  public void setUp() {

    rowset = RowSets.rowSet(new Value[][] {
       Values.array(1d, 2d, 3d),
       Values.array(1d, 2d, 3d)
    });
  }

  @Test
  public void testSumRowSet() {
    Vector sum = RowSets.sum(rowset);
    assertEquals(2d, sum.get(0).get(), 0);
    assertEquals(4d, sum.get(1).get(), 0);
    assertEquals(6d, sum.get(2).get(), 0);
  }

  @Test
  public void testSumRowSetWithStartVectorIndex() {
    Vector sum = RowSets.sum(1, rowset);
    assertEquals(0d, sum.get(0).get(), 0);
    assertEquals(4d, sum.get(1).get(), 0);
    assertEquals(6d, sum.get(2).get(), 0);
  }

}
