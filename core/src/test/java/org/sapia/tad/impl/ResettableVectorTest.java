package org.sapia.tad.impl;

import org.junit.Before;
import org.junit.Test;
import org.sapia.tad.Vector;
import org.sapia.tad.util.Data;
import org.sapia.tad.value.NumericValue;
import org.sapia.tad.value.Value;
import org.sapia.tad.value.Values;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author yduchesne
 */
public class ResettableVectorTest {

  private Vector originVector;
  private ResettableVector vector;

  @Before
  public void setUp() {
    originVector = new DefaultVector(Values.with(0, 1, 2, 3));
    vector = ResettableVector.of(originVector);
  }

  @Test
  public void testGet() {
    assertEquals(new NumericValue(0), vector.get(0));
    assertEquals(new NumericValue(1), vector.get(1));
    assertEquals(new NumericValue(2), vector.get(2));
    assertEquals(new NumericValue(3), vector.get(3));
  }

  @Test
  public void testGet_after_set() {
    vector.set(0, 1);

    assertEquals(new NumericValue(1), vector.get(0));
  }

  @Test
  public void testReset_after_set() {
    vector.set(0, 1);
    assertEquals(new NumericValue(1), vector.get(0));

    vector.reset();
    assertEquals(new NumericValue(0), vector.get(0));
  }

  @Test
  public void testSubset() {
    Vector subset = vector.subset(1, 2);
    assertEquals(new NumericValue(1), subset.get(0));
    assertEquals(new NumericValue(2), subset.get(1));
  }

  @Test
  public void testSize() {
    assertEquals(4, vector.size());
  }

  @Test
  public void testIterator() {
    List<Value> values = Data.list(vector.iterator());
    assertEquals(new NumericValue(0), values.get(0));
    assertEquals(new NumericValue(1), values.get(1));
    assertEquals(new NumericValue(2), values.get(2));
    assertEquals(new NumericValue(3), values.get(3));
  }

  @Test
  public void testIterator_after_set() {
    vector.set(0, 1);

    List<Value> values = Data.list(vector.iterator());
    assertEquals(new NumericValue(1), values.get(0));
  }

  @Test
  public void testIterator_after_reset() {
    vector.set(0, 1);

    List<Value> values = Data.list(vector.iterator());
    assertEquals(new NumericValue(1), values.get(0));

    vector.reset();

    values = Data.list(vector.iterator());
    assertEquals(new NumericValue(0), values.get(0));
  }
}