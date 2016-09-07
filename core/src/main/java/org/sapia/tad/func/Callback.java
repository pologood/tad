package org.sapia.tad.func;

/**
 * An instance of this interface is invoked upon a certain event occurring - such callbacks are meant for basic
 * notifications, and are not expected to return any value, nor to receive anything as input.
 *
 * @author yduchesne
 */
public interface Callback {

  /**
   * Invoked upon an arbitrary/application-specific event occurring.
   */
  public void apply();
}
