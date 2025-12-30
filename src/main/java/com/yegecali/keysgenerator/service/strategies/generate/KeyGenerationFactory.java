package com.yegecali.keysgenerator.service.strategies.generate;

import com.yegecali.keysgenerator.exception.ApplicationException;
import com.yegecali.keysgenerator.exception.ErrorCode;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

@ApplicationScoped
public class KeyGenerationFactory {

    private final Instance<KeyGenerator> strategies;

    @Inject
    public KeyGenerationFactory(Instance<KeyGenerator> strategies) {
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
        throw ApplicationException.unknownType("No KeyGenerator registered for type: " + type);
    }
}
