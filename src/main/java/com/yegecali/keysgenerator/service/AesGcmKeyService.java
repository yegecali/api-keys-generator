package com.yegecali.keysgenerator.service;

import com.yegecali.keysgenerator.exception.KeyGenerationException;
import com.yegecali.keysgenerator.model.KeyModel;
import com.yegecali.keysgenerator.service.strategies.KeyGenerationStrategyFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

@ApplicationScoped
public class AesGcmKeyService implements KeyGenerator {

    private static final Logger LOG = Logger.getLogger(AesGcmKeyService.class);

    @Inject
    KeyGenerationStrategyFactory strategyFactory;

    @Override
    public String getType() {
        return "AES_GCM";
    }

    @Override
    public KeyModel generate(KeyGenerationRequest request) {
        try {
            KeyGenerator strategy = strategyFactory.get(request.getType());
            return strategy.generate(request);
        } catch (Exception e) {
            LOG.error("Error generating keys", e);
            throw new KeyGenerationException("Failed to generate keys: " + e.getMessage(), e);
        }
    }
}
