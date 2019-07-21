package impl.configuration;

import interfaces.lexer.Lexer;
import interfaces.services.ConfigurationService;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.concurrent.locks.ReadWriteLock;

public class Configurations {
    public static final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("org.jetbrains.data.jpa.hibernate");

    public static final EntityManager em = entityManagerFactory.createEntityManager();

    public static ConfigurationService createConfiguration() {
        return new ConfigurationServiceImpl();
    }

    public static ConfigurationService createConfiguration(Lexer lexer) {
        return new ConfigurationServiceImpl(lexer);
    }

    public static ReadWriteLock getIndexLock() {
        return ConfigurationServiceImpl.lock;
    }
}
