package org.sapia.tad.stat;

import org.junit.Before;
import org.junit.Test;
import org.sapia.tad.ColumnSets;
import org.sapia.tad.Datatype;
import org.sapia.tad.RowSet;
import org.sapia.tad.RowSets;
import org.sapia.tad.computation.ComputationResult;
import org.sapia.tad.computation.ComputationResults;
import org.sapia.tad.value.NumericValue;
import org.sapia.tad.value.Value;
import org.sapia.tad.value.Values;

import static org.junit.Assert.assertEquals;

public class MeanVarianceComputationTest {
  
  private ComputationResults context;
  private RowSet rows;
  private SpreadStatsComputation comp;
  
  @Before
  public void setUp() {
    context = ComputationResults.newInstance(
        ColumnSets.columnSet("col0", Datatype.NUMERIC, "col1", Datatype.STRING)
    );
   
    rows = RowSets.rowSet(new Value[][] {
       Values.array(5, "row1"),
       Values.array(10, "row2"),
       Values.array(15, "row3")
    });
    
    comp = new SpreadStatsComputation();
  }

  @Test
  public void testComputeMean() {
    comp.compute(context, rows);
    ComputationResult mean     = context.get("mean");
    assertEquals(new NumericValue(10), mean.get(context.getColumnSet().get("col0")));
    assertEquals(new NumericValue(0), mean.get(context.getColumnSet().get("col1")));
  }
  
  @Test
  public void testComputeVariance() {
    comp.compute(context, rows);
    ComputationResult variance = context.get("variance");
    double mean = 10;
    double expected = Math.pow(5 - mean, 2) + Math.pow(10 - mean, 2) + Math.pow(15 - mean, 2) ;
    
    assertEquals(new NumericValue(expected), variance.get(context.getColumnSet().get("col0")));
    assertEquals(new NumericValue(0), variance.get(context.getColumnSet().get("col1")));
  }
  
  @Test
  public void testComputeStandardDeviation() {
    comp.compute(context, rows);
    ComputationResult stddev = context.get("stddev");
    double mean = 10;
    double expected = Math.sqrt(Math.pow(5 - mean, 2) + Math.pow(10 - mean, 2) + Math.pow(15 - mean, 2));
    assertEquals(new NumericValue(expected), stddev.get(context.getColumnSet().get("col0")));
    assertEquals(new NumericValue(0), stddev.get(context.getColumnSet().get("col1")));
  }

}
