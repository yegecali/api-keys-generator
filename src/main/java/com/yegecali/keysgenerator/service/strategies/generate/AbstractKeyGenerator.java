package com.yegecali.keysgenerator.service.strategies;

import com.yegecali.keysgenerator.exception.KeyGenerationException;
import com.yegecali.keysgenerator.model.KeyModel;
import com.yegecali.keysgenerator.service.KeyGenerationRequest;
import org.jboss.logging.Logger;

import java.util.Base64;

/**
 * Shared base class for key generators with common helpers.
 */
public abstract class AbstractKeyGenerator implements com.yegecali.keysgenerator.service.KeyGenerator {

    protected Logger getLogger() {
        return Logger.getLogger(this.getClass());
    }

    protected int resolveKeySize(KeyGenerationRequest request, int defaultSize) {
        return (request == null || request.getKeySize() == null) ? defaultSize : request.getKeySize();
    }

    protected String base64Encode(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    protected KeyModel baseModel(String type, int keySize) {
        KeyModel m = new KeyModel();
        m.setType(type);
        m.setKeySize(keySize);
        return m;
    }

    protected KeyGenerationException wrapException(String message, Exception e) {
        return new KeyGenerationException(message + (e == null ? "" : (": " + e.getMessage())), e);
    }
}

