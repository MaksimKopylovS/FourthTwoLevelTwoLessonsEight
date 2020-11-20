package packageMessage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileMessage implements Message {

    private static final long serializableVersionUID = 5193392663743561680L;

    private String  fileName;
    private int sizeOfFiles = 1024*1024;
    private byte[] data;

    public String getFileName() {
        return fileName;
    }

    public byte[] getData(){
        return data;
    }

    public FileMessage(Path path) throws IOException {
        fileName = path.getFileName().toString();
        data = Files.readAllBytes(path);
    }
}
