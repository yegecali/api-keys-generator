package com.yegecali.keysgenerator.factory;

import com.yegecali.keysgenerator.exception.NoSuchKeyGeneratorException;
import com.yegecali.keysgenerator.service.KeyGenerator;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

@ApplicationScoped
public class KeyGeneratorFactory {

    @Inject
    Instance<KeyGenerator> generators;

    public KeyGenerator get(String type) {
        if (type == null) {
            type = "RSA";
        }
        for (KeyGenerator g : generators) {
            if (g.getType().equalsIgnoreCase(type)) {
                return g;
            }
        }
        throw new NoSuchKeyGeneratorException("No KeyGenerator registered for type: " + type);
    }
}

