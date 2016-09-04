package org.sapia.tad.transform.slice;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.sapia.tad.ColumnSet;
import org.sapia.tad.ColumnSets;
import org.sapia.tad.Dataset;
import org.sapia.tad.Datatype;
import org.sapia.tad.IndexedDataset;
import org.sapia.tad.Vector;
import org.sapia.tad.Vectors;
import org.sapia.tad.conf.Conf;
import org.sapia.tad.impl.DefaultDataset;
import org.sapia.tad.util.Data;
import org.sapia.tad.util.Numbers;
import org.sapia.tad.value.NumericValue;

public class SliceDatasetTest {
  
  private static int START = 5;
  private static int END   = 35;
  
  private SliceDataset slice;
  
  @Before 
  public void setUp() {
    ColumnSet columns = ColumnSets.columnSet("col0", Datatype.NUMERIC, "col1", Datatype.STRING, "col2", Datatype.STRING);
    List<Vector> rows = new ArrayList<>(50);
    for (int i : Numbers.range(50)) {
      rows.add(Vectors.vector(new Integer(i), "s1", "s2"));
    }
    slice = new SliceDataset(new DefaultDataset(columns, rows), START, END);
  }

  @Test
  public void testGetColumnSet() {
    slice.getColumnSet().contains("col0", "col1", "col2");
  }

  @Test
  public void testGetColumnInt() {
    Vector vector = slice.getColumn(0);
    for (int i : Numbers.range(0, 5)) {
      assertEquals(new NumericValue(i + 5), vector.get(i));
    }
  }

  @Test
  public void testGetColumnString() {
    Vector vector = slice.getColumn("col0");
    for (int i : Numbers.range(0, 5)) {
      assertEquals(new NumericValue(i + 5), vector.get(i));
    }
  }

  @Test
  public void testGetColumnSubsetIntCriteriaOfObject() {
    Dataset subset = slice.getColumnSubset(0, value -> value.get() >= 8);
    for (int i : Numbers.range(0, 2)) {
      assertEquals(new NumericValue(i + 8), subset.getRow(i).get(0));
    }
  }

  @Test
  public void testGetColumnSubsetStringCriteriaOfObject() {
    Dataset subset = slice.getColumnSubset("col0", value -> value.get() >= 8);
    for (int i : Numbers.range(0, 2)) {
      assertEquals(new NumericValue(i + 8), subset.getRow(i).get(0));
    }
  }

  @Test
  public void testGetSubset() {
    Dataset subset = slice.getSubset(rowResult -> rowResult.get(0).get() >= 8);
    for (int i : Numbers.range(0, 2)) {
      assertEquals(new NumericValue(i + 8), subset.getRow(i).get(0));
    }
  }

  @Test
  public void testGetRow() {
    for (int i = 0; i < slice.size(); i++) {
      assertEquals(new NumericValue(START + i), slice.getRow(i).get(0));
    }
  }

  @Test
  public void testHead() {
    Dataset head = slice.head();
    assertEquals(Conf.getHeadLength(), head.size());
  }

  @Test
  public void testTail() {
    Dataset tail = slice.tail();
    assertEquals(Conf.getTailLength(), tail.size());    
  }

  @Test
  public void testIndexStringArray() {
    IndexedDataset indexed = slice.index("col0");
    assertEquals(slice.size(), indexed.size());
  }

  @Test
  public void testIndexListOfString() {
    IndexedDataset indexed = slice.index(Data.list("col0"));
    assertEquals(slice.size(), indexed.size());
  }

  @Test
  public void testSize() {
    assertEquals(END - START, slice.size());
  }

}
