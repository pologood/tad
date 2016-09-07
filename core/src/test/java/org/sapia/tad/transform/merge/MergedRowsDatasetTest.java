package org.sapia.tad.transform.merge;

import org.junit.Before;
import org.junit.Test;
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

import static org.junit.Assert.assertEquals;

public class MergedRowsDatasetTest {
  
  private Dataset d1, d2;
  private Dataset merged;
  
  @Before
  public void testSetup() {
    d1 = new DefaultDataset(
        Data.list(
            new DefaultColumn(0, Datatype.STRING, "col0"),
            new DefaultColumn(1, Datatype.STRING, "col1"),
            new DefaultColumn(2, Datatype.STRING, "col2")
        ),
        Data.list(
            new DefaultVector("00", "01", "02"),
            new DefaultVector("10", "11", "12"),
            new DefaultVector("20", "21", "22")
        )
    );
    
    d2 = new DefaultDataset(
        Data.list(
            new DefaultColumn(0, Datatype.STRING, "col0"),
            new DefaultColumn(1, Datatype.STRING, "col1"),
            new DefaultColumn(2, Datatype.STRING, "col2")
        ),
        Data.list(
            new DefaultVector("30", "31", "32"),
            new DefaultVector("40", "41", "42"),
            new DefaultVector("50", "51", "52"),
            new DefaultVector("60", "61", "62")
        )
    );
    
    merged = new MergedRowsDataset(d1.getColumnSet(), Data.list(d1, d2));
  }

  @Test
  public void testGetColumnSet() {
    assertEquals(d1.getColumnSet(), merged.getColumnSet());
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
    merged.getColumnSubset(1, v -> v.getInternalValue().equals("41") || v.getInternalValue().equals("51") || v.getInternalValue().equals("61"));
  }

  @Test
  public void testGetColumnSubsetStringCriteriaOfObject() {
    merged.getColumnSubset("col1", v -> v.getInternalValue().equals("41") || v.getInternalValue().equals("51") || v.getInternalValue().equals("61"));
  }

  @Test
  public void testGetRow() {
    for (int i = 0; i < d1.size(); i++) {
      Vector row = merged.getRow(i);
      for (int j = 0; j < row.size(); j++) {
        assertEquals(StringValue.of(new StringBuilder().append(i).append(j).toString()), row.get(j));
      }
    }
  }

  @Test
  public void testGetSubset() {
    Dataset subset = merged.getSubset(v -> v.get(0).getInternalValue().equals("40") || v.get(0).getInternalValue().equals("50") || v.get(0).getInternalValue().equals("60"));
    for (int i : Numbers.range(3)) {
      Vector row = subset.getRow(i);
      for (int j = 0; j < row.size(); j++) {
        assertEquals(StringValue.of(new StringBuilder().append(i + 4).append(j).toString()), row.get(j));
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
    assertEquals(i, d1.size() + d2.size());
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
    assertEquals(i, d1.size() + d2.size());
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
    assertEquals(i, d1.size() + d2.size());
  }

  @Test
  public void testSize() {
    assertEquals(merged.size(), d1.size() + d2.size());
  }

}
