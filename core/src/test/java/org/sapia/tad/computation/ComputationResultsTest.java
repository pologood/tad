package org.sapia.tad.computation;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.sapia.tad.ColumnSets;
import org.sapia.tad.Datatype;
import org.sapia.tad.value.NumericValue;

public class ComputationResultsTest {
  
  private ComputationResults context;
  
  @Before
  public void setUp() {
    context = ComputationResults.newInstance(
        ColumnSets.columnSet("col0", Datatype.NUMERIC, "col1", Datatype.STRING)
    );
  }

  @Test
  public void testGet() {
    ComputationResult result = context.get("test");
    result.set(context.getColumnSet().get("col0"), new NumericValue(1));
    result = context.get("test");
    assertEquals(new NumericValue(1), result.get(context.getColumnSet().get("col0")));
  }
}
