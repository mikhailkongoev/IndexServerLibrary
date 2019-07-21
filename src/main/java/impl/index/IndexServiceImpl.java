package impl.index;

import impl.configuration.Configurations;
import interfaces.lexer.Lexer;
import model.entities.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static impl.configuration.Configurations.em;

public class IndexServiceImpl extends AbstractIndexService {

    private final ReadWriteLock lock;

    public IndexServiceImpl(Lexer lexer, ReadWriteLock lock) {
        super(lexer);
        this.lock = lock;
    }

    public IndexServiceImpl(Lexer lexer) {
        this(lexer, Configurations.getIndexLock());
    }

    @Override
    public void addToIndex(Path path) throws IOException {
        lock.writeLock().lock();
        try(Stream<Path> paths = Files.walk(path)) {
            em.getTransaction().begin();
            paths.forEach(this::addSingleFileToIndex);
            em.getTransaction().commit();
            em.createQuery("from IndexLine", IndexLine.class).getResultList().forEach(System.out::println);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void removeFromIndex(Path path) {
        lock.writeLock().lock();
        try {
            //TODO: remove file or directory from impl.index
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void addToIndex(String path) throws IOException {
        addToIndex(Paths.get(path));
    }

    @Override
    public void removeFromIndex(String path) {
        removeFromIndex(Paths.get(path));
    }

    private void addSingleFileToIndex(Path path) {
        if (!Files.exists(path)) {
            throw new IllegalArgumentException("Файл по указанному адресу не найден!");
        }

        String fullPath = path.toAbsolutePath().toString();

        // This file is already indexed
        if (em.find(AbstractFile.class, fullPath) != null) {
            return;
        }

        AbstractFile newFile;
        if (Files.isDirectory(path)) {
            newFile = new Directory();
        } else {
            newFile = new RegularFile();
            ((RegularFile) newFile).setFileFormat(RegularFile.FileFormat.TXT);
        }
        Directory parent = em.find(Directory.class, fullPath);
        newFile.setParent(parent);
        newFile.setFullPath(fullPath);
        em.persist(newFile);

        if (newFile instanceof RegularFile) {
            Document document = new Document();
            document.setFile((RegularFile) newFile);
            document.setName(fullPath);
            em.persist(document);

            try {
                String content = Files.lines(path).collect(Collectors.joining());
                Collection<String> keywords = lexer.parse(content).stream().distinct().collect(Collectors.toList());
                keywords.forEach(s -> {
                    IndexLine indexLine = new IndexLine(s, document);
                    em.persist(indexLine);
                });
            } catch (IOException e) {
                throw new RuntimeException("Ошибка ввода-вывода!");
            }
        }
    }
}
