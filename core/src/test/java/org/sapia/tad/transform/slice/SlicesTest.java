package org.sapia.tad.transform.slice;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.sapia.tad.ColumnSet;
import org.sapia.tad.ColumnSets;
import org.sapia.tad.Dataset;
import org.sapia.tad.Datasets;
import org.sapia.tad.Datatype;
import org.sapia.tad.Vector;
import org.sapia.tad.Vectors;
import org.sapia.tad.conf.Conf;
import org.sapia.tad.impl.DefaultDataset;
import org.sapia.tad.util.Numbers;
import org.sapia.tad.value.NumericValue;

public class SlicesTest {
  
  private Dataset dataset;
  
  @Before
  public void setUp() {
    ColumnSet columns = ColumnSets.columnSet("col0", Datatype.NUMERIC, "col1", Datatype.STRING, "col2", Datatype.STRING);
    List<Vector> rows = new ArrayList<>(50);
    for (int i : Numbers.range(50)) {
      rows.add(Vectors.vector(new Integer(i), "s1", "s2"));
    }
    dataset = new DefaultDataset(columns, rows);
  }

  @Test
  public void testTop() {
    Dataset top = Slices.top(dataset, 0.1);
    assertEquals(5, top.size());
    for (int i : Numbers.range(0, 5)) {
      assertEquals(NumericValue.of(i), top.getRow(i).get(0));
    }
  }

  @Test
  public void testBottom() {
    Dataset bottom = Slices.bottom(dataset, 0.1);
    assertEquals(5, bottom.size());
    for (int i : Numbers.range(45, 49)) {
      assertEquals(NumericValue.of(i), bottom.getRow(i - 45).get(0));
    }
  }

  @Test
  public void testHead() {
    Dataset head = Slices.head(dataset);
    for (int i : Numbers.range(0, Conf.getHeadLength())) {
      assertEquals(NumericValue.of(i), head.getRow(i).get(0));
    }
  }

  @Test
  public void testTail() {
    Dataset tail = Slices.tail(dataset);
    for (int i : Numbers.range(dataset.size() - Conf.getTailLength(), 50)) {
      assertEquals(NumericValue.of(i), tail.getRow(i - 25).get(0));
    }
  }

  @Test
  public void testSlice() {
    Dataset slice = Slices.slice(dataset, 10, 20);
    for (int i : Numbers.range(10, 20)) {
      assertEquals(NumericValue.of(i), slice.getRow(i - 10).get(0));
    }
  }

  @Test
  public void testQuartile() {
    Dataset q1 = Slices.quartile(dataset, 1);
    assertEquals(NumericValue.of(0), q1.getRow(0).get(0));

    Dataset q2 = Slices.quartile(dataset, 2);
    assertEquals(NumericValue.of(12), q2.getRow(0).get(0));
    
    Dataset q3 = Slices.quartile(dataset, 3);
    assertEquals(NumericValue.of(24), q3.getRow(0).get(0));
    
    Dataset q4 = Slices.quartile(dataset, 4);
    assertEquals(NumericValue.of(36), q4.getRow(0).get(0));
    assertEquals(NumericValue.of(49), Datasets.lastRow(q4).get(0));
  }

  @Test
  public void testQuintile() {
    for (int n : Numbers.range(1, 6)) {
      Dataset quintile = Slices.quintile(dataset, n);
      assertEquals(NumericValue.of((n - 1) * 10), quintile.getRow(0).get(0));
    }
  }

}
