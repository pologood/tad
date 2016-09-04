package org.sapia.tad.impl;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class NullIterator<T> implements Iterator<T> {
  
  @Override
  public boolean hasNext() {
    return false;
  }
  
  @Override
  public T next() {
    throw new NoSuchElementException();
  }
  
  @Override
  public void remove() {
  }

}
