package interfaces.services;

import interfaces.lexer.Lexer;

import java.io.IOException;

/**
 * Интерфейс взаимодействия с индексом. Предоставляет интерфейсы индексирования и поиска, использующие общий @{@link Lexer},
 * то есть работающие в согласованном режиме
 */
public interface ConfigurationService {
    /**
     * Установить лексер, используемый для индексирования и поиска.
     *
     * @param lexer Устанавливаемый лексер
     */
    void setLexer(Lexer lexer);

    /**
     * Предоставляет интерфейс для индексации файлов и директорий
     *
     * @return
     */
    IndexService createIndexService();

    /**
     * Предоставляет интерфейс для осуществления запросов к индексу.
     *
     * @return
     */
    SearchService createSearchService();
}
