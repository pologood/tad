package org.sapia.tad.plot.gral;

import org.sapia.tad.ColumnSets;
import org.sapia.tad.Dataset;
import org.sapia.tad.Datatype;
import org.sapia.tad.Vectors;
import org.sapia.tad.plot.Plot;
import org.sapia.tad.plot.Plots;
import org.sapia.tad.util.Data;
import org.sapia.tad.util.SettingValues;

import static org.sapia.tad.Datasets.dataset;

public class Main  {

  public Main() {
     
    Dataset dataset = dataset(
        ColumnSets.columnSet(
          "x", Datatype.NUMERIC,
          "category", Datatype.STRING
        ), 
        Data.list(
            Vectors.vector(3, "silver"),
            Vectors.vector(2, "bronze"),
            Vectors.vector(3, "silver"),
            Vectors.vector(4, "gold"),
            Vectors.vector(2, "bronze"),
            Vectors.vector(2, "bronze"),
            Vectors.vector(5, "diamond")
        )
        
    );
    
    Plot plot = Plots.pieplot(
        dataset, 
        SettingValues.valueOf(
            "title", "Example", 
            "column", "x"
        )
    );
    plot.display();
  }
  
  public static void main(String[] args) {
    Main main = new Main();
  }
}
