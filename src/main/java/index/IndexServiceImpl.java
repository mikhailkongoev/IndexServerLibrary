package index;

import interfaces.lexer.Lexer;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.locks.ReadWriteLock;

public class IndexServiceImpl extends AbstractIndexService {

    private final ReadWriteLock lock;

    public IndexServiceImpl(Lexer lexer, ReadWriteLock lock) {
        super(lexer);
        this.lock = lock;
    }

    @Override
    public void addToIndex(Path path) {
        lock.writeLock().lock();
        try {
            //TODO: add file or directory into index
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void removeFromIndex(Path path) {
        lock.writeLock().lock();
        try {
            //TODO: remove file or directory from index
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void addToIndex(String path) {
        addToIndex(Paths.get(path));
    }

    @Override
    public void removeFromIndex(String path) {
        removeFromIndex(Paths.get(path));
    }
}
