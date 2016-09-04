package org.sapia.tad.plot.gral;

import de.erichseifert.gral.data.DataSeries;
import de.erichseifert.gral.data.DataSource;
import de.erichseifert.gral.plots.XYPlot;
import de.erichseifert.gral.plots.areas.AreaRenderer;
import de.erichseifert.gral.plots.areas.DefaultAreaRenderer2D;
import de.erichseifert.gral.plots.lines.DefaultLineRenderer2D;
import de.erichseifert.gral.plots.lines.LineRenderer;
import de.erichseifert.gral.util.GraphicsUtils;
import org.sapia.tad.plot.LinePlot;

import java.awt.*;
import java.util.List;

/**
 * Gral-based implementation of the {@link LinePlot} interface.
 * 
 * @author yduchesne
 *
 */
public class GralLinePlotAdapter extends GralXYPlotAdapter implements LinePlot {

  private static final long serialVersionUID = 1L;

  @Override
  protected void customize(XYPlot plot, DataSource datasource, List<DataSeries> series) {
    Color c = style.getForeground();
    for (DataSeries s : series) {
      plot.setPointRenderer(s, null);
      LineRenderer line = new DefaultLineRenderer2D();
      line.setSetting(DefaultLineRenderer2D.COLOR, c);
      plot.setLineRenderer(s, line);
      AreaRenderer areaUpper = new DefaultAreaRenderer2D();
      areaUpper.setSetting(AreaRenderer.COLOR, GraphicsUtils.deriveWithAlpha(c, 64));
      plot.setAreaRenderer(s, areaUpper);
      c = randomColor();
    }
  }
 
}
