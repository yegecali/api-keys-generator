package com.yegecali.keysgenerator.factory;

import com.yegecali.keysgenerator.service.strategies.encrypt.CryptoStrategy;
import com.yegecali.keysgenerator.exception.NoSuchKeyGeneratorException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import com.yegecali.keysgenerator.openapi.model.CryptoRequest;
import com.yegecali.keysgenerator.openapi.model.KeyGenerationRequest;

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

    public CryptoStrategy get(CryptoRequest.TypeEnum typeEnum) {
        return get(typeEnum == null ? null : typeEnum.getValue());
    }

    public CryptoStrategy get(KeyGenerationRequest.TypeEnum typeEnum) {
        return get(typeEnum == null ? null : typeEnum.getValue());
    }
}
