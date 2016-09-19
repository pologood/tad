package org.sapia.tad.ml.regression;

import org.sapia.tad.*;
import org.sapia.tad.impl.DefaultDataset;
import org.sapia.tad.matrix.Matrix;
import org.sapia.tad.ml.error.CumulativeError;
import org.sapia.tad.ml.error.MseCumulativeError;
import org.sapia.tad.util.Checks;
import org.sapia.tad.util.Data;
import org.sapia.tad.util.Numbers;
import org.sapia.tad.value.NumericValue;

import java.util.List;

/**
 * Created by yanic on 9/11/2016.
 */
public class LinearRegression {

  private double   learningRate;
  private double   convergenceThreshold;
  private Vector   ltm;
  private Matrix   featureMatrix;
  private Matrix   featureMatrixTranspose;
  private Vector   outcomeVector;

  private double cost = -1;

  public LinearRegression(Tad tad, Dataset dataset, double learningRate, double convergenceThreshold, String outcomeColumnName, String...featureColumnNames) {
    Checks.isFalse(dataset.size() == 0, "Dataset is empty");
    Checks.isFalse(learningRate <= 0, "Learning rate must be greater than 0 - got %s", learningRate);
    Checks.isFalse(featureColumnNames.length == 0, "Empty array for feature columns names");

    this.convergenceThreshold = convergenceThreshold;
    this.learningRate = learningRate;

    dataset = tad.stats.rangeNormalization(dataset);

    Dataset features        = tad.xf.views.include(dataset, featureColumnNames);
    Vector  identityColumn  = Vectors.withNumbers(Numbers.repeat(1, features.size()));
    featureMatrix           = tad.matrices.merge(
                                      tad.matrices.withColumn(identityColumn),
                                      tad.matrices.withDataset(features)
                              );

    ltm = Vectors.withNumbers(Numbers.repeat(0.1d, featureMatrix.getDimensions().getColumnCount()));

    outcomeVector           = tad.xf.views.include(dataset, outcomeColumnName).getColumn(0);
    featureMatrixTranspose  = featureMatrix.transpose();
  }


  private void computeCost() {
    CumulativeError error = new MseCumulativeError();
    Vector hypothesis = featureMatrix.product(ltm);
    cost =  1d/2d * featureMatrix.getDimensions().getRowCount() * error.accumulate(outcomeVector, hypothesis).get();
  }

  public Vector getLtm() {
    return ltm;
  }

  public boolean iterate() throws InterruptedException {
    if (cost == -1) {
      computeCost();
    }
    NumericValue gradientRatio = NumericValue.of(learningRate / featureMatrix.getDimensions().getRowCount());
    Vector descent = featureMatrixTranspose
            .product(featureMatrix.product(ltm).difference(outcomeVector))
            .product(gradientRatio);

    ltm = ltm.difference(descent);

    double oldCost = cost;
    computeCost();
    if (oldCost - cost > convergenceThreshold) {
      return true;
    } else {
      return false;
    }
  }

  public static void main(String[] args) throws Exception {
    List<Vector> rows = Data.list(
            Vectors.withNumbers(225000, 1300, 3),
            Vectors.withNumbers(335000, 1700, 3),
            Vectors.withNumbers(450000, 2400, 4),
            Vectors.withNumbers(220000, 1250, 3),
            Vectors.withNumbers(350000, 1800, 3),
            Vectors.withNumbers(435000, 2500, 3),
            Vectors.withNumbers(275000, 1200, 3),
            Vectors.withNumbers(345000, 1550, 3),
            Vectors.withNumbers(500000, 2500, 4),
            Vectors.withNumbers(230000, 1300, 3),
            Vectors.withNumbers(310000, 1400, 3),
            Vectors.withNumbers(525000, 2600, 4)
    );

    /*List<Vector> rows = Data.list(
            Vectors.withNumbers(225000, 1300),
            Vectors.withNumbers(335000, 1700),
            Vectors.withNumbers(450000, 2400),
            Vectors.withNumbers(220000, 1250),
            Vectors.withNumbers(350000, 1800),
            Vectors.withNumbers(435000, 2500),
            Vectors.withNumbers(275000, 1200),
            Vectors.withNumbers(345000, 1550),
            Vectors.withNumbers(500000, 2500),
            Vectors.withNumbers(230000, 1300),
            Vectors.withNumbers(310000, 1400),
            Vectors.withNumbers(525000, 2600)
    );*/

    Dataset dataset = new DefaultDataset(
            Datasets.columns()
                    .column("price", Datatype.NUMERIC)
                    .column("sqft", Datatype.NUMERIC)
                    .column("bdrms", Datatype.NUMERIC)
                    .build(),
            rows
    );

    Tad tad = Tad.builder().build();
    LinearRegression regression = new LinearRegression(tad, dataset, 0.3d, 0.003d, "price","sqft", "bdrms");
    while(regression.iterate()) {
    }
    for (int i : Numbers.range(0, 1000)) {
      if(!regression.iterate()) {
        System.out.println("Convergence reached");
        break;
      }
      //System.out.println(tad.matrices.withRow(Vectors.withNumbers(1, 2000)).product(tad.matrices.withColumn(newLtm)));
      System.out.println(String.format("%s -> %s", i+1, regression.cost));
    }


    System.exit(0);
  }


}
