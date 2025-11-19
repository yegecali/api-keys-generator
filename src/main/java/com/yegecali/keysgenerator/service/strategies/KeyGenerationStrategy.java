package com.yegecali.keysgenerator.service.strategies;

import com.yegecali.keysgenerator.model.KeyModel;
import com.yegecali.keysgenerator.service.KeyGenerator;
import com.yegecali.keysgenerator.service.KeyGenerationRequest;

public interface KeyGenerationStrategy extends KeyGenerator {
    // Extends KeyGenerator para que las estrategias sean compatibles
    // y puedan ser inyectadas como KeyGenerator
}
