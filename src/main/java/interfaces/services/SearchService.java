package interfaces.services;

import java.nio.file.Path;
import java.util.Collection;

/**
 * Сервис, позволяющий осуществлять запросы к индексу.
 */
public interface SearchService {
    /**
     * Осуществляет запрос в индекс.
     *
     * @param query Пользовательский запрос
     * @return Коллекция файлов, удавлетворяющих запросу
     */
    Collection<Path> search(String query);
}
