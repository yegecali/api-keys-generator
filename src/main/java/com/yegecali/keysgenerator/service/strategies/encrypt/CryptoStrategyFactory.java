package com.yegecali.keysgenerator.service.strategies.encrypt;

import com.yegecali.keysgenerator.exception.ApplicationException;
import com.yegecali.keysgenerator.exception.ErrorCode;
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
        if (type == null) throw ApplicationException.invalidRequest("Type is required");
        for (CryptoStrategy s : strategies) {
            if (s.getAlgorithm().equalsIgnoreCase(type)) return s;
        }
        throw ApplicationException.unknownType("No CryptoStrategy for type: " + type);
    }

    public CryptoStrategy get(CryptoRequest.TypeEnum typeEnum) {
        return get(typeEnum == null ? null : typeEnum.getValue());
    }

    public CryptoStrategy get(KeyGenerationRequest.TypeEnum typeEnum) {
        return get(typeEnum == null ? null : typeEnum.getValue());
    }
}
