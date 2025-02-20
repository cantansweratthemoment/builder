package config;

/**
 * Это исключение используется при проблемах с загрузкой или сохранением конфигурационных данных.
 */
public class ConfigurationException extends Exception {

    public ConfigurationException(String message) {
        super(message);
    }

    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
