package com.yegecali.keysgenerator.factory;

import com.yegecali.keysgenerator.crypto.strategies.CryptoStrategy;
import com.yegecali.keysgenerator.exception.NoSuchKeyGeneratorException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

@ApplicationScoped
public class CryptoStrategyFactory {

    @Inject
    Instance<CryptoStrategy> strategies;

    public CryptoStrategy get(String type) {
        if (type == null) throw new NoSuchKeyGeneratorException("Type is required");
        for (CryptoStrategy s : strategies) {
            if (s.getAlgorithm().equalsIgnoreCase(type)) return s;
        }
        throw new NoSuchKeyGeneratorException("No CryptoStrategy for type: " + type);
    }
}

