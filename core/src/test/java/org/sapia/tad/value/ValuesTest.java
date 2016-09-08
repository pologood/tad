package org.sapia.tad.value;

import org.junit.Test;
import org.sapia.tad.util.Numbers;

import static org.junit.Assert.assertEquals;

public class ValuesTest {

  @Test
  public void array_with_numbers() {
    Value[] values = Values.with(0, 1, 2, 3, 4);
    for (int i : Numbers.range(0, 4)) {
      assertEquals(NumericValue.of(i), values[i]);
    }
  }

  @Test
  public void array_with_strings() {
    Value[] values = Values.with("0", "1", "2", "3", "4");
    for (int i : Numbers.range(0, 4)) {
      assertEquals(StringValue.of(i), values[i]);
    }
  }

  @Test
  public void array_with_values() {
    Value[] values = Values.with(
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
    Value[] values = Values.with(
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
