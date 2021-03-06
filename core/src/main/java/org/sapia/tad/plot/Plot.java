package org.sapia.tad.plot;

import org.sapia.tad.Dataset;

/**
 * Specifies the behavior common to all plot types.
 * 
 * @author yduchesne
 *
 */
public interface Plot {

  /**
   * @param dataset the {@link Dataset} to use.
   */
  public void setDataset(Dataset dataset);

  /**
   * @param style the {@link PlotStyle} to use.
   */
  public void setStyle(PlotStyle style);
  
  /**
   * Displays this plot.
   */
  public void display();
}
