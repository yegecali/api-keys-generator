package com.yegecali.keysgenerator.validator;

import com.yegecali.keysgenerator.dto.CryptoRequest;
import com.yegecali.keysgenerator.exception.InvalidRequestException;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CryptoRequestValidator {

    public void validateForEncrypt(CryptoRequest req) {
        if (req == null) throw new InvalidRequestException("Request body is required");
        if (req.getType() == null) throw new InvalidRequestException("Type is required (RSA or AES_GCM)");
        String type = req.getType().toUpperCase();
        if (req.getPayload() == null) throw new InvalidRequestException("Payload is required for encryption");

        switch (type) {
            case "RSA":
                if (req.getKey() == null) throw new InvalidRequestException("publicKey (PEM) is required for RSA encryption");
                break;
            case "AES_GCM":
            case "AES":
                if (req.getKey() == null) throw new InvalidRequestException("symmetric key (base64) is required for AES-GCM encryption");
                break;
            default:
                throw new InvalidRequestException("Unknown type: " + req.getType());
        }
    }

    public void validateForDecrypt(CryptoRequest req) {
        if (req == null) throw new InvalidRequestException("Request body is required");
        if (req.getType() == null) throw new InvalidRequestException("Type is required (RSA or AES_GCM)");
        String type = req.getType().toUpperCase();
        if (req.getPayload() == null) throw new InvalidRequestException("Payload is required for decryption");

        switch (type) {
            case "RSA":
                if (req.getKey() == null) throw new InvalidRequestException("privateKey (PEM) is required for RSA decryption");
                break;
            case "AES_GCM":
            case "AES":
                if (req.getKey() == null) throw new InvalidRequestException("symmetric key (base64) is required for AES-GCM decryption");
                if (req.getIv() == null) throw new InvalidRequestException("iv (base64) is required for AES-GCM decryption");
                break;
            default:
                throw new InvalidRequestException("Unknown type: " + req.getType());
        }
    }
}

