package pbouda.asgct;

import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class InfiniteSingleVirtualThread {

    private static final ExecutorService EXECUTOR =
            Executors.newVirtualThreadPerTaskExecutor();

    private static final int MB50 = 50 * 1000 * 1000;
    private static final byte[] CONTENT = new byte[MB50];

    public static void main(String[] args) throws Exception {
        System.out.println("Click! " + ProcessHandle.current().pid());
        System.in.read();

        spawnVirtualThread();

        Thread.currentThread().join();
    }

    private static void spawnVirtualThread() {
        EXECUTOR.execute(() -> {
            while (true) {
                arrayCopying();
            }
        });
    }

    private static void arrayCopying() {
        byte[] newBytes = new byte[MB50];
        System.arraycopy(CONTENT, 0, newBytes, 0, MB50);
    }
}
