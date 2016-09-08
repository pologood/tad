package org.sapia.tad.ml.cluster.kmeans;

import lombok.RequiredArgsConstructor;
import org.sapia.tad.Dataset;
import org.sapia.tad.Vector;
import org.sapia.tad.concurrent.TaskExecutor;
import org.sapia.tad.ml.distance.DistanceFunction;
import org.sapia.tad.ml.distance.DistanceFunctions;
import org.sapia.tad.transform.slice.Slices;
import org.sapia.tad.util.KVPair;

import java.util.*;
import java.util.concurrent.ExecutorService;

/**
 * Implements the K-Means clustering algorithm.
 *
 * @author yduchesne
 */
@RequiredArgsConstructor
public class KMeansClusteringAlgo {

  private final ExecutorService  executorService;
  private final Dataset          dataset;
  private final int              numClusters;

  // The only distance function that will insure conversion is the Euclidian distance
  // function, which in this context minimizes cluster variance.
  // For an interesting discussion on that topic, see:
  // http://stats.stackexchange.com/questions/81481/why-does-k-means-clustering-algorithm-use-only-euclidean-distance-metric
  private final DistanceFunction distanceFunction = DistanceFunctions.euclidian();


  public List<Cluster> run() throws InterruptedException {
    List<Dataset> partitions = Slices.partitions(dataset, numClusters);

    List<Cluster> clusters = new ArrayList<>(numClusters);

    for (int i = 0; i < partitions.size(); i++) {
      Cluster c = new Cluster(i, partitions.size(), dataset.getColumnSet().size());
      clusters.add(c);
    }

    Random rand = new Random();

    for (Dataset partition : partitions) {
      for (Vector v : partition) {
        Cluster c = clusters.get(rand.nextInt(clusters.size()));
        c.addObservation(new Observation(v));
      }
    }

    computeCentroids(clusters);
    processClusters(clusters);
    return clusters;
  }

  private void processClusters(Collection<Cluster> clusters) throws InterruptedException {
    List<KVPair<Observation, Cluster>> updates = Collections.synchronizedList(new ArrayList<>());
    do {
      updates.clear(); // making sure the list is cleared before each pass
      TaskExecutor executor = new TaskExecutor(executorService);
      clusters.forEach(
          c -> {
            executor.addTask(() ->
              c.getObservations().forEach(o -> {
                Cluster nearest = doFindNearest(distanceFunction, o, clusters);
                if (!nearest.equals(c)) {
                  updates.add(new KVPair<>(o, nearest));
                }
              })
            );
          }
      );
      executor.await();
      if (!updates.isEmpty()) {
        updates.forEach(u -> u.getKey().moveTo(u.getValue()));
        computeCentroids(clusters);
      }
    } while (!updates.isEmpty());
  }

  private Cluster doFindNearest(DistanceFunction function, Observation o, Collection<Cluster> clusters) {
    // initializing nearest to distance between observation and its current cluster
    double distanceFromCurrentCluster = function.computeDistanceBetween(o.getVector(), o.getCluster().getCentroid());
    KVPair<Double, Cluster> nearestDistanceCluster = new KVPair<>(distanceFromCurrentCluster, o.getCluster());

    for (Cluster c : clusters) {
      if (!c.equals(o.getCluster())) {
        double distance = function.computeDistanceBetween(o.getVector(), c.getCentroid());
        if (distance < nearestDistanceCluster.getKey()) {
          nearestDistanceCluster = new KVPair<Double, Cluster>(distance, c);
        }
      }
    }
    return nearestDistanceCluster.getValue();
  }

  private void computeCentroids(Collection<Cluster> clusters) throws InterruptedException {
    // computing centroids
    TaskExecutor executor = new TaskExecutor(executorService);
    for (Cluster c : clusters) {
      executor.addTask(() -> c.computeCentroid());
    }
    executor.await();
  }
}
