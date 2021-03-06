package org.sapia.tad.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.sapia.tad.Column;
import org.sapia.tad.ColumnSet;
import org.sapia.tad.Datatype;
import org.sapia.tad.RowSet;
import org.sapia.tad.Vector;
import org.sapia.tad.util.Data;
import org.sapia.tad.util.Numbers;
import org.sapia.tad.value.Values;

public class DefaultIndexTest {
  
  private RowSet rowset;
  private DefaultIndex singleIndex, multiIndex;
  
  @Before
  public void setUp() {
    rowset = new DefaultRowSet(
        Data.list(
            (Vector) new DefaultVector("00", "00", "00", "00"),
            (Vector) new DefaultVector("00", "00", "00", "01"),
            (Vector) new DefaultVector("00", "00", "00", "02"),
            
            (Vector) new DefaultVector("00", "00", "01", "00"),
            (Vector) new DefaultVector("00", "00", "01", "01"),
            (Vector) new DefaultVector("00", "00", "01", "02"),
            
            (Vector) new DefaultVector("00", "01", "01", "00"),
            (Vector) new DefaultVector("00", "01", "01", "01"),
            (Vector) new DefaultVector("00", "01", "01", "02"),
            
            (Vector) new DefaultVector("01", "01", "01", "00"),
            (Vector) new DefaultVector("01", "01", "01", "01"),
            (Vector) new DefaultVector("01", "01", "01", "02")
        )
      );
    
    ColumnSet columns = new DefaultColumnSet(
        Data.list(
            (Column) new DefaultColumn(0, Datatype.STRING, "col0"),
            (Column) new DefaultColumn(1, Datatype.STRING, "col1"),
            (Column) new DefaultColumn(2, Datatype.STRING, "col2"),
            (Column) new DefaultColumn(2, Datatype.STRING, "col3")
        )
    );
    
    singleIndex = new DefaultIndex(
        rowset, 
        columns, 
        columns.includes("col1")
    );
    
    multiIndex = new DefaultIndex(
        rowset, 
        columns, 
        columns.includes("col1", "col2")
    ); 
  }

  @Test
  public void testGetColumnSet() {
    ColumnSet colSet = multiIndex.getColumnSet();
    colSet.get("col0");
    colSet.get("col1");
    colSet.get("col2");
  }

  @Test
  public void testGetIndexedColumnSet() {
    ColumnSet colSet = multiIndex.getIndexedColumnSet();
    colSet.get("col1");
    colSet.get("col2");
  }

  @Test
  public void testGetKeys() {
    assertEquals(2, singleIndex.getKeys().size());
    assertEquals(3, multiIndex.getKeys().size());
  }

  @Test
  public void testSize() {
    assertEquals(12, singleIndex.size());
    assertEquals(12, multiIndex.size());

  }

  @Test
  public void testGetRowSet() {
    Set<Vector> expected = Data.set(rowset.iterator());
    Set<Vector> singleIndexRowSet = Data.set(singleIndex.getRowSet().iterator());
    assertTrue(expected.containsAll(singleIndexRowSet));
  }
  
  @Test
  public void testGetRowSetForMultiIndex() {
    Set<Vector> expected = Data.set(rowset.iterator());
    Set<Vector> multiIndexRowSet = Data.set(multiIndex.getRowSet().iterator());
    assertTrue(expected.containsAll(multiIndexRowSet));
  }

  @Test
  public void testGet() {
    for (int i : Numbers.range(rowset.size())) {
      assertEquals(rowset.get(i), singleIndex.get(i));
    }
  }
  
  @Test
  public void testGetForMultiIndex() {
    for (int i : Numbers.range(rowset.size())) {
      assertEquals(rowset.get(i), multiIndex.get(i));
    }
  }

  @Test
  public void testGetRowSetByKeyForSingleIndex() {
    RowSet result = singleIndex.getRowSet(Data.array("col1"), Values.array("00"));
    RowSet expected = new DefaultRowSet(
        Data.list(
          (Vector) new DefaultVector("00", "00", "00", "00"),
          (Vector) new DefaultVector("00", "00", "00", "01"),
          (Vector) new DefaultVector("00", "00", "00", "02"),
          (Vector) new DefaultVector("00", "00", "01", "00"),
          (Vector) new DefaultVector("00", "00", "01", "01"),
          (Vector) new DefaultVector("00", "00", "01", "02")        
        )
    );
    
    for (int i = 0 ; i < result.size(); i++) {
      assertEquals(expected.get(i), result.get(i));
    }
  }
  
  @Test
  public void testGetRowSetByKeyForMultiIndex() {
    RowSet result   = multiIndex.getRowSet(Data.array("col1", "col2"), Values.array("00", "01"));
    RowSet expected = new DefaultRowSet(
        Data.list(
            new DefaultVector("00", "00", "01", "00"),
            new DefaultVector("00", "00", "01", "01"),
            new DefaultVector("00", "00", "01", "02")
        )
    );
    
    for (int i = 0 ; i < result.size(); i++) {
      assertEquals(expected.get(i), result.get(i));
    }
  }

}
