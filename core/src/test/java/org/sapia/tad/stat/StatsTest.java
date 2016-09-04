package org.sapia.tad.stat;

import org.junit.Before;
import org.junit.Test;
import org.sapia.tad.*;
import org.sapia.tad.impl.DefaultColumn;
import org.sapia.tad.impl.DefaultDataset;
import org.sapia.tad.impl.DefaultVector;
import org.sapia.tad.util.Data;
import org.sapia.tad.value.Value;
import org.sapia.tad.value.Values;

import java.util.Random;
import java.util.function.Function;

import static org.junit.Assert.*;

public class StatsTest {

  private Random rand;

  @Before
  public void setUp() {
    rand = new Random();
  }

  @Test
  public void testNormalize() throws Exception {
    Dataset dataset = new DefaultDataset(
            Data.list(
                    new DefaultColumn(0, Datatype.NUMERIC, "col0"),
                    new DefaultColumn(1, Datatype.NUMERIC, "col1"),
                    new DefaultColumn(2, Datatype.NUMERIC, "col2"),
                    new DefaultColumn(3, Datatype.NUMERIC, "col3")
            ),
            Data.list(5, () -> vector())
    );

    Dataset normalized = Stats.normalize(dataset);

    for (int i = 0; i < normalized.getColumnSet().size(); i++) {
      Dataset denormalizedSubset = dataset.getColumnSubset(i, v -> true);
      Dataset normalizedSubset   = normalized.getColumnSubset(i, v -> true);
      Value min = min(denormalizedSubset);
      Value max = max(denormalizedSubset);
      for (int j = 0; j < denormalizedSubset.size(); j++) {
        Value denormalizedValue = denormalizedSubset.getRow(j).get(0);
        Value normalizedValue   = normalizedSubset.getRow(j).get(0);

        assertEquals((denormalizedValue.get() - min.get()) / (max.get() - min.get()), normalizedValue.get(), 0);
      }
    }
  }

  private Vector vector() {
    return Vectors.vector(10 + rand.nextInt(100), 10 + rand.nextInt(100), 10 + rand.nextInt(100), 10 + rand.nextInt(100));
  }

  private Value min(Dataset subset) {
    Value min = null;
    for (Vector v : subset) {
      if (min == null || v.get(0).get() < min.get()) {
        min = v.get(0);
      }
    }
    return min;
  }

  private Value max(Dataset subset) {
    Value max = null;
    for (Vector v : subset) {
      if (max == null || v.get(0).get() > max.get()) {
        max = v.get(0);
      }
    }
    return max;
  }
}