package org.sapia.tad.computation;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.sapia.tad.Column;
import org.sapia.tad.Datatype;
import org.sapia.tad.func.NoArgFunction;
import org.sapia.tad.impl.DefaultColumn;
import org.sapia.tad.impl.DefaultColumnSet;
import org.sapia.tad.util.Numbers;
import org.sapia.tad.value.NumericValue;
import org.sapia.tad.value.Value;

public class ComputationResultTest {
  
  private ComputationResult res;
  
  @Before
  public void setUp() {
    List<Column> cols = new ArrayList<>();
    for (int i : Numbers.range(3)) {
      cols.add(new DefaultColumn(i, Datatype.NUMERIC, "col" + i));
    }
    res = new ComputationResult(new DefaultColumnSet(cols));
  }

  @Test
  public void testGetColumnSet() {
    for (int i : Numbers.range(3)) {
      res.getColumnSet().get(i);
    }
  }
  
  @Test
  public void testSet() {
    for (int i : Numbers.range(3)) {
      res.set(res.getColumnSet().get(i), new NumericValue(i + 1));
      assertEquals(i + 1, res.get(res.getColumnSet().get(i)).get(), 0);
    }
  }

  @Test
  public void testGetWithNoArgFunction() {
    for (final int i : Numbers.range(3)) {
      assertEquals(i + 1, res.get(res.getColumnSet().get(i), new NoArgFunction<Value>() {
        @Override
        public Value call() {
          return new NumericValue(i + 1);
        }
      }).get(), 0);
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetNotNull() {
    Column col = new DefaultColumn(5, Datatype.NUMERIC, "col5");
    res.getNotNull(col);
  }

}
