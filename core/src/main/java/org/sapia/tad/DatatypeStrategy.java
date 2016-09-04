package org.sapia.tad;

import org.sapia.tad.value.Value;

/**
 * An instance of this interface is associated to a {@link Datatype}.
 * 
 * @author yduchesne
 *
 */
public interface DatatypeStrategy {

  /**
   * Tests if a given "raw value" can be wrapped in a {@link Value} that this strategy would create.
   *
   * @param rawValue a raw value (that is: a value expected to be eventually wrapped in a {@link Value} instance that this
   *                 strategy would create).
   * @return <code>true</code> if the given value is assignable to the 
   * {@link Datatype} to which this instance corresponds.
   */
  public boolean isAssignableFrom(Object rawValue);

  /**
   * @param value a {@link Value} instance.
   * @return <code>true</code> if the {@link Value} instance passed in corresponds to the concrete type of {@link Value}s
   * that this instance creates.
   */
  public boolean isType(Value value);

  /**
   * @param obj an arbitrary object to wrap.
   * @return a new {@link Value} wrapping the given object.
   */
  public Value getValueFor(Object obj);
  
  /**
   * @param currentValue the current value.
   * 
   * @return
   */
  public Object add(Object currentValue, Object toAdd);
  
  /**
   * @param value the {@link Value} whose type corresponds to this instance's {@link Datatype}.
   * @param operand the {@link Value} operand with which to compare the given value.
   * @return 0 if the both parameters are deemed equal, a number less than 0 if <code>value</code> is deemed
   * lower/smaller than <code>operand</code>, or larger than 0 if <code>value</code> is deemed 
   * greater/larger than <code>operand</code>.
   */
  public int compareTo(Value value, Value operand);

}
