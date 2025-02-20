package config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.*;

/**
 * Реализация модуля управления конфигурацией.
 */
public class ConfigurationManager implements ConfigurationManagerInterface {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String BACKUP_EXTENSION = ".backup";

    /**
     * Загружает файл и пытается распарсить его как JSON.
     */
    @Override
    public String loadConfigAsJson(Path path) throws ConfigurationException {
        try {
            String content = Files.readString(path);
            JsonNode jsonNode = objectMapper.readTree(content);
            return objectMapper.writeValueAsString(jsonNode);
        } catch (IOException e) {
            throw new ConfigurationException("Ошибка загрузки конфигурации: " + path, e);
        }
    }

    /**
     * Проверяет, что входная строка является корректным JSON.
     */
    @Override
    public boolean validateConfig(String jsonConfig) {
        try {
            objectMapper.readTree(jsonConfig);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Сохраняет данные (JSON) в целевой файл.
     * Если файл не существует, он будет создан.
     */
    @Override
    public void saveConfig(Path path, String data) throws ConfigurationException {
        try {
            Files.writeString(path, data, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new ConfigurationException("Ошибка сохранения конфигурации: " + path, e);
        }
    }

    /**
     * Создает резервную копию файла, чтобы можно было откатить изменения в случае неудачной правки конфигурации.
     */
    @Override
    public void backupConfig(Path path) throws ConfigurationException {
        Path backupPath = getBackupPath(path);
        try {
            Files.copy(path, backupPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new ConfigurationException("Ошибка создания резервной копии: " + path, e);
        }
    }

    /**
     * Восстанавливает конфигурационный файл из резервной копии.
     * Если резервной копии нет, метод выбросит исключение.
     */
    @Override
    public void rollbackConfig(Path path) throws ConfigurationException {
        Path backupPath = getBackupPath(path);
        if (!Files.exists(backupPath)) {
            throw new ConfigurationException("Резервная копия не найдена: " + backupPath);
        }
        try {
            Files.copy(backupPath, path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new ConfigurationException("Ошибка восстановления конфигурации из резервной копии: " + backupPath, e);
        }
    }

    /**
     * Формирует путь к файлу резервной копии (добавляя суффикс .backup).
     */
    private Path getBackupPath(Path path) {
        return Paths.get(path.toString() + BACKUP_EXTENSION);
    }
}