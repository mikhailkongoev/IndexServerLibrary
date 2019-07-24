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

public abstract class AbstractIndexService implements IndexService {
    Lexer lexer;

    private WatchService watcher;

    final ReadWriteLock lock;

    private final Map<WatchKey, Path> keys = new ConcurrentHashMap<>();

    {
        try {
            watcher = FileSystems.getDefault().newWatchService();
            Runnable fileSystemListener = new Runnable() {
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

    AbstractIndexService(Lexer lexer, ReadWriteLock lock) throws IOException {
        this.lexer = lexer;
        this.lock = lock;
    }

    public void setLexer(Lexer lexer) {
        this.lexer = lexer;
    }

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

    void unregisterDirectory(Path dir) {
        keys.entrySet().stream().filter(e -> e.getValue().equals(dir)).findAny().ifPresent(e -> {
            e.getKey().cancel();
            keys.remove(e.getKey());
        });
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
