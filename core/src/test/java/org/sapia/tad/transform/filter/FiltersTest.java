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
  public void testRemoveNullsDatasetForAllColumns() {
    ColumnSet columns = ColumnSets.columnSet("col0", Datatype.NUMERIC, "col1", Datatype.STRING, "col2", Datatype.STRING);
    List<Vector> rows = Data.list(
      Vectors.vector(null, "s1"),
      Vectors.vector("s0", null),
      Vectors.vector("s0", "s1")
    );
        
    dataset = new DefaultDataset(columns, rows);    
    dataset = Filters.removeNulls(dataset, Data.list("col0", "col1"));
    assertEquals(1, dataset.size());
    assertEquals(StringValue.of("s0"), dataset.getRow(0).get(0));
    assertEquals(StringValue.of("s1"), dataset.getRow(0).get(1));
    
    dataset = new DefaultDataset(columns, rows);    
    dataset = Filters.removeNulls(dataset, "col0", "col1");
    assertEquals(1, dataset.size());
    assertEquals(StringValue.of("s0"), dataset.getRow(0).get(0));
    assertEquals(StringValue.of("s1"), dataset.getRow(0).get(1));
  }
  
  @Test
  public void testRemoveNullsDatasetForSomeColumns() {
    ColumnSet columns = ColumnSets.columnSet("col0", Datatype.NUMERIC, "col1", Datatype.STRING, "col2", Datatype.STRING);
    List<Vector> rows = Data.list(
      Vectors.vector(null, "s1"),
      Vectors.vector("s0", null),
      Vectors.vector("s0", "s1")
    );
        
    dataset = new DefaultDataset(columns, rows);    
    dataset = Filters.removeNulls(dataset, "col0");
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
      Vectors.vector(null, "s1"),
      Vectors.vector("s0", null),
      Vectors.vector("s0", "s1")
    );
        
    dataset = new DefaultDataset(columns, rows);    
    dataset = Filters.removeNulls(dataset, "col0");

    dataset = new DefaultDataset(columns, rows);    
    dataset = Filters.removeNulls(dataset, "col0", "col1");
    assertEquals(1, dataset.size());
    assertEquals(StringValue.of("s0"), dataset.getRow(0).get(0));
    assertEquals(StringValue.of("s1"), dataset.getRow(0).get(1));
  }
  
  @Test
  public void testReplace() {
    ColumnSet columns = ColumnSets.columnSet("col0", Datatype.NUMERIC);
    List<Vector> rows = Data.list(
      Vectors.vector(0),
      Vectors.vector(1),
      Vectors.vector(2)
    );    
    
    dataset = new DefaultDataset(columns, rows);
    
    dataset = Filters.replace(dataset, Data.array("col0"), Datatype.STRING, in -> StringValue.of(in.get()));
    
    for (int i : Numbers.range(3)) {
      assertEquals(StringValue.of(new Double(i).toString()), dataset.getRow(i).get(0));
    }
  }
  
  @Test
  public void testReplaceWithNominal() {
    ColumnSet columns = ColumnSets.columnSet("col0", Datatype.STRING);
    List<Vector> rows = Data.list(
      Vectors.vector("VAL0"),
      Vectors.vector("VAL1"),
      Vectors.vector("VAL2")
    );    
    
    dataset = new DefaultDataset(columns, rows);
    
    dataset = Filters.replaceWithNominal(dataset, "col0");
    
    for (int i : Numbers.range(3)) {
      Nominal nominal = (Nominal) dataset.getRow(i).get(0);
      assertEquals("VAL" + i, nominal.getName());
      assertEquals(i, nominal.getValue());
    }
  }

  @Test
  public void testRemoveHead() {
    Dataset subset = Filters.removeHead(dataset, 10);
    for (int i : Numbers.range(10, 50)) {
      assertEquals(NumericValue.of(i), subset.getRow(i - 10).get(0));
    }
  }

  @Test
  public void testRemoveTail() {
    Dataset subset = Filters.removeTail(dataset, 10);
    for (int i : Numbers.range(0, 40)) {
      assertEquals(NumericValue.of(i), subset.getRow(i).get(0));
    }
  }

  @Test
  public void testRemoveTop() {
    Dataset subset = Filters.removeTop(dataset, 0.1);
    for (int i : Numbers.range(5, 50)) {
      assertEquals(NumericValue.of(i), subset.getRow(i - 5).get(0));
    }
  }

  @Test
  public void testRemoveBottom() {
    Dataset subset = Filters.removeBottom(dataset, 0.1);
    for (int i : Numbers.range(0, 45)) {
      assertEquals(NumericValue.of(i), subset.getRow(i).get(0));
    }
  }

  @Test
  public void testSelect() {
    Dataset subset = Filters.select(dataset, "col0 >= 10 && col1 == 's1'");
    assertEquals(NumericValue.of(10), subset.getRow(0).get(0));
  }
  
  @Test
  public void testSelectWithNullValue() {
    ColumnSet columns = ColumnSets.columnSet("col0", Datatype.STRING);
    List<Vector> rows = new ArrayList<>();
    rows.add(Vectors.vector("val0"));
    rows.add(new DefaultVector(new Object[] { null }));
    Dataset ds = new DefaultDataset(columns, rows);
    Dataset subset = Filters.select(ds, "col0 != null");
    assertEquals(1, subset.size());
    
  }

}
