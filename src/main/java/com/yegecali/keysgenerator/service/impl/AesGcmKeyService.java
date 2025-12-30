package com.yegecali.keysgenerator.service.impl;

import com.yegecali.keysgenerator.openapi.model.KeyGenerationRequest;
import com.yegecali.keysgenerator.model.KeyModel;
import com.yegecali.keysgenerator.service.KeyGenerationService;
import com.yegecali.keysgenerator.openapi.model.CryptoRequest;
import com.yegecali.keysgenerator.service.strategies.generate.KeyGenerationFactory;
import com.yegecali.keysgenerator.exception.ApplicationException;
import org.jboss.logging.Logger;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AesGcmKeyService implements KeyGenerationService {

    private final KeyGenerationFactory strategyFactory;

    private static final Logger LOGGER = Logger.getLogger(AesGcmKeyService.class);

    @Inject
    public AesGcmKeyService(KeyGenerationFactory strategyFactory) {
        this.strategyFactory = strategyFactory;
    }

    @Override
    public CryptoRequest.TypeEnum getType() {
        return CryptoRequest.TypeEnum.AES_GCM;
    }

    @Override
    public KeyModel generate(KeyGenerationRequest request) {
        try {
            // usa la fábrica para obtener la estrategia concreta de generación
            com.yegecali.keysgenerator.service.strategies.generate.KeyGenerator strategy =
                    strategyFactory.get(request.getType() == null ? null : request.getType().getValue());
            return strategy.generate(request);
        } catch (Exception e) {
            LOGGER.error("Error generating keys", e);
            throw ApplicationException.keyGenerationFailed("Failed to generate keys", e);
        }
    }
}
