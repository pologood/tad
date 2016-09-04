package org.sapia.tad.plot.gral;

import org.sapia.tad.plot.*;

/**
 * A {@link PlotProvider} implemented on top of the Gral framework.
 * 
 * @author yduchesne
 *
 */
public class GralPlotProvider implements PlotProvider {
  
  @Override
  public XYPlot newXYPlot() {
    return new GralXYPlotAdapter();
  }
  
  @Override
  public LinePlot newLinePlot() {
    return new GralLinePlotAdapter();
  }

  @Override
  public HistogramPlot newHistogram() {
    return new GralHistogramPlotAdapter();
  }
  
  @Override
  public PiePlot newPiePlot() {
    return new GralPiePlot();
  }
}
