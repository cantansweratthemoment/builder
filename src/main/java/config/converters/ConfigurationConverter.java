package config.converters;

import config.ConfigurationException;

/**
 * Интерфейс для преобразования конфигураций из одного формата в другой.
 */
public interface ConfigurationConverter {
    String convertToJson(String sourceConfig) throws ConfigurationException;
}