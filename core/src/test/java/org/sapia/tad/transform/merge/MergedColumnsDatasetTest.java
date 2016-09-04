package org.sapia.tad.transform.merge;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.sapia.tad.Column;
import org.sapia.tad.Dataset;
import org.sapia.tad.Datatype;
import org.sapia.tad.IndexedDataset;
import org.sapia.tad.Vector;
import org.sapia.tad.impl.DefaultColumn;
import org.sapia.tad.impl.DefaultDataset;
import org.sapia.tad.impl.DefaultVector;
import org.sapia.tad.util.Data;
import org.sapia.tad.util.Numbers;
import org.sapia.tad.value.StringValue;
import org.sapia.tad.value.Values;

public class MergedColumnsDatasetTest {

  private Dataset d1, d2;
  private Dataset merged;
  
  @Before
  public void testSetup() {
    d1 = new DefaultDataset(
        Data.list(
            (Column) new DefaultColumn(0, Datatype.STRING, "col0"),
            (Column) new DefaultColumn(1, Datatype.STRING, "col1"),
            (Column) new DefaultColumn(2, Datatype.STRING, "col2")
        ),
        Data.list(
            (Vector) new DefaultVector(Values.array("00", "01", "02")),
            (Vector) new DefaultVector(Values.array("10", "11", "12")),
            (Vector) new DefaultVector(Values.array("20", "21", "22"))
        )
    );
    
    d2 = new DefaultDataset(
        Data.list(
            (Column) new DefaultColumn(0, Datatype.STRING, "col3"),
            (Column) new DefaultColumn(1, Datatype.STRING, "col4"),
            (Column) new DefaultColumn(2, Datatype.STRING, "col5")
        ),
        Data.list(
            (Vector) new DefaultVector(Values.array("03", "04", "05")),
            (Vector) new DefaultVector(Values.array("13", "14", "15")),
            (Vector) new DefaultVector(Values.array("23", "24", "25"))
        )
    );
    
    merged = new MergedColumnsDataset(Data.list(d1, d2));
  }
  
  @Test
  public void testGetColumnSet() {
    assertEquals(d1.getColumnSet().size() + d2.getColumnSet().size(), merged.getColumnSet().size());
  }

  @Test
  public void testGetColumnInt() {
    for (int i = 0; i < merged.getColumnSet().size(); i++) {
      Vector vec = merged.getColumn(i);
      for (int j : Numbers.range(0, d1.size())) {
        assertEquals(StringValue.of(new StringBuilder().append(j).append(i).toString()), vec.get(j));
      }
    }
  }

  @Test
  public void testGetColumnString() {
    for (int i = 0; i < merged.getColumnSet().size(); i++) {
      Vector vec = merged.getColumn(merged.getColumnSet().get(i).getName());
      for (int j : Numbers.range(0, d1.size())) {
        assertEquals(StringValue.of(new StringBuilder().append(j).append(i).toString()), vec.get(j));
      }
    }
  }

  @Test
  public void testGetColumnSubsetIntCriteriaOfObject() {
    Dataset ds = merged.getColumnSubset(1, v -> v.toString().equals("01") || v.toString().equals("11"));
    assertEquals(2, ds.size());
  }

  @Test
  public void testGetColumnSubsetStringCriteriaOfObject() {
    Dataset ds = merged.getColumnSubset("col1", v -> v.toString().equals("01") || v.toString().equals("11"));
    assertEquals(2, ds.size());
  }

  @Test
  public void testGetRow() {
    for (int i = 0; i < d1.size(); i++) {
      Vector row = merged.getRow(i);
      assertEquals(d1.size() + d2.size(), row.size());
      for (int j = 0; j < row.size(); j++) {
        assertEquals(new StringValue(new StringBuilder().append(i).append(j).toString()), row.get(j));
      }
    }
  }

  @Test
  public void testGetSubset() {
    Dataset subset = merged.getSubset(r ->
        r.get("col0").toString().equals("00") || r.get("col0").toString().equals("10") || r.get("col0").toString().equals("20")
    );
    for (int i : Numbers.range(3)) {
      Vector row = subset.getRow(i);
      for (int j = 0; j < row.size(); j++) {
        assertEquals(new StringValue(new StringBuilder().append(i).append(j).toString()), row.get(j));
      }
    }
    
  }

  @Test
  public void testIndexStringArray() {
    IndexedDataset indexed = merged.index("col0");
    indexed.getIndexedColumnSet().get("col0");
  }

  @Test
  public void testIndexListOfString() {
    IndexedDataset indexed = merged.index(Data.list("col0"));
    indexed.getIndexedColumnSet().get("col0");
  }

  @Test
  public void testIterator() {
    int i =  0;
    for (Vector row : merged) {
      for (int j = 0; j < row.size(); j++) {
        assertEquals(StringValue.of(new StringBuilder().append(i).append(j).toString()), row.get(j));
      }
      i++;
    }
    assertEquals(i, d1.size());
  }

  @Test
  public void testHead() {
    int i =  0;
    for (Vector row : merged.head()) {
      for (int j = 0; j < row.size(); j++) {
        assertEquals(StringValue.of(new StringBuilder().append(i).append(j).toString()), row.get(j));
      }
      i++;
    }
    assertEquals(i, d1.size());
  }

  @Test
  public void testTail() {
    int i =  0;
    for (Vector row : merged.head()) {
      for (int j = 0; j < row.size(); j++) {
        assertEquals(StringValue.of(new StringBuilder().append(i).append(j).toString()), row.get(j));
      }
      i++;
    }
    assertEquals(i, d1.size());
  }

  @Test
  public void testSize() {
    assertEquals(merged.size(), d1.size());
  }

}
