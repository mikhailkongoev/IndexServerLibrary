package interfaces.services;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Сервис, позволяющий индексировать файлы и директории и удалять их из индекса.
 */
public interface IndexService {
    /**
     * Добавляет указанный файл или директорию вместе со всем её содержимым в индекс.
     * Использует сохранённый в {@link impl.index.AbstractIndexService} {@link interfaces.lexer.Lexer} для индексации
     *
     * @param path Файл или директория, которую необходимо проиндексировать
     *
     * @throws IOException Возникает при ошибке получения доступа к указанному файлу
     */
    void addToIndex(Path path) throws IOException;

    /**
     * Удаляет указанный файл или директорию вместе со всем её содержимым из индекса.
     * Обычно вызывается уже после удаления файла/директории в операционной системе, поэтому ориентируется на строковое значение пути.
     *
     * @param path Файл или директория, которую необходимо удалить из индекса
     */
    void removeFromIndex(Path path);

    /**
     * Добавляет указанные по пути файл или директорию вместе со всем её содержимым в индекс.
     * Использует сохранённый в {@link impl.index.AbstractIndexService} {@link interfaces.lexer.Lexer} для индексации
     *
     * @param path Путь до файла или директории, которую необходимо проиндексировать
     *
     * @throws IOException Возникает при ошибке получения доступа к указанному файлу
     */
    void addToIndex(String path) throws IOException;

    /**
     * Удаляет указанный по пути файл или директорию вместе со всем её содержимым из индекса.
     *
     * @param path Путь до файла или директории, которую необходимо удалить из индекса
     */
    void removeFromIndex(String path);
}
