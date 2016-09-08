package org.sapia.tad.ml.cluster.kmeans;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.sapia.tad.Dataset;
import org.sapia.tad.Datatype;
import org.sapia.tad.Vector;
import org.sapia.tad.Vectors;
import org.sapia.tad.impl.DefaultColumn;
import org.sapia.tad.impl.DefaultDataset;
import org.sapia.tad.util.Data;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class KMeansClusteringAlgoTest {

  @Mock
  private ExecutorService executorService;
  private Dataset dataset;

  private KMeansClusteringAlgo algo;

  @Before
  public void setUp() throws Exception {
    List<Vector> vectors = Data.list(
            Vectors.withNumbers(1, 2, 3),
            Vectors.withNumbers(4, 5, 6),
            Vectors.withNumbers(7, 8, 9),
            Vectors.withNumbers(40, 41, 42),
            Vectors.withNumbers(43, 44, 45),
            Vectors.withNumbers(46, 47, 48),
            Vectors.withNumbers(100, 101, 102),
            Vectors.withNumbers(103, 104, 105),
            Vectors.withNumbers(106, 107, 108)
    );
    Collections.shuffle(vectors);

    dataset = new DefaultDataset(
            Data.list(
                    new DefaultColumn(0, Datatype.STRING, "col0"),
                    new DefaultColumn(1, Datatype.STRING, "col1"),
                    new DefaultColumn(2, Datatype.STRING, "col2")
            ),
            vectors
    );

    algo = new KMeansClusteringAlgo(executorService, dataset, 3);

    doAnswer(new Answer<Future<?>>() {
      @Override
      public Future<?> answer(InvocationOnMock invocationOnMock) throws Throwable {
        Future<?> future =  mock(Future.class);
        Runnable task = invocationOnMock.getArgumentAt(0, Runnable.class);
        task.run();
        when(future.get()).thenReturn(null);
        return future;
      }
    }).when(executorService).submit(any(Runnable.class));
  }

  @After
  public void tearDown() throws Exception {
    executorService.shutdown();
  }

  @Test
  public void testRun() throws Exception {
    List<Cluster> clusters = algo.run();

    for (Cluster c : clusters) {
      assertThat(c.getObservations()).hasSize(3);
      Set<Vector> vectors = c.getObservations().stream().map(Observation::getVector).collect(Collectors.toSet());
      if (vectors.contains(Vectors.withNumbers(4, 5, 6))) {
        assertThat(vectors).contains(Vectors.withNumbers(1, 2, 3), Vectors.withNumbers(7, 8, 9));
      } else if (vectors.contains(Vectors.withNumbers(43, 44, 45))) {
        assertThat(vectors).contains(Vectors.withNumbers(40, 41, 42), Vectors.withNumbers(46, 47, 48));
      } else if (vectors.contains(Vectors.withNumbers(103, 104, 105))) {
        assertThat(vectors).contains(Vectors.withNumbers(100, 101, 102), Vectors.withNumbers(106, 107, 108));
      } else {
        throw new IllegalStateException("None of the expected vectors has been found");
      }
    }

  }
}