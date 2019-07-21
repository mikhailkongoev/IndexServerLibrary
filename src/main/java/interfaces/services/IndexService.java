package interfaces.services;

import java.io.IOException;
import java.nio.file.Path;

public interface IndexService {
    void addToIndex(Path path) throws IOException;

    void removeFromIndex(Path path);

    void addToIndex(String path) throws IOException;

    void removeFromIndex(String path);
}
