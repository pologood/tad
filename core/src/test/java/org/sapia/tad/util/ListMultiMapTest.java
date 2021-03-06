package org.sapia.tad.util;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ListMultiMapTest {

  private ListMultiMap<String, String> map;
  
  @Before
  public void setUp() {
    map = ListMultiMap.createArrayListMultiMap();
  }
  
  @Test
  public void testGet() {
    map.put("k", "v1").put("k", "v2");
    assertTrue(map.get("k").containsAll(Data.set("v1", "v2")));
  }

  @Test
  public void testKeySet() {
    map.put("k1", "v1").put("k2", "v2");
    assertTrue(map.keySet().containsAll(Data.set("k1", "k2")));
  }  
}
