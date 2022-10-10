package pbouda.asgct;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LeveledVirtualThread {

    private static final Path FILE = Path.of("temp-file.bin");

    private static final ExecutorService EXECUTOR =
            Executors.newVirtualThreadPerTaskExecutor();

    public static void main(String[] args) throws IOException {
        System.out.println("Click! " + ProcessHandle.current().pid());
        System.in.read();

        while (true) {
            spawnVirtualThread();
        }
    }

    private static void spawnVirtualThread() {
        EXECUTOR.execute(() -> {
            doSomeBlocking();

            EXECUTOR.execute(() -> doSomeBlocking2());
        });
    }

    private static void doSomeBlocking() {
        extracted();
    }

    private static void doSomeBlocking2() {
        extracted();
    }

    private static void extracted() {
        try {
            // new RuntimeException().printStackTrace();
            byte[] bytes = Files.readAllBytes(FILE);
            System.out.println("READ: " + bytes.length);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
