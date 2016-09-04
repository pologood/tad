package org.sapia.tad.transform.view;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.sapia.tad.Column;
import org.sapia.tad.Datatype;
import org.sapia.tad.impl.DefaultColumn;

public class ViewColumnTest {
  
  private ViewColumn col;
  private Column     delegate;
  
  @Before
  public void setUp() {
    delegate = new DefaultColumn(1, Datatype.STRING, "test");
    col = new ViewColumn(2, delegate);
  }

  @Test
  public void testGetIndex() {
    assertEquals(2, col.getIndex());
  }

  @Test
  public void testGetName() {
    assertEquals(delegate.getName(), col.getName());
  }

  @Test
  public void testGetType() {
    assertEquals(delegate.getType(), col.getType());
  }

  @Test
  public void testGetFormat() {
    assertEquals(delegate.getFormat(), col.getFormat());
  }


  @Test
  public void testGetParser() {
    assertEquals(delegate.getParser(), col.getParser());
  }

}
