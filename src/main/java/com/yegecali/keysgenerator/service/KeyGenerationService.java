package com.yegecali.keysgenerator.service;

import com.yegecali.keysgenerator.model.KeyModel;
import com.yegecali.keysgenerator.openapi.model.CryptoRequest;
import com.yegecali.keysgenerator.openapi.model.KeyGenerationRequest;

/**
 * Interfaz dedicada a los servicios que exponen la capacidad de generar llaves.
 * Se separa de com.yegecali.keysgenerator.service.strategies.generate.KeyGenerator
 * para evitar confusión entre estrategias de generación y servicios que las usan.
 */
public interface KeyGenerationService {
    CryptoRequest.TypeEnum getType();
    KeyModel generate(KeyGenerationRequest request);
}
