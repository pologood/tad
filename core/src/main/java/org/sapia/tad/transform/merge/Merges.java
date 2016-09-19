package org.sapia.tad.transform.merge;

import org.sapia.tad.Dataset;
import org.sapia.tad.TadContext;
import org.sapia.tad.help.Doc;
import org.sapia.tad.util.Checks;

import java.util.Arrays;
import java.util.List;

/**
 * Provides dataset merging methods.
 * 
 * @author yduchesne
 *
 */
@Doc("Provides dataset merging methods")
public class Merges {

  private TadContext context;

  public Merges(TadContext context) {
    this.context = context;
  }
  
  /**
   * @param datasets one or more {@link Dataset}s to merge.
   * @return the {@link Dataset} resulting from the merge.
   */
  @Doc("Merges the rows of one or more datasets into a single dataset (all datasets must have the same columns)")
  public Dataset mergeRows(
    @Doc("the datasets to merge") Dataset...datasets) {
    return mergeRows(Arrays.asList(datasets));
  }
  
  /**
   * @param datasets a non-empty {@link List} of {@link Dataset}s to merge.
   * @return the {@link Dataset} resulting from the merge.
   */
  @Doc("Merges the rows one or more datasets into a single dataset (all datasets must have the same number columns)")
  public Dataset mergeRows(
    @Doc("the datasets to merge") List<Dataset> datasets) {
    Checks.isFalse(datasets.isEmpty(), "Dataset list cannot be empty (one or more dataset(s) to merge must be provided)");
    return new MergedRowsDataset(datasets.get(0).getColumnSet(), datasets);
  }

  /**
   * @param datasets the datasets to merge.
   * @return the {@link Dataset} resulting from the merge.
   */
  @Doc("Merges the columns one or more datasets into a single dataset (all datasets must have the same number of rows)")
  public Dataset mergeColumns(
      @Doc("the datasets to merge") Dataset...datasets) {
    return mergeColumns(Arrays.asList(datasets));
  }

  /**
   * @param datasets the datasets to merge.
   * @return the {@link Dataset} resulting from the merge.
   */
  @Doc("Merges the columns one or more datasets into a single dataset (all datasets must have the same number of rows)")
  public Dataset mergeColumns(
      @Doc("the datasets to merge") List<Dataset> datasets) {
    return new MergedColumnsDataset(datasets);
  }
  
}
