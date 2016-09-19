package org.sapia.tad.transform.filter;

import org.junit.Before;
import org.junit.Test;
import org.sapia.tad.*;
import org.sapia.tad.impl.DefaultDataset;
import org.sapia.tad.impl.DefaultVector;
import org.sapia.tad.util.Data;
import org.sapia.tad.util.Numbers;
import org.sapia.tad.value.NullValue;
import org.sapia.tad.value.NumericValue;
import org.sapia.tad.value.StringValue;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FiltersTest {

  private Tad tad;

  private Dataset dataset;
  
  @Before
  public void setUp() {
    tad = TestTad.get();
    ColumnSet columns = ColumnSets.columnSet("col0", Datatype.NUMERIC, "col1", Datatype.STRING, "col2", Datatype.STRING);
    List<Vector> rows = new ArrayList<>(50);
    for (int i : Numbers.range(50)) {
      rows.add(Vectors.with(new Integer(i), "s1", "s2"));
    }
    dataset = new DefaultDataset(columns, rows);
  }

  @Test
  public void testRemoveNullsDatasetForAllColumns() {
    ColumnSet columns = ColumnSets.columnSet("col0", Datatype.NUMERIC, "col1", Datatype.STRING, "col2", Datatype.STRING);
    List<Vector> rows = Data.list(
      Vectors.with(null, "s1"),
      Vectors.with("s0", null),
      Vectors.with("s0", "s1")
    );
        
    dataset = new DefaultDataset(columns, rows);    
    dataset = tad.xf.filters.removeNulls(dataset, Data.list("col0", "col1"));
    assertEquals(1, dataset.size());
    assertEquals(StringValue.of("s0"), dataset.getRow(0).get(0));
    assertEquals(StringValue.of("s1"), dataset.getRow(0).get(1));
    
    dataset = new DefaultDataset(columns, rows);    
    dataset = tad.xf.filters.removeNulls(dataset, "col0", "col1");
    assertEquals(1, dataset.size());
    assertEquals(StringValue.of("s0"), dataset.getRow(0).get(0));
    assertEquals(StringValue.of("s1"), dataset.getRow(0).get(1));
  }
  
  @Test
  public void testRemoveNullsDatasetForSomeColumns() {
    ColumnSet columns = ColumnSets.columnSet("col0", Datatype.NUMERIC, "col1", Datatype.STRING, "col2", Datatype.STRING);
    List<Vector> rows = Data.list(
      Vectors.with(null, "s1"),
      Vectors.with("s0", null),
      Vectors.with("s0", "s1")
    );
        
    dataset = new DefaultDataset(columns, rows);    
    dataset = tad.xf.filters.removeNulls(dataset, "col0");
    assertEquals(2, dataset.size());

    assertTrue(NullValue.isNull(dataset.getRow(0).get(1)));
    assertEquals(StringValue.of("s0"), dataset.getRow(0).get(0));
    assertEquals(StringValue.of("s0"), dataset.getRow(1).get(0));
    assertEquals(StringValue.of("s1"), dataset.getRow(1).get(1));
  }


  @Test
  public void testRemoveAnyNulls() {
    ColumnSet columns = ColumnSets.columnSet("col0", Datatype.NUMERIC, "col1", Datatype.STRING, "col2", Datatype.STRING);
    List<Vector> rows = Data.list(
      Vectors.with(null, "s1"),
      Vectors.with("s0", null),
      Vectors.with("s0", "s1")
    );
        
    dataset = new DefaultDataset(columns, rows);    
    dataset = tad.xf.filters.removeNulls(dataset, "col0");

    dataset = new DefaultDataset(columns, rows);    
    dataset = tad.xf.filters.removeNulls(dataset, "col0", "col1");
    assertEquals(1, dataset.size());
    assertEquals(StringValue.of("s0"), dataset.getRow(0).get(0));
    assertEquals(StringValue.of("s1"), dataset.getRow(0).get(1));
  }
  
  @Test
  public void testReplace() {
    ColumnSet columns = ColumnSets.columnSet("col0", Datatype.NUMERIC);
    List<Vector> rows = Data.list(
      Vectors.with(0),
      Vectors.with(1),
      Vectors.with(2)
    );    
    
    dataset = new DefaultDataset(columns, rows);
    
    dataset = tad.xf.filters.replace(dataset, Data.array("col0"), Datatype.STRING, in -> StringValue.of(in.get()));
    
    for (int i : Numbers.range(3)) {
      assertEquals(StringValue.of(new Double(i).toString()), dataset.getRow(i).get(0));
    }
  }
  
  @Test
  public void testReplaceWithNominal() {
    ColumnSet columns = ColumnSets.columnSet("col0", Datatype.STRING);
    List<Vector> rows = Data.list(
      Vectors.with("VAL0"),
      Vectors.with("VAL1"),
      Vectors.with("VAL2")
    );    
    
    dataset = new DefaultDataset(columns, rows);
    
    dataset = tad.xf.filters.replaceWithNominal(dataset, "col0");
    
    for (int i : Numbers.range(3)) {
      Nominal nominal = (Nominal) dataset.getRow(i).get(0);
      assertEquals("VAL" + i, nominal.getName());
      assertEquals(i, nominal.getValue());
    }
  }

  @Test
  public void testRemoveHead() {
    Dataset subset = tad.xf.filters.removeHead(dataset, 10);
    for (int i : Numbers.range(10, 50)) {
      assertEquals(NumericValue.of(i), subset.getRow(i - 10).get(0));
    }
  }

  @Test
  public void testRemoveTail() {
    Dataset subset = tad.xf.filters.removeTail(dataset, 10);
    for (int i : Numbers.range(0, 40)) {
      assertEquals(NumericValue.of(i), subset.getRow(i).get(0));
    }
  }

  @Test
  public void testRemoveTop() {
    Dataset subset = tad.xf.filters.removeTop(dataset, 0.1);
    for (int i : Numbers.range(5, 50)) {
      assertEquals(NumericValue.of(i), subset.getRow(i - 5).get(0));
    }
  }

  @Test
  public void testRemoveBottom() {
    Dataset subset = tad.xf.filters.removeBottom(dataset, 0.1);
    for (int i : Numbers.range(0, 45)) {
      assertEquals(NumericValue.of(i), subset.getRow(i).get(0));
    }
  }

  @Test
  public void testSelect() {
    Dataset subset = tad.xf.filters.select(dataset, "col0 >= 10 && col1 == 's1'");
    assertEquals(NumericValue.of(10), subset.getRow(0).get(0));
  }
  
  @Test
  public void testSelectWithNullValue() {
    ColumnSet columns = ColumnSets.columnSet("col0", Datatype.STRING);
    List<Vector> rows = new ArrayList<>();
    rows.add(Vectors.with("val0"));
    rows.add(new DefaultVector(new Object[] { null }));
    Dataset ds = new DefaultDataset(columns, rows);
    Dataset subset = tad.xf.filters.select(ds, "col0 != null");
    assertEquals(1, subset.size());
    
  }

}
