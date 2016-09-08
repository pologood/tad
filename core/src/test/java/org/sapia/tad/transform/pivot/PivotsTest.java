package org.sapia.tad.transform.pivot;

import org.junit.Before;
import org.junit.Test;
import org.sapia.tad.*;
import org.sapia.tad.util.Data;

public class PivotsTest {
  
  private Dataset multiLevel, singleLevel;
  
  @Before
  public void setUp() {
    multiLevel = Datasets.dataset(
        ColumnSets.columnSet("country", Datatype.STRING, "gdp", Datatype.NUMERIC, "year", Datatype.NUMERIC),
        Data.list(
            Vectors.with("us", 100, 2000),
            Vectors.with("us", 101, 2001),
            Vectors.with("us", 102, 2002),
            Vectors.with("uk", 200, 2000),
            Vectors.with("uk", 201, 2001),
            Vectors.with("uk", 203, 2003),
            Vectors.with("fr", 302, 2002)
        )
    );
    
    singleLevel = Datasets.dataset(
        ColumnSets.columnSet("gdp", Datatype.NUMERIC, "year", Datatype.NUMERIC),
        Data.list(
            Vectors.with(100, 2000),
            Vectors.with(101, 2001),
            Vectors.with(102, 2002),
            Vectors.with(200, 2000),
            Vectors.with(201, 2001),
            Vectors.with(203, 2003),
            Vectors.with(302, 2002)
        )
    );
  }

  @Test
  public void testPivot() {
    System.out.println(Pivots.pivot(multiLevel, "year", "country", "gdp"));
  }

}
