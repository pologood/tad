package org.sapia.tad;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.concurrent.*;

import static org.mockito.Mockito.*;

/**
 * Created by yanic on 9/11/2016.
 */
public final class MockExecutorService {

  private MockExecutorService() {

  }

  public static ExecutorService get() {
    ExecutorService mock = mock(ExecutorService.class);

    doAnswer(new Answer<Future<?>>() {
      @Override
      public Future<?> answer(InvocationOnMock invocationOnMock) throws Throwable {
        Runnable task = invocationOnMock.getArgumentAt(0, Runnable.class);
        try {
          task.run();
        } catch (RuntimeException e) {
          ExecutionException err = new ExecutionException("Error executing task: " + task, e);
          Future<?> future = mock(Future.class);
          when(future.get()).thenThrow(err);
          when(future.get(anyLong(), any(TimeUnit.class))).thenThrow(err);
          return future;
        }
        Future<?> future = mock(Future.class);
        return future;
      }
    }).when(mock).submit(any(Runnable.class));

    doAnswer(new Answer<Future<?>>() {
      @Override
      public Future<?> answer(InvocationOnMock invocationOnMock) throws Throwable {
        Callable task = invocationOnMock.getArgumentAt(0, Callable.class);
        try {
          Object response = task.call();
          Future<?> future = mock(Future.class);
          when(future.get()).thenReturn(response);
          when(future.get(anyLong(), any(TimeUnit.class))).thenReturn(response);
          return future;
        } catch (RuntimeException e) {
          ExecutionException err = new ExecutionException("Error executing task: " + task, e);
          Future<?> future = mock(Future.class);
          when(future.get()).thenThrow(err);
          when(future.get(anyLong(), any(TimeUnit.class))).thenThrow(err);
          return future;
        }
      }
    }).when(mock).submit(any(Callable.class));
    return mock;
  }


}
