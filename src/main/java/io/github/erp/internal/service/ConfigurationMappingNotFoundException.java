package io.github.erp.internal.service;

/**
 * Thrown when mappings are not found
 */
public class ConfigurationMappingNotFoundException extends RuntimeException {

    public ConfigurationMappingNotFoundException() {
    }

    public ConfigurationMappingNotFoundException(String message) {
        super(message);
    }

    public ConfigurationMappingNotFoundException(Throwable cause) {
        super(cause);
    }

    public ConfigurationMappingNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigurationMappingNotFoundException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    
}