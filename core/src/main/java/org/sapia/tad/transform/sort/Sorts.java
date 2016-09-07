package org.sapia.tad.transform.sort;

import org.sapia.tad.Dataset;
import org.sapia.tad.Vector;
import org.sapia.tad.help.Doc;
import org.sapia.tad.impl.DefaultDataset;
import org.sapia.tad.util.ReverseComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Helper class used for sorting.
 * 
 * @author yduchesne
 *
 */
@Doc("Provides methods pertaining to sorting (in ascending and descending order)")
public class Sorts {
  
  private Sorts() {
  }
  
  /**
   * Sorts the given dataset's rows in ascending order.
   * 
   * @param toSort a {@link Dataset} to sort.
   * @param colNames one or more column names corresponding to the columns on
   * which to do the sorting.
   * @return a new {@link Dataset}, holding the same rows as the given one, but
   * in the desired sort order.
   */  
  @Doc("Sorts the given dataset in ascending order, on the given columns (returns a new sorted dataset)")
  public static Dataset asc(@Doc("a dataset") Dataset toSort, @Doc("the names of the columns on which to sort") List<String> colNames) {
    return asc(toSort, colNames.toArray(new String[colNames.size()]));
  }

  /**
   * Sorts the given dataset's rows in ascending order.
   * 
   * @param toSort a {@link Dataset} to sort.
   * @param colNames one or more column names corresponding to the columns on
   * which to do the sorting.
   * @return a new {@link Dataset}, holding the same rows as the given one, but
   * in the desired sort order.
   */
  @Doc("Sorts the given dataset in ascending order, on the given columns (returns a new sorted dataset)")
  public static Dataset asc(@Doc("a dataset") Dataset toSort, @Doc("the names of the columns on which to sort") String...colNames) {
    List<Vector> rows = new ArrayList<>();
    for (Vector row : toSort) {
      rows.add(row);
    }
    Collections.sort(rows, new VectorComparator(toSort.getColumnSet().includes(colNames)));
    return new DefaultDataset(toSort.getColumnSet(), rows);
  }
  
  /**
   * Sorts the given dataset's rows in descending order.
   * 
   * @param toSort a {@link Dataset} to sort.
   * @param colNames one or more column names corresponding to the columns on
   * which to do the sorting.
   * @return a new {@link Dataset}, holding the same rows as the given one, but
   * in the desired sort order.
   */
  @Doc("Sorts the given dataset in descending order, on the given columns (returns a new sorted dataset)")
  public static Dataset desc(@Doc("a dataset") Dataset toSort, @Doc("the names of the columns on which to sort") List<String> colNames) {
    return desc(toSort, colNames.toArray(new String[colNames.size()]));
  }
  
  /**
   * Sorts the given dataset's rows in descending order.
   * 
   * @param toSort a {@link Dataset} to sort.
   * @param colNames one or more column names corresponding to the columns on
   * which to do the sorting.
   * @return a new {@link Dataset}, holding the same rows as the given one, but
   * in the desired sort order.
   */
  @Doc("Sorts the given dataset in descending order, on the given columns (returns a new sorted dataset)")
  public static Dataset desc(@Doc("a dataset") Dataset toSort, @Doc("the names of the columns on which to sort") String...colNames) {
    List<Vector> rows = new ArrayList<>();
    for (Vector row : toSort) {
      rows.add(row);
    }
    Collections.sort(
        rows, 
        new ReverseComparator<>(
            new VectorComparator(toSort.getColumnSet().includes(colNames))
        )
    );
    return new DefaultDataset(toSort.getColumnSet(), rows);
  }
}
