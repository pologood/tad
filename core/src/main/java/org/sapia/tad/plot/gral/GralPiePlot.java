package org.sapia.tad.plot.gral;

import de.erichseifert.gral.data.DataSource;
import de.erichseifert.gral.plots.BarPlot;
import de.erichseifert.gral.plots.PiePlot.PieSliceRenderer;
import de.erichseifert.gral.plots.colors.LinearGradient;
import de.erichseifert.gral.ui.InteractivePanel;
import de.erichseifert.gral.util.Insets2D;
import org.sapia.tad.Dataset;
import org.sapia.tad.algo.Criteria;
import org.sapia.tad.plot.PiePlot;
import org.sapia.tad.plot.PlotStyle;
import org.sapia.tad.value.Value;

import javax.swing.*;
import java.awt.*;

/**
 * A Gral-based {@link PiePlot} implementation.
 * 
 * @author yduchesne
 *
 */
public class GralPiePlot extends JFrame implements PiePlot {
  
  private static final long serialVersionUID = 1L;

  private static final double DEFAULT_GAP = 0.2;

  private Dataset   dataset;
  private PlotStyle style;
  private String    columnName;
  private Color     gradientStart, gradientEnd = Color.GREEN;
  
  @Override
  public void setDataset(Dataset dataset) {
    this.dataset = dataset;
  }
  
  @Override
  public void setStyle(PlotStyle style) {
    this.style = style;
  }
  
  @Override
  public void setColumn(String name) {
    this.columnName = name;
  }
  
  @Override
  public void setGradientStart(Color color) {
    this.gradientStart = color;
  }
  
  @Override
  public void setGradientEnd(Color color) {
    this.gradientEnd = color;
  }
  
  @Override
  public void display() {
    Dataset toDisplay = dataset.getColumnSubset(columnName, new Criteria<Value>() {
      @Override
      public boolean matches(Value v) {
        return true;
      }
    });
    
    DataSource data = new DatasetDataSourceAdapter(toDisplay);
    de.erichseifert.gral.plots.PiePlot plot = new de.erichseifert.gral.plots.PiePlot(data);
    
    plot.setInsets(
        new Insets2D.Double(
          style.getInsets().top, 
          style.getInsets().left, 
          style.getInsets().bottom, 
          style.getInsets().right
        )
    );
    if (style.getTitle() != null) {
      plot.setSetting(BarPlot.TITLE, style.getTitle());
    }
    
    plot.getPointRenderer(data).setSetting(PieSliceRenderer.GAP, DEFAULT_GAP);

    LinearGradient colors = new LinearGradient(
        gradientStart == null ? style.getForeground() : gradientStart, 
        gradientEnd
    );
    
    plot.getPointRenderer(data).setSetting(PieSliceRenderer.COLOR, colors);
    
    plot.getPointRenderer(data).setSetting(PieSliceRenderer.VALUE_DISPLAYED, true);
    plot.getPointRenderer(data).setSetting(PieSliceRenderer.VALUE_COLOR, Color.WHITE);
    plot.getPointRenderer(data).setSetting(PieSliceRenderer.VALUE_FONT, Font.decode(null).deriveFont(Font.BOLD));
    
    getContentPane().add(new InteractivePanel(plot), BorderLayout.CENTER);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setMinimumSize(getContentPane().getMinimumSize());
    setSize(style.getDimension());
    setVisible(true);
    
  }

}
