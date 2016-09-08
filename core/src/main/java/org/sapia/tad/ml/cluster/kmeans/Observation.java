package org.sapia.tad.ml.cluster.kmeans;

import lombok.*;
import org.sapia.tad.Vector;

/**
 * Wraps a vector.
 */
@RequiredArgsConstructor
@Getter
@ToString(of = "vector")
public class Observation {

  @Setter(AccessLevel.PACKAGE)
  private Cluster cluster;
  private final Vector vector;

  void moveTo(Cluster newCluster) {
    cluster.removeObservation(this);
    newCluster.addObservation(this);
  }

}
