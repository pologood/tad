package org.sapia.tad.transform.formula;

import org.junit.Before;
import org.junit.Test;
import org.sapia.tad.*;
import org.sapia.tad.func.ArgFunction;
import org.sapia.tad.impl.DefaultColumn;
import org.sapia.tad.impl.DefaultColumnSet;
import org.sapia.tad.impl.DefaultDataset;
import org.sapia.tad.impl.DefaultVector;
import org.sapia.tad.util.Data;
import org.sapia.tad.util.Numbers;
import org.sapia.tad.value.NumericValue;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class FormulaDatasetTest {
  
  private Dataset dataset;
  private Dataset delegate;
  
  @Before
  public void setUp() {
    ColumnSet columns = ColumnSets.columnSet(Datatype.NUMERIC, "col0");
    List<Object> rows = new ArrayList<>();
    rows.addAll(Data.listOfInts(Numbers.range(10)));
    delegate = new DefaultDataset(columns, Data.transform(rows, new ArgFunction<Object, Vector>() {
      @Override
      public Vector call(Object arg) {
        return new DefaultVector(arg);
      }
    }));
    
    List<FormulaInfo> infos = new ArrayList<>();
    infos.add(new FormulaInfo(1, arg -> NumericValue.product(arg.get(0), NumericValue.of(2))));
    
    List<Column> formulaColumns = new ArrayList<>();
    formulaColumns.addAll(columns.getColumns());
    formulaColumns.add(new DefaultColumn(1, Datatype.NUMERIC, "col1"));
    
    dataset = new FormulaDataset(
        delegate, 
        new DefaultColumnSet(formulaColumns), 
        infos
    );
  }

  @Test
  public void testGetColumnString() {
    Vector col = dataset.getColumn("col1");
    for (int i : Numbers.range(dataset.size())) {
      assertEquals(NumericValue.of(i * 2), col.get(i));
    }
  }

  @Test
  public void testGetColumnInt() {
    Vector col = dataset.getColumn(1);
    for (int i : Numbers.range(dataset.size())) {
      assertEquals(NumericValue.of(i * 2), col.get(i));
    }
  }

  @Test
  public void testGetColumnSet() {
    assertEquals("col0", dataset.getColumnSet().get(0).getName());
    assertEquals("col1", dataset.getColumnSet().get(1).getName());
  }

  @Test
  public void testGetColumnSubsetIntCriteriaOfObject() {
    Dataset subset = dataset.getColumnSubset(1, v -> v.get() >= 5);
    
    for (int i : Numbers.range(5)) {
      assertEquals(NumericValue.of((i + 3) * 2), subset.getRow(i).get(0));
    }
  }

  @Test
  public void testGetColumnSubsetStringCriteriaOfObject() {
    Dataset subset = dataset.getColumnSubset("col1",  v -> v.get() >= 5);
    for (int i : Numbers.range(5)) {
      assertEquals(NumericValue.of((i + 3) * 2), subset.getRow(i).get(0));
    }
  }

  @Test
  public void testGetRow() {
    for (int i = 0; i < dataset.size(); i++) {
      Vector row = dataset.getRow(i);
      assertEquals(NumericValue.of(i * 2), row.get(1));
    }
  }

  @Test
  public void testGetSubset() {
    Dataset subset = dataset.getSubset(r -> r.get("col1").get() >= 5);
    
    for (int i : Numbers.range(5)) {
      assertEquals(NumericValue.of((i + 3) * 2), subset.getRow(i).get(1));
    }
  }

  @Test
  public void testHead() {    
  }

  @Test
  public void testTail() {
  }

  @Test
  public void testIndexStringArray() {
    IndexedDataset ds = dataset.index("col1");
    ds.getIndexedColumnSet().get("col1");
  }

  @Test
  public void testIndexListOfString() {
    IndexedDataset ds = dataset.index(Data.list("col1"));
    ds.getIndexedColumnSet().get("col1");
  }

  @Test
  public void testIterator() {
    int i = 0;
    for (Vector r : dataset) {
      i++;
    }
    assertEquals(i, dataset.size());
  }

  @Test
  public void testSize() {
    assertEquals(delegate.size(), dataset.size());
  }

}
