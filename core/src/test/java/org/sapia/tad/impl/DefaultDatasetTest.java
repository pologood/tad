package org.sapia.tad.impl;

import org.junit.Before;
import org.junit.Test;
import org.sapia.tad.ColumnSet;
import org.sapia.tad.Dataset;
import org.sapia.tad.Datatype;
import org.sapia.tad.Vector;
import org.sapia.tad.util.Data;
import org.sapia.tad.util.Numbers;
import org.sapia.tad.value.StringValue;
import org.sapia.tad.value.Values;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class DefaultDatasetTest {
  
  private DefaultDataset dataset;
  
  @Before
  public void setUp() {
    dataset = new DefaultDataset(
        Data.list(
            new DefaultColumn(0, Datatype.STRING, "col0"),
            new DefaultColumn(1, Datatype.STRING, "col1"),
            new DefaultColumn(2, Datatype.STRING, "col2")
        ),
        Data.list(
            new DefaultVector(Values.array("00", "01", "02")),
            new DefaultVector(Values.array("10", "11", "12")),
            new DefaultVector(Values.array("20", "21", "22"))
        )
    );
    
  }

  @Test
  public void testGetColumnSet() {
    ColumnSet cols = dataset.getColumnSet();
    assertEquals(3, cols.size());
    for (int i : Numbers.range(3)) {
      assertEquals(i, cols.get(i).getIndex());
      assertEquals("col" + i, cols.get(i).getName());
    }
  }

  @Test
  public void testGetRow() {
    for (int i : Numbers.range(3)) {
      Vector row = dataset.getRow(i);
      for (int j : Numbers.range(2)) {
        StringValue expected = new StringValue(new StringBuilder().append(i).append(j).toString());
        assertEquals(expected, row.get(j));
      }
    }
  }

  @Test
  public void testSize() {
    assertEquals(dataset.size(), 3);
  }

  @Test
  public void testGetColumnForName() {
    Vector column = dataset.getColumn("col1");
    assertEquals(3, column.size());
    for (int i : Numbers.range(3)) {
      StringValue expected = new StringValue(new StringBuilder().append(i).append("1").toString());
      assertEquals(expected, column.get(i));
    }
  }

  @Test
  public void testGetColumnForIndex() {
    Vector column = dataset.getColumn(1);
    assertEquals(3, column.size());
    for (int i : Numbers.range(3)) {
      StringValue expected = new StringValue(new StringBuilder().append(i).append("1").toString());
      assertEquals(expected, column.get(i));
    }
  }

  @Test
  public void testGetColumnSubsetForIndex() {
    Dataset subset = dataset.getColumnSubset(1, value ->
            value.toString().equals("11") || value.toString().equals("21")
    );
  
    assertEquals("col1", subset.getColumnSet().get(0).getName());
    assertEquals(2, subset.size());
    
    for (int i : Numbers.range(0, 2)) {
      StringValue expected = new StringValue(new StringBuilder().append(i + 1).append("1").toString());
      assertEquals(expected, subset.getRow(i).get(0));
    }
  }
  
  @Test
  public void testGetColumnSubsetForName() {
    Dataset subset = dataset.getColumnSubset("col1", value ->
      value.toString().equals("11") || value.toString().equals("21")
    );
  
    assertEquals("col1", subset.getColumnSet().get(0).getName());
    assertEquals(2, subset.size());
    
    for (int i : Numbers.range(0, 2)) {
      StringValue expected = new StringValue(new StringBuilder().append(i + 1).append("1").toString());
      assertEquals(expected, subset.getRow(i).get(0));
    }
  }

  @Test
  public void testGetSubset() {
    Dataset subset = dataset.getSubset(v -> v.get(1).toString().equals("11") || v.get(1).toString().equals("21"));
    
    assertEquals(2, subset.size());
    
    for (int i : Numbers.range(0, 2)) {
      StringValue expected = new StringValue(new StringBuilder().append(i + 1).append("1").toString());
      assertEquals(expected, subset.getRow(i).get(1));
    }
  }

  @Test
  public void testIterator() {
    List<Vector> rows = Data.list(dataset.iterator());
    assertEquals(3, rows.size());
    for (int i : Numbers.range(3)) {
      assertEquals(StringValue.of("" + i + "0"), rows.get(i).get(0));
    }
  }

  @Test
  public void testIndex() {
  }

}
