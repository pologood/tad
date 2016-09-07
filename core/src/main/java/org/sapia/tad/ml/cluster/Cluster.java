package org.sapia.tad.ml.cluster;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.sapia.tad.Vector;
import org.sapia.tad.Vectors;
import org.sapia.tad.util.Checks;
import org.sapia.tad.value.Value;
import org.sapia.tad.value.Values;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author yduchesne
 */
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@ToString(of = {"clusterIndex", "observations", "centroid"})
public class Cluster {

  @Getter
  private final int clusterIndex;
  private final Set<Observation> observations;
  private final int vectorLen;
  private Vector centroid;

  /**
   * @param clusterIndex a cluster index.
   * @param expectedCapacity some expected cluster capacity (based on the estimated number of observations per cluster).
   * @param vectorLen the length of the vectors that this instance is meant to process.
   */
  Cluster(int clusterIndex, int expectedCapacity, int vectorLen) {
    this.clusterIndex = clusterIndex;
    this.observations = new HashSet(expectedCapacity + 1);
    this.vectorLen = vectorLen;
  }

  /**
   * Computes this instance's centroid {@link Vector}, which will hold a vector of the means corresponding to this
   * instance's vectors.
   */
  void computeCentroid() {
    Double[] means = new Double[vectorLen];
    for (int i = 0; i < means.length; i++) {
      means[i] = new Double(0);
    }
    for (Observation o : observations) {
      for (int i = 0; i < o.getVector().size(); i++) {
        Value val = o.getVector().get(i);
        means[i] = means[i] + (val.isNumeric() ? val.get() : 0);
      }
    }
    if (!observations.isEmpty()) {
      for (int i = 0; i < means.length; i++) {
        means[i] = means[i] / observations.size();
      }
    }
    centroid = Vectors.vector(Values.array(means));
  }

  /**
   * @return the {@link Vector} corresponding to this instance's centroid.
   */
  Vector getCentroid() {
    Checks.illegalState(centroid == null, "Centroid is not set: make sure to call this instance's computeCentroid() method");
    return centroid;
  }

  /**
   * Adds the given observation to this instance (associating it to this cluster).
   *
   * @param obs an {@link Observation} to add.
   * @see Observation#setCluster(Cluster)
   */
  void addObservation(Observation obs) {
    if(observations.add(obs)) {
      obs.setCluster(this);
    }
  }

  /**
   * Removes the given observation from this instance (disassociating it from this cluster).
   *
   * @param obs an {@link Observation} to remove.
   * @see Observation#setCluster(Cluster)
   */
  void removeObservation(Observation obs) {
    if(observations.remove(obs)) {
      obs.setCluster(null);
    }
  }

  /**
   * @return this instance's observations, in an unmodifiable {@link Set}.
   */
  Set<Observation> getObservations() {
    return Collections.unmodifiableSet(observations);
  }
}
