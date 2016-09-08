package org.sapia.tad.stat;

import org.junit.Before;
import org.junit.Test;
import org.sapia.tad.ColumnSets;
import org.sapia.tad.Datatype;
import org.sapia.tad.RowSet;
import org.sapia.tad.RowSets;
import org.sapia.tad.computation.ComputationResults;
import org.sapia.tad.value.NumericValue;
import org.sapia.tad.value.Value;
import org.sapia.tad.value.Values;

import static org.junit.Assert.assertEquals;

public class MedianComputationTest {
  
  private ComputationResults context;
  private RowSet oddRows, evenRows;
  private MedianComputation comp;
 
  
  @Before
  public void setUp() {
    context = ComputationResults.newInstance(
        ColumnSets.columnSet("col0", Datatype.NUMERIC, "col1", Datatype.STRING)
    );
   
    oddRows = RowSets.rowSet(new Value[][] {
       Values.with(5, "row1"),
       Values.with(10, "row2"),
       Values.with(15, "row3")
    });
    
    evenRows = RowSets.rowSet(new Value[][] {
        Values.with(5, "row1"),
        Values.with(10, "row2"),
     });
    
    comp = new MedianComputation();
  }


  @Test
  public void testComputeWithOddRows() {
    comp.compute(context, oddRows);
    assertEquals(new NumericValue(10), context.get("median").get(context.getColumnSet().get("col0")));
    assertEquals(new NumericValue(0), context.get("median").get(context.getColumnSet().get("col1")));
  }
  
  @Test
  public void testComputeWithEvenRows() {
    comp.compute(context, evenRows);
    assertEquals(new NumericValue(7.5), context.get("median").get(context.getColumnSet().get("col0")));
    assertEquals(new NumericValue(0), context.get("median").get(context.getColumnSet().get("col1")));
  }

}
