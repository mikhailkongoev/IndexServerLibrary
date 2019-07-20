package interfaces.services;

import java.nio.file.Path;
import java.util.Collection;

public interface SearchService {
    Collection<Path> search(String query);
}
