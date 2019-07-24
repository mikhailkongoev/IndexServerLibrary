package impl.configuration;

import interfaces.lexer.Lexer;
import interfaces.services.ConfigurationService;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * Утилитарный класс, позволяющий получить интерфейс работы с индексом, доступ к глобальной заглушке, а так же различным конфигурационным настройкам.
 */
public class Configurations {
    private static final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("org.jetbrains.data.jpa.hibernate");

    public static final EntityManager em = entityManagerFactory.createEntityManager();

    public static ConfigurationService createConfiguration() {
        return new ConfigurationServiceImpl();
    }

    /**
     * Предоставляет интерфейс взаимодействия с индексом. Обычно с него начинается работа с индексом.
     * Чаще всего один вызов на весь индекс, или один вызов на сессию.
     *
     * @param lexer Лексер для взаимодействия с индексом. Используется при индексации и поиске.
     * @return Интерфейс взаимодействия с индексом
     */
    public static ConfigurationService createConfiguration(Lexer lexer) {
        return new ConfigurationServiceImpl(lexer);
    }

    /**
     * @return Глобальная ReadWrite заглушка на индекс.
     */
    public static ReadWriteLock getIndexLock() {
        return ConfigurationServiceImpl.lock;
    }
}
