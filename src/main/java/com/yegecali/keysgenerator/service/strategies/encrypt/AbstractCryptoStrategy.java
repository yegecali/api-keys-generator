package com.yegecali.keysgenerator.service.strategies.encrypt;

import com.yegecali.keysgenerator.openapi.model.CryptoResponse;

/**
 * Base class with common helpers for crypto strategies.
 */
public abstract class AbstractCryptoStrategy implements CryptoStrategy {

    protected byte[] base64Decode(String s) {
        return s == null ? null : java.util.Base64.getDecoder().decode(s);
    }

    protected String base64Encode(byte[] bytes) {
        return bytes == null ? null : java.util.Base64.getEncoder().encodeToString(bytes);
    }

    protected byte[] toUtf8Bytes(String s) {
        return s == null ? new byte[0] : s.getBytes(java.nio.charset.StandardCharsets.UTF_8);
    }

    protected String utf8String(byte[] bytes) {
        return bytes == null ? null : new String(bytes, java.nio.charset.StandardCharsets.UTF_8);
    }

    protected CryptoResponse successWithCiphertext(byte[] ct) {
        CryptoResponse resp = new CryptoResponse();
        resp.setSuccess(true);
        resp.setCiphertext(base64Encode(ct));
        return resp;
    }

    protected CryptoResponse successWithPlaintext(String plaintext) {
        CryptoResponse resp = new CryptoResponse();
        resp.setSuccess(true);
        resp.setPlaintext(plaintext);
        return resp;
    }
}
