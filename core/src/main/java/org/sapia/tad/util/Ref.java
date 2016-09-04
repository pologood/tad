package org.sapia.tad.util;

/**
 * Models a reference to another object.
 * 
 * @author yduchesne
 *
 */
public interface Ref<T> {
  
  /**
   * @return the object wrapped by this instance.
   */
  public T get();

}
