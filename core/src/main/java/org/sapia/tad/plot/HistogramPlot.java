package org.sapia.tad.plot;

public interface HistogramPlot extends Plot {

  /**
   * @param column the name of the column whose array should be used.
   */
  public void setColumn(String column);

  /**
   * @param spacing the spacing value to use between X-axis ticks.
   */
  public void setTicksSpacingX(int spacing);
  
  /**
   * @param spacing the spacing value to use between Y-axis ticks.
   */
  public void setTicksSpacingY(int spacing);
}
