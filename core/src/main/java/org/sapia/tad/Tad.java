package org.sapia.tad;

import org.sapia.tad.matrix.Matrices;
import org.sapia.tad.modules.MathModule;
import org.sapia.tad.modules.TransformModule;
import org.sapia.tad.stat.Stats;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Entry point into the framework's functionality.
 *
 * @author yduchesne
 */
public class Tad {

  private class TadContextImpl implements TadContext {

    @Override
    public Matrices getMatrixModule() {
      return matrices;
    }

    @Override
    public ExecutorService getThreadPool() {
      return executorService;
    }
  }

  private ExecutorService executorService;

  public final TadContext context;

  // --------------------------------------------------------------------------
  // Modules, accessible as public/final instance variables

  public final Stats           stats;
  public final MathModule      maths;
  public final TransformModule xf;
  public final Matrices matrices;

  private Tad(ExecutorService executorService) {
    this.executorService = executorService;
    this.context = new TadContextImpl();

    stats    = new Stats(context);
    maths    = new MathModule(context);
    xf       = new TransformModule(context);
    matrices = new Matrices(context);
  }

  /**
   * @return a new {@link TadBuilder}.
   */
  public static TadBuilder builder() {
    return new TadBuilder();
  }

  // --------------------------------------------------------------------------
  // Builder class

  public static final class TadBuilder {

    private ExecutorService executorService;

    private TadBuilder() {
    }

    /**
     * Allows specifying an {@link ExecutorService} acting as a thread pool to perform operations in
     * parallel.
     *
     * @param executorService the {@link ExecutorService} to use.
     * @return this instance.
     */
    public TadBuilder withThreadPool(ExecutorService executorService) {
      this.executorService = executorService;
      return this;
    }

    /**
     * @return a new {@link Tad}.
     */
    public Tad build() {
      if (executorService == null) {
        executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
      }
      return new Tad(executorService);
    }
  }
}
