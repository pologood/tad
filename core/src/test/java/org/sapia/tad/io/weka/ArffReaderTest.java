package org.sapia.tad.io.weka;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Test;
import org.sapia.tad.util.Checks;

public class ArffReaderTest {
  
  @Test
  public void testLoadDataset() throws Exception {
    ArffReader reader = new ArffReader();
    InputStream is = getClass().getResourceAsStream("Test.arff");
    Checks.notNull(is, "No resource found: Test.arff");
    try {
      reader.read(new BufferedReader(new InputStreamReader(is)));
    } finally {
      is.close();
    }
  }

}
