package configuration;

import interfaces.lexer.Lexer;
import interfaces.services.ConfigurationService;

import java.util.concurrent.locks.ReadWriteLock;

public class Configurations {
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
