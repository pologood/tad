package org.sapia.tad;

import org.sapia.tad.matrix.Matrices;

import java.util.concurrent.ExecutorService;

/**
 * Provides access to core objects required by modules (among others).
 *
 * @author yduchesne
 */
public interface TadContext {

  /**
   * @return the {@link ExecutorService} acting as a thread pool to be used for parallel operations.
   */
  public ExecutorService getThreadPool();

  /**
   * @return the {@link Matrices} module.
   */
  public Matrices getMatrixModule();
}
