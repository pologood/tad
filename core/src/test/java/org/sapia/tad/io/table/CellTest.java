package org.sapia.tad.io.table;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CellTest {

  @Test
  public void testToStringMaxWidth() {
    Cell c = new Cell(0, "aaa", new ColumnStyle().width(2));
    assertEquals("aa", c.toString());
  }
  
  @Test
  public void testToString() {
    Cell c = new Cell(0, "aa", new ColumnStyle().alignLeft().width(3));
    assertEquals("aa ", c.toString());
  }

}
