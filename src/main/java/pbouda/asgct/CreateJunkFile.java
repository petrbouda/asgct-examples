package pbouda.asgct;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

public class CreateJunkFile {

    public static void main(String[] args) throws IOException {
        // 50BM
        int mb50 = 50 * 1000 * 1000;
        byte[] content = new byte[mb50];

        Files.write(Path.of("temp-file.bin"), content, CREATE, TRUNCATE_EXISTING);
    }
}
