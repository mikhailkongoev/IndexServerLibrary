package impl.search;

import impl.configuration.Configurations;
import interfaces.lexer.Lexer;
import model.entities.Document;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.stream.Collectors;

import static impl.configuration.Configurations.em;

public class SearchServiceImpl extends AbstractSearchService {

    private ReadWriteLock lock;

    public SearchServiceImpl(Lexer lexer, ReadWriteLock lock) {
        super(lexer);
        this.lock = lock;
    }

    public SearchServiceImpl(Lexer lexer) {
        this(lexer, Configurations.getIndexLock());
    }

    @Override
    public Collection<Path> search(String query) {
        lock.readLock().lock();
        try {
            Collection<String> keywords = lexer.parse(query);

            Collection<Document> docs = em.createQuery("select distinct il.indexLinePK.document from IndexLine il " +
                    "where il.indexLinePK.keyword in (:keywords)", Document.class).setParameter("keywords", keywords).getResultList();
            return docs.stream().map(d -> Paths.get(d.getFile().getFullPath())).collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }
}
