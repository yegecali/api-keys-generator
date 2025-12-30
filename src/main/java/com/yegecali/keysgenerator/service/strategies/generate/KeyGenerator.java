package com.yegecali.keysgenerator.service;

import com.yegecali.keysgenerator.model.KeyModel;

public interface KeyGenerator {
    String getType();
    KeyModel generate(KeyGenerationRequest request);
}

