package pbouda.asgct;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VirtualThreadAndException {

    private static final ExecutorService VIRTUAL_EXECUTOR =
            Executors.newVirtualThreadPerTaskExecutor();

    private static final ExecutorService SINGLE_EXECUTOR =
            Executors.newSingleThreadExecutor();

    public static void main(String[] args) throws Exception {
        VIRTUAL_EXECUTOR.execute(() -> {
            throw new RuntimeException("virtual");
        });

        SINGLE_EXECUTOR.execute(() -> {
            throw new RuntimeException("single");
        });

        Thread.currentThread().join();
    }
}
