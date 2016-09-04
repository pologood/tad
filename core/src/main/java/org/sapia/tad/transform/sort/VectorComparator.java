package org.sapia.tad.transform.sort;

import org.sapia.tad.Column;
import org.sapia.tad.ColumnSet;
import org.sapia.tad.Vector;
import org.sapia.tad.value.Value;

import java.util.Comparator;

/**
 * Compares {@link Vector} instances.
 * 
 * @author yduchesne
 *
 */
public class VectorComparator implements Comparator<Vector> {

  private ColumnSet columns;
  
  /**
   * @param columns the {@link ColumnSet} indicating on which columns the comparison is to be done.
   */
  public VectorComparator(ColumnSet columns) {
    this.columns = columns;
  }
  
  @Override
  public int compare(Vector o1, Vector o2) {
    int cmp = 0;
    for (Column col : columns) {
      Value v1 = o1.get(col.getIndex());
      Value v2 = o2.get(col.getIndex());
      cmp = col.getType().strategy().compareTo(v1, v2);
      if (cmp != 0) {
        break;
      } 
    }
    return cmp;
  }
}
