package com.yegecali.keysgenerator.service.strategies;

import com.yegecali.keysgenerator.exception.NoSuchKeyGeneratorException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

@ApplicationScoped
public class KeyGenerationStrategyFactory {

    @Inject
    Instance<KeyGenerationStrategy> strategies;

    public KeyGenerationStrategy get(String type) {
        if (type == null) {
            type = "RSA";
        }
        for (KeyGenerationStrategy s : strategies) {
            if (s.getType().equalsIgnoreCase(type)) {
                return s;
            }
        }
        throw new NoSuchKeyGeneratorException("No KeyGenerationStrategy registered for type: " + type);
    }
}

