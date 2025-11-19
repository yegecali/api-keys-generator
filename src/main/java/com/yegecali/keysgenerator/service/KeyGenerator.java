package com.yegecali.keysgenerator.service;

import com.yegecali.keysgenerator.model.KeyModel;

public interface KeyGenerator {
    // Returns the type this generator supports, e.g. "RSA", "AES_GCM"
    String getType();

    // Generate a key (symmetric or asymmetric) according to the request
    KeyModel generate(KeyGenerationRequest request);
}

