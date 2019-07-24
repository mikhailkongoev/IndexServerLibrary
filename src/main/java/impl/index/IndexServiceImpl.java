package impl.index;

import impl.configuration.Configurations;
import interfaces.lexer.Lexer;
import model.entities.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static impl.configuration.Configurations.em;

/**
 * Стандартная реализация {@link interfaces.services.IndexService}
 * Производит индексацию файлов с применением только лексического анализа содержимого.
 */
public class IndexServiceImpl extends AbstractIndexService {

    public IndexServiceImpl(Lexer lexer, ReadWriteLock lock) {
        super(lexer, lock);
    }

    public IndexServiceImpl(Lexer lexer) {
        this(lexer, Configurations.getIndexLock());
    }

    /**
     * Добавляет указанный файл или директорию вместе со всем её содержимым в индекс.
     * Использует сохранённый в {@link impl.index.AbstractIndexService} {@link interfaces.lexer.Lexer} для индексации
     *
     * @param path Файл или директория, которую необходимо проиндексировать
     *
     * @throws IOException Возникает при ошибке получения доступа к указанному файлу
     */
    @Override
    public void addToIndex(Path path) throws IOException {
        // File is deleted or temporal
        if (!isFileNotHidden(path)) {
            return;
        }
        lock.writeLock().lock();
        try(Stream<Path> paths = Files.walk(path).filter(this::isFileNotHidden)) {
            em.getTransaction().begin();
            paths.forEach(p -> {
                addSingleFileToIndex(p);
                if (Files.isDirectory(p)) {
                    registerDirectory(p);
                }
            });
            em.getTransaction().commit();
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Удаляет указанный файл или директорию вместе со всем её содержимым из индекса.
     * Обычно вызывается уже после удаления файла/директории в операционной системе, поэтому ориентируется на строковое значение пути.
     *
     * @param path Файл или директория, которую необходимо удалить из индекса
     */
    @Override
    public void removeFromIndex(Path path) {
        lock.writeLock().lock();
        try {
            em.getTransaction().begin();
            List<AbstractFile> filesToRemove = em.createNamedQuery(AbstractFile.class.getSimpleName() + ".findAllFilesByPath", AbstractFile.class)
                    .setParameter("path", path.toAbsolutePath().toString()).getResultList();
            List<Document> docsToRemove = em.createNamedQuery(Document.class.getSimpleName() + ".findDocumentsByFileList", Document.class)
                    .setParameter("filesList", filesToRemove.stream().filter(f -> f instanceof RegularFile).collect(Collectors.toList())).getResultList();
            em.createNamedQuery(IndexLine.class.getSimpleName() + ".removeIndexLinesByDocumentsList")
                    .setParameter("docsList", docsToRemove).executeUpdate();
            filesToRemove.forEach(em::remove);
            em.getTransaction().commit();
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Добавляет указанные по пути файл или директорию вместе со всем её содержимым в индекс.
     * Использует сохранённый в {@link impl.index.AbstractIndexService} {@link interfaces.lexer.Lexer} для индексации
     *
     * @param path Путь до файла или директории, которую необходимо проиндексировать
     *
     * @throws IOException Возникает при ошибке получения доступа к указанному файлу
     */
    @Override
    public void addToIndex(String path) throws IOException {
        addToIndex(Paths.get(path));
    }

    /**
     * Удаляет указанный по пути файл или директорию вместе со всем её содержимым из индекса.
     *
     * @param path Путь до файла или директории, которую необходимо удалить из индекса
     */
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
        Directory parent = em.find(Directory.class, path.getParent().toAbsolutePath().toString());
        newFile.setParent(parent);
        newFile.setFullPath(fullPath);
        em.persist(newFile);

        if (newFile instanceof RegularFile) {
            Document document = new Document();
            document.setFile((RegularFile) newFile);
            document.setName(fullPath);
            ((RegularFile) newFile).setDocument(document);

            em.persist(newFile);

            try {
                String content = Files.lines(path).collect(Collectors.joining());
                Collection<String> keywords = lexer.parse(content).stream().distinct().collect(Collectors.toList());
                keywords.forEach(s -> {
                    IndexLine indexLine = new IndexLine(s, document);
                    em.persist(indexLine);
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
