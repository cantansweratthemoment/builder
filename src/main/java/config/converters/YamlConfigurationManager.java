package config.converters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import config.ConfigurationException;
import config.ConfigurationManagerInterface;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Реализация ConfigurationManager для работы с YAML.
 */
public class YamlConfigurationManager implements ConfigurationManagerInterface {
    private final ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());

    @Override
    public String loadConfigAsJson(Path path) throws ConfigurationException {
        try {
            String yamlContent = Files.readString(path);
            Object yamlData = yamlMapper.readValue(yamlContent, Object.class);
            return new ObjectMapper().writeValueAsString(yamlData);
        } catch (IOException e) {
            throw new ConfigurationException("Ошибка загрузки YAML-конфигурации: " + path, e);
        }
    }

    @Override
    public boolean validateConfig(String jsonConfig) {
        try {
            new ObjectMapper().readTree(jsonConfig);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public void saveConfig(Path path, String data) throws ConfigurationException {
        try {
            Files.writeString(path, data);
        } catch (IOException e) {
            throw new ConfigurationException("Ошибка сохранения YAML-конфигурации: " + path, e);
        }
    }

    @Override
    public void backupConfig(Path path) throws ConfigurationException, IOException {
        Files.copy(path, Path.of(path.toString() + ".backup"));
    }

    @Override
    public void rollbackConfig(Path path) throws ConfigurationException, IOException {
        Path backupPath = Path.of(path.toString() + ".backup");
        Files.copy(backupPath, path);
    }
}
