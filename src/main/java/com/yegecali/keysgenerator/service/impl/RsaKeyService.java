package com.yegecali.keysgenerator.service;

import com.yegecali.keysgenerator.dto.KeyGenerationRequest;
import com.yegecali.keysgenerator.model.KeyModel;
import com.yegecali.keysgenerator.service.strategies.generate.KeyGenerationFactory;
import com.yegecali.keysgenerator.service.strategies.generate.AbstractKeyGenerator;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class RsaKeyService extends AbstractKeyGenerator implements KeyGenerationService {

    private final KeyGenerationFactory strategyFactory;

    @Inject
    public RsaKeyService(KeyGenerationFactory strategyFactory) {
        this.strategyFactory = strategyFactory;
    }

    @Override
    public String getType() {
        return "RSA";
    }

    @Override
    public KeyModel generate(KeyGenerationRequest request) {
        try {
            // usa la fábrica para obtener la estrategia concreta de generación
            com.yegecali.keysgenerator.service.strategies.generate.KeyGenerator strategy =
                    strategyFactory.get(request.getType());
            return strategy.generate(request);
        } catch (Exception e) {
            getLogger().error("Error generating keys", e);
            throw wrapException("Failed to generate keys", e);
        }
    }
}
