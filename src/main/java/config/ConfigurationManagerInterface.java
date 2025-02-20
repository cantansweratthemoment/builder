package config;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Интерфейс для управления конфигурацией системы сборки.
 */
public interface ConfigurationManagerInterface {

    /**
     * Загружает конфигурацию из указанного файла и возвращает её в виде JSON-строки.
     */
    String loadConfigAsJson(Path path) throws ConfigurationException;

    /**
     * Проверяет корректность структуры JSON, чтобы убедиться, что конфигурация готова к использованию.
     */
    boolean validateConfig(String jsonConfig);

    /**
     * Сохраняет переданные данные в файл конфигурации.
     */
    void saveConfig(Path path, String data) throws ConfigurationException;

    /**
     * Создает резервную копию текущего файла конфигурации.
     */
    void backupConfig(Path path) throws ConfigurationException, IOException;

    /**
     * Откатывает файл конфигурации к последней резервной копии.
     */
    void rollbackConfig(Path path) throws ConfigurationException, IOException;
}
