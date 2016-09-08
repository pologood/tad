package org.sapia.tad;

import org.sapia.tad.help.Doc;
import org.sapia.tad.impl.DatasetRowSetAdapter;
import org.sapia.tad.impl.DefaultRowSet;
import org.sapia.tad.impl.DefaultVector;
import org.sapia.tad.util.Numbers;
import org.sapia.tad.value.NumericValue;
import org.sapia.tad.value.Value;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility methods pertaining to rowsets.
 * @author yduchesne
 *
 */
@Doc("Holds utility methods pertaining to RowSets")
public class RowSets {

  private RowSets() {
  }
  
  /**
   * Performs an arithmetic sum over the rows of the given {@link RowSet}.
   * <p>
   * Non-numeric values in the rowset's vectors are taken as zeroes.
   * 
   * @param toSum a {@link RowSet} whose vectors should be summed.
   * @return the {@link Vector} resulting from the sum operation.
   */
  @Doc("Performs the arithmetic sum of the vectors in the provided rowsets. Non-numeric values are converted to 0")
  public static Vector sum(RowSet toSum) {
    return sum(0, toSum);
  }
  
  /**
   * Performs an arithmetic sum over the rows of the given {@link RowSet}, but only for vector
   * values from the given index and up.
   * <p>
   * Non-numeric values in the rowset's vectors are taken as zeroes.
   * 
   * @param startVectorIndex the start index from which to sum vector values (given a vector v1 and vector v2,
   * the sum will be done over v1[startIndex] + v2[startIndex], v1[startIndex + 1] + v2[startIndex + 1], and so
   * on). 
   * @param toSum a {@link RowSet} whose vectors should be summed.
   * @return the {@link Vector} resulting from the sum operation.
   */
  @Doc("Performs the arithmetic sum of the vectors in the provided rowsets. Performs the sum for vector values at given index and beyond. " +
       "Non-numeric values are converted to 0")
  public static Vector sum(int startVectorIndex, RowSet toSum) {
    Value[] sums = null;
    for (Vector v : toSum) {
      if (sums == null) {
        sums = Numbers.repeatDouble(0d, v.size());
      }
      for (int i = startVectorIndex; i < v.size(); i++) {
        Value value = v.get(i);
        if (value.isNumeric()) {
          sums[i] = NumericValue.sum(sums[i], value);
        }
      }
    }
    return new DefaultVector((Value[])sums);
  }

  /**
   * @param dataset a {@link Dataset}.
   * @return a {@link RowSet} wrapping the given dataset.
   */
  public static RowSet rowSet(Dataset dataset) {
    return new DatasetRowSetAdapter(dataset);
  }
  
  /**
   * @param arraysOfVectorValues an values of other arrays that correspond
   * to vector values.
   * @return the {@link RowSet} holding the corresponding {@link Vector}s.
   */
  public static RowSet rowSet(Value[][] arraysOfVectorValues) {
    List<Vector> rows = new ArrayList<>();
    for (int i = 0; i < arraysOfVectorValues.length; i++) {
      rows.add(new DefaultVector(arraysOfVectorValues[i]));
    }
    return new DefaultRowSet(rows);
  }
  
  /**
   * @param listsOfVectorValues a list containing other lists that each
   * consist of the values for a given {@link Vector}.
   * @return the {@link RowSet} holding the corresponding {@link Vector}s.
   */
  public static RowSet rowSet(List<List<Value>> listsOfVectorValues) {
    List<Vector> rows = new ArrayList<>();
    for (List<Value> values : listsOfVectorValues) {
      rows.add(new DefaultVector(values));
    }
    return new DefaultRowSet(rows);
  }
}
