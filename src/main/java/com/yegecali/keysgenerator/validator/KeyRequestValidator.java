package com.yegecali.keysgenerator.validator;

import com.yegecali.keysgenerator.service.KeyGenerationRequest;
import com.yegecali.keysgenerator.exception.InvalidRequestException;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@ApplicationScoped
public class KeyRequestValidator {

    private static final List<Integer> RSA_ALLOWED = Arrays.asList(2048, 3072, 4096);
    private static final List<Integer> AES_ALLOWED = Arrays.asList(128, 192, 256);

    public KeyGenerationRequest validateAndBuild(String typeParam, Integer size) {
        String type = (typeParam == null) ? "RSA" : typeParam.toUpperCase(Locale.ROOT);

        switch (type) {
            case "RSA": {
                int keySize = (size == null) ? 2048 : size;
                if (!RSA_ALLOWED.contains(keySize)) {
                    throw new InvalidRequestException("Invalid RSA key size. Allowed: " + RSA_ALLOWED);
                }
                return new KeyGenerationRequest("RSA", keySize, null);
            }
            case "AES_GCM":
            case "AES": {
                int keySize = (size == null) ? 256 : size;
                if (!AES_ALLOWED.contains(keySize)) {
                    throw new InvalidRequestException("Invalid AES key size. Allowed: " + AES_ALLOWED);
                }
                return new KeyGenerationRequest("AES_GCM", keySize, null);
            }
            default:
                throw new InvalidRequestException("Unknown key type: " + typeParam);
        }
    }
}

