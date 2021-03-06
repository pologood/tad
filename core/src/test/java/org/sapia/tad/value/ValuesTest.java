package org.sapia.tad.value;

import static org.junit.Assert.*;

import org.junit.Test;
import org.sapia.tad.util.Numbers;

public class ValuesTest {

  @Test
  public void array_with_numbers() {
    Value[] values = Values.array(0, 1, 2, 3, 4);
    for (int i : Numbers.range(0, 4)) {
      assertEquals(NumericValue.of(i), values[i]);
    }
  }

  @Test
  public void array_with_strings() {
    Value[] values = Values.array("0", "1", "2", "3", "4");
    for (int i : Numbers.range(0, 4)) {
      assertEquals(StringValue.of(i), values[i]);
    }
  }

  @Test
  public void array_with_values() {
    Value[] values = Values.array(
            NumericValue.of(0),
            NumericValue.of(1),
            NumericValue.of(2),
            NumericValue.of(3)
    );
    for (int i : Numbers.range(0, 4)) {
      assertEquals(NumericValue.of(i), values[i]);
    }
  }

  @Test
  public void array_with_null() {
    Value[] values = Values.array(
            null,
            NumericValue.of(1),
            NumericValue.of(2),
            NumericValue.of(3)
    );
    for (int i : Numbers.range(0, 4)) {
      assertEquals(NumericValue.of(i), values[i]);
    }
  }

}
