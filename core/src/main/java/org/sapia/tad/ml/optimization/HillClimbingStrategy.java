package org.sapia.tad.ml.optimization;

import org.sapia.tad.MutableVector;
import org.sapia.tad.MutableVectors;
import org.sapia.tad.Vector;
import org.sapia.tad.Vectors;
import org.sapia.tad.ml.error.CumulativeError;
import org.sapia.tad.ml.error.MseCumulativeError;
import org.sapia.tad.util.Numbers;
import org.sapia.tad.value.NumericValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

/**
 * Implements basic hill climbing: internally uses 5 predefined slope values and tries each one: in the end the best
 * one is selected.
 *
 * @author yduchesne
 */
public class HillClimbingStrategy implements OptimizationStrategy {

  private double initialVelocity;
  private double acceleration;
  private Supplier<CumulativeError> errorSupplier;
  private MutableVector stepSize;
  private Vector ltm;

  public HillClimbingStrategy(double initialVelocity, double acceleration, Supplier<CumulativeError> errorSupplier) {
    this.initialVelocity = initialVelocity;
    this.acceleration    = acceleration;
    this.errorSupplier   = errorSupplier;
  }

  @Override
  public Vector iterate(Vector ideal, Vector actual) {
    if (stepSize == null) {
      stepSize = MutableVectors.copyOf(Vectors.withNumbers(Numbers.repeat(initialVelocity, ideal.size())));
    }
    if (ltm == null) {
      ltm = Vectors.withNumbers(Numbers.repeat(1, ideal.size()));
    }
    Vector candidates = Vectors.withNumbers(
            -acceleration, -1 / acceleration, 0, 1 / acceleration, acceleration
    );

    for (int i = 0; i < actual.size(); i++) {
      double bestCandidateErrorValue = Double.MAX_VALUE;

      for (int j = 0; j < candidates.size(); j++) {
        MutableVector ltmCopy    = MutableVectors.copyOf(ltm);
        MutableVector actualCopy = MutableVectors.copyOf(actual);

        // measuring actual error
        CumulativeError actualError = errorSupplier.get();
        actualCopy.set(i, NumericValue.product(actual.get(i), ltmCopy.get(i)));
        actualError.accumulate(ideal, actualCopy);
        double actualErrorValue = actualError.get();

        // measuring hypothetical error
        double hypothesisCoefficient = ltm.get(i).get() + stepSize.get(i).get() * candidates.get(j).get();
        ltmCopy.set(i, hypothesisCoefficient);
        CumulativeError hypothesisError = errorSupplier.get();
        actualCopy.set(i, NumericValue.product(actual.get(i), ltmCopy.get(i)));
        hypothesisError.accumulate(ideal, actual);
        double hypothesisErrorValue = hypothesisError.get();

        if (hypothesisErrorValue < actualErrorValue &&
            hypothesisErrorValue < bestCandidateErrorValue) {
          bestCandidateErrorValue = hypothesisErrorValue;
          ltmCopy.set(i, hypothesisCoefficient);
          stepSize.set(i, stepSize.get(i).get() * candidates.get(j).get());
        }
        ltm = ltmCopy;
      }
   }

    return ltm;
  }

  public static void main(String[] args) {
    Vector ltm   = Vectors.withNumbers(Numbers.repeat(1, 1));
    Vector ideal  = Vectors.withNumbers(50);
    List<Vector> actuals = new ArrayList<>(10);
    for (int i : Numbers.range(0, 10)) {
      actuals.add(
              Vectors.withNumbers(Numbers.randomSpread(50, 10))
      );
    }
    /*for (int i : Numbers.range(0, 3)) {
      actuals.add(ideal);
    }*/

    Collections.shuffle(actuals);

    HillClimbingStrategy strategy = new HillClimbingStrategy(0.5, 0.5, () -> new MseCumulativeError());
    for (int i = 0; i < 100; i++) {
      for (int j = 0; j < actuals.size(); j++) {
        Vector actual = actuals.get(j);
        Vector newLtm = strategy.iterate(ideal, actual);

        System.out.println(newLtm);
        if (ltm.equals(newLtm)) {
          System.out.println("Algorithm converged");
          System.exit(0);
        } else {
          ltm = newLtm;
        }
      }
    }


  }
}
