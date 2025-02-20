package config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

/**
 * Класс для чтения конфигурационных файлов в виде сырой строки.
 * Дальнейшая обработка происходит в ConfigurationManager.
 */
public class ConfigFileReader {
    public Optional<String> readConfigFile(Path path) {
        try {
            String content = Files.readString(path);
            return Optional.of(content);
        } catch (IOException e) {
            System.err.println("Ошибка чтения конфигурационного файла: " + e.getMessage());
            return Optional.empty();
        }
    }
}

