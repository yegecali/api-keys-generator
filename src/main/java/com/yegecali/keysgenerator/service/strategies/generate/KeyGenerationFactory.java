package com.yegecali.keysgenerator.service.strategies;

import com.yegecali.keysgenerator.exception.NoSuchKeyGeneratorException;
import com.yegecali.keysgenerator.service.KeyGenerator;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

@ApplicationScoped
public class KeyGenerationStrategyFactory {

    private final Instance<KeyGenerator> strategies;

    @Inject
    public KeyGenerationStrategyFactory(Instance<KeyGenerator> strategies) {
        this.strategies = strategies;
    }

    public KeyGenerator get(String type) {
        if (type == null) {
            type = "RSA";
        }
        for (KeyGenerator s : strategies) {
            if (s.getType().equalsIgnoreCase(type)) {
                return s;
            }
        }
        throw new NoSuchKeyGeneratorException("No KeyGenerator registered for type: " + type);
    }
}
