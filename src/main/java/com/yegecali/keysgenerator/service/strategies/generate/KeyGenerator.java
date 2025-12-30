package com.yegecali.keysgenerator.service.strategies.generate;

import com.yegecali.keysgenerator.model.KeyModel;
import com.yegecali.keysgenerator.openapi.model.KeyGenerationRequest;

public interface KeyGenerator {
    String getType();
    KeyModel generate(KeyGenerationRequest request);
}

