package com.yegecali.keysgenerator.exception;

import java.util.HashMap;
import java.util.Map;

/**
 * Builder para crear ApplicationException con configuración avanzada (metadatos, estado personalizado).
 *
 * Uso:
 * <pre>
 * ApplicationException exception = ApplicationExceptionBuilder
 *     .of(ErrorCode.KEY_GENERATION_FAILED)
 *     .message("Failed to generate RSA key")
 *     .metadata("size", "4096")
 *     .metadata("algorithm", "RSA")
 *     .cause(originalException)
 *     .build();
 * </pre>
 */
public class ApplicationExceptionBuilder {
    private final ErrorCode errorCode;
    private String message;
    private int status;
    private final Map<String, String> metadata = new HashMap<>();
    private Throwable cause;

    private ApplicationExceptionBuilder(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.status = errorCode.getDefaultStatus();
    }

    /**
     * Crea un nuevo builder para la excepción con el ErrorCode especificado.
     */
    public static ApplicationExceptionBuilder of(ErrorCode errorCode) {
        return new ApplicationExceptionBuilder(errorCode);
    }

    /**
     * Define el mensaje de la excepción.
     */
    public ApplicationExceptionBuilder message(String message) {
        this.message = message;
        return this;
    }

    /**
     * Define el código de estado HTTP personalizado.
     */
    public ApplicationExceptionBuilder status(int status) {
        this.status = status;
        return this;
    }

    /**
     * Define la causa raíz de la excepción.
     */
    public ApplicationExceptionBuilder cause(Throwable cause) {
        this.cause = cause;
        return this;
    }

    /**
     * Agrega un metadato individual.
     */
    public ApplicationExceptionBuilder metadata(String key, String value) {
        this.metadata.put(key, value);
        return this;
    }

    /**
     * Agrega múltiples metadatos de una vez.
     */
    public ApplicationExceptionBuilder metadata(Map<String, String> metadata) {
        this.metadata.putAll(metadata);
        return this;
    }

    /**
     * Construye la ApplicationException con la configuración establecida.
     */
    public ApplicationException build() {
        if (message == null || message.isEmpty()) {
            message = "An error occurred";
        }
        return new ApplicationException(errorCode, message, status,
            metadata.isEmpty() ? null : metadata, cause);
    }
}

