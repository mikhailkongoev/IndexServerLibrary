package impl.index;

import interfaces.lexer.Lexer;
import interfaces.services.IndexService;

import java.io.IOException;
import java.nio.file.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.stream.Stream;

import static java.nio.file.StandardWatchEventKinds.*;

/**
 * Предок всех {@link IndexService}
 * Хранит ссылку на используемый для индексации {@link Lexer}
 * А также занимается отслеживанием изменений файловой системы по проиндексированным файлам и директориям.
 * Отслеживает изменения за счёт событий в отдельном потоке-демоне, переиндексирует все, затронутые изменениями файлы.
 */
public abstract class AbstractIndexService implements IndexService {
    Lexer lexer;

    private WatchService watcher;

    final ReadWriteLock lock;

    private final Map<WatchKey, Path> keys = new ConcurrentHashMap<>();

    {
        try {
            watcher = FileSystems.getDefault().newWatchService();
            Runnable fileSystemListener = new Runnable() {
                /**
                 * Отслеживает изменения в файловой системе.
                 * При изменении файла или директории индексирует всё его содержимое, рекурсивно.
                 * Переименование считается за удаление + создание, поэтому если папка была проиндексирована частично,
                 * затем была переименована, то она станет полностью проиндексирована.
                 */
                @Override
                public void run() {
                    while(true) {
                        // wait for key to be signalled
                        WatchKey key;
                        try {
                            key = watcher.take();
                        } catch (InterruptedException x) {
                            return;
                        }

                        lock.writeLock().lock();

                        try {

                            Path dir = keys.get(key);

                            if (dir == null) {
                                continue;
                            }

                            for (WatchEvent<?> event : key.pollEvents()) {
                                WatchEvent.Kind kind = event.kind();

                                // Context for directory entry event is the file name of entry
                                Path name = ((WatchEvent<Path>) event).context();
                                Path child = dir.resolve(name);

                                if (kind == ENTRY_DELETE) {
                                    removeFromIndex(child);
                                } else if (kind == ENTRY_CREATE) {

                                    try {
                                        addToIndex(child);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    if (Files.isDirectory(child)) {
                                        try (Stream<Path> paths = Files.walk(child)) {
                                            paths.filter(p -> isFileNotHidden(p) && Files.isDirectory(p)).forEach(AbstractIndexService.this::registerDirectory);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                } else if (kind == ENTRY_MODIFY) {

                                    removeFromIndex(child);
                                    try {
                                        addToIndex(child);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }

                            // reset key and remove from set if directory no longer accessible
                            boolean valid = key.reset();
                            if (!valid) {
                                keys.remove(key);
                            }
                        } finally {
                            lock.writeLock().unlock();
                        }
                    }
                }
            };
            Thread fileSystemListenerThread = new Thread(fileSystemListener);
            fileSystemListenerThread.setDaemon(true);
            fileSystemListenerThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    AbstractIndexService(Lexer lexer, ReadWriteLock lock) {
        this.lexer = lexer;
        this.lock = lock;
    }

    public void setLexer(Lexer lexer) {
        this.lexer = lexer;
    }

    /**
     * Добавляет директорию к отслеживаемым.
     *
     * @param dir Директория, которую необходимо начать отслеживать
     */
    void registerDirectory(Path dir) {
        WatchKey key;
        try {
            key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка ввода-вывода");
        }
        keys.put(key, dir);
    }

    /**
     * Проверяет, что обрабатываемый файл ещё не был удалён.
     * Так же работает как костыль для текстовых редакторов наподобие gedit, который создаёт промежуточные файлы со странными названиями
     * и отправляет события об их изменении, хотя они уже не существуют. Проверки на Files.exists(path) в этом случае не хватает.
     *
     * @param path
     * @return
     */
    boolean isFileNotHidden(Path path) {
        return Files.exists(path) && !path.getFileName().toString().startsWith(".");
    }
}
