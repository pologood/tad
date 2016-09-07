package org.sapia.tad.concurrent;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Eases the work of submitting tasks to an {@link ExecutorService} and waiting for the completion of these tasks.
 */
@RequiredArgsConstructor
public class TaskExecutor {

  private final ExecutorService executor;

  private final List<Future<?>> executingTasks = new ArrayList<>();

  public TaskExecutor addTask(Runnable task) {
    Future<?> result = executor.submit(task);
    executingTasks.add(result);
    return this;
  }

  public void await() throws InterruptedException {
    for (Future<?> f : executingTasks) {
      try {
        f.get();
      } catch (ExecutionException e) {
        throw new IllegalStateException("Error occured while waiting for task completion", e);
      }
    }
  }
}
