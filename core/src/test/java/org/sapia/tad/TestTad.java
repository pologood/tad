package org.sapia.tad;

/**
 * Use the class to create a test-only {@link Tad} instance.
 *
 * @author yduchesne
 */
public final class TestTad {

  private TestTad() {

  }

  /**
   * @return a new test-only {@link Tad} instance.
   *
   * @see MockExecutorService
   */
  public static Tad get() {
    return Tad.builder().withThreadPool(MockExecutorService.get()).build();
  }
}
