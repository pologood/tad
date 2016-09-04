package org.sapia.tad.transform.formula;

import org.sapia.tad.func.ArgFunction;
import org.sapia.tad.value.Value;

/**
 * Implements a {@link Formula}. 
 *  
 * @author yduchesne
 *
 * @param <RowResult> the {@link org.sapia.tad.RowResult} that this function
 * takes as input.
 */
public interface Formula<RowResult> extends ArgFunction<RowResult, Value> {

}
