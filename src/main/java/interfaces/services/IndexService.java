package interfaces.services;

import java.nio.file.Path;

public interface IndexService {
    void addToIndex(Path path);

    void removeFromIndex(Path path);

    void addToIndex(String path);

    void removeFromIndex(String path);
}
