package la.serendipity;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.jupiter.api.Test;

public class WaitNotifyTest {
    final Object obj = new Object();

    @Test
    public void waitObj() throws InterruptedException {
        obj.wait();
    }

    @Test
    public void test() throws InterruptedException, ExecutionException {

        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        final Future<Object> future = executorService.submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                System.out.println("Before synchronized");
                synchronized (obj) {
                    System.out.println("After synchronized");
                    Thread.sleep(100);
                    System.out.println("Before Wait");
                    waitObj();
                    System.out.println("After Wait");
                    Thread.sleep(100);
                }
                return "OK";
            }
        });

        synchronized (obj) {
            Thread.sleep(1000);
        }

        Thread.sleep(100);

        synchronized (obj) {
            System.out.println("Before notify");
            obj.notify();
            System.out.println("After notify");
            Thread.sleep(1000);
            System.out.println("Finish synchronized");
        }

        future.get();
    }
}
