package search;

import interfaces.lexer.Lexer;

import java.nio.file.Path;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;

public class SearchServiceImpl extends AbstractSearchService {

    private ReadWriteLock lock;

    public SearchServiceImpl(Lexer lexer, ReadWriteLock lock) {
        super(lexer);
        this.lock = lock;
    }

    @Override
    public Collection<Path> search(String query) {
        lock.readLock().lock();
        try {
            Collection<String> keywords = lexer.parse(query);

            Set<Path> relevantFiles = new HashSet<>();
            for (String keyword: keywords) {
                //TODO: query to index, get documents, then get files
            }
            return relevantFiles;
        } finally {
            lock.readLock().unlock();
        }
    }
}
