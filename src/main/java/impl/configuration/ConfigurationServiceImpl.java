package impl.configuration;

import impl.index.AbstractIndexService;
import impl.index.IndexServiceImpl;
import impl.lexer.SimpleTokenizer;
import impl.search.AbstractSearchService;
import impl.search.SearchServiceImpl;
import interfaces.lexer.Lexer;
import interfaces.services.ConfigurationService;
import interfaces.services.IndexService;
import interfaces.services.SearchService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Сервис, предоставляющий интерфейс взаимодействия с индексом. Позволяет создавать сервисы индексации и поиска и поддерживать их
 * в согласованном друг с другом порядке. То есть в любой момент времени использовать общий {@link Lexer}.
 */
public class ConfigurationServiceImpl implements ConfigurationService {

    private Lexer lexer;

    static final ReadWriteLock lock = new ReentrantReadWriteLock();

    private Collection<AbstractIndexService> indexServices = new ArrayList<>();

    private Collection<AbstractSearchService> searchServices = new ArrayList<>();

    ConfigurationServiceImpl(Lexer lexer) {
        setLexer(lexer);
    }

    ConfigurationServiceImpl() {
    }

    /**
     * Устанавливает переданный лексер в качестве используемого для всех сервисов, созданных через текущий экземпляр, а
     * также для создаваемых в будущем.
     *
     * @param lexer Устанавливаемый лексер
     */
    @Override
    public synchronized void setLexer(Lexer lexer) {
        lock.writeLock().lock();
        try {
            this.lexer = lexer;
            indexServices.forEach(i -> i.setLexer(lexer));
            searchServices.forEach(i -> i.setLexer(lexer));
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * @return Экземпляр сервиса, позволяющего добавлять и удалять из индекса файлы и директории
     */
    @Override
    public synchronized IndexService createIndexService() {
        IndexServiceImpl indexService = new IndexServiceImpl(lexer == null ? new SimpleTokenizer() : lexer, lock);
        indexServices.add(indexService);
        return indexService;
    }

    /**
     * @return Экземпляр сервиса, позволяющего осуществлять запросы к индексу.
     */
    @Override
    public synchronized SearchService createSearchService() {
        SearchServiceImpl searchService = new SearchServiceImpl(lexer == null ? new SimpleTokenizer() : lexer, lock);
        searchServices.add(searchService);
        return searchService;
    }
}
