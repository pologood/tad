package org.sapia.tad.transform.range;

import org.junit.Before;
import org.junit.Test;
import org.sapia.tad.*;
import org.sapia.tad.impl.DefaultDataset;
import org.sapia.tad.stat.MeanValue;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class RangesTest {
  
  private Dataset dataset;

  private Tad tad;
  
  @Before
  public void setUp() {
    tad = TestTad.get();
    ColumnSet columns = ColumnSets.columnSet(
        "col0", Datatype.STRING, 
        "col1", Datatype.NUMERIC, 
        "col2", Datatype.STRING,
        "col3", Datatype.NUMERIC
    );
    
    List<Vector> rows = new ArrayList<>();
        
    for (int i = 0; i < 10; i++) {
      int v1 = i + 1;
      int v2 = (i + 1) * 10; 
      rows.add(Vectors.with("s" + v1, v1, "s" + v2, v2));
    }
    dataset = new DefaultDataset(columns, rows);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testRange() {
    Dataset result = tad.xf.ranges.range(dataset, 2, "col1", "col3");
    for (int i = 0; i < result.size(); i += 2) {
      double v1 = new MeanValue().increase(i + 1).increase(i + 2).get();
      double v2 = v1 * 10d; 
      Range c1 = (Range) result.getRow(i).get(1);
      Range c3 = (Range) result.getRow(i + 1).get(3);
      assertEquals(v1, c1.get(), 0);
      assertEquals(v2, c3.get(), 0);
    }
  }

}
