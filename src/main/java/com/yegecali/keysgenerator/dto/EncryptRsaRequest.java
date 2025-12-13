package com.yegecali.keysgenerator.dto;

public class EncryptRsaRequest extends CryptoRequest {

    public EncryptRsaRequest() {
        this.type = CryptoAlgorithm.RSA;
    }

    public EncryptRsaRequest(String publicKey, String payload) {
        this.type = CryptoAlgorithm.RSA;
        this.key = publicKey;
        this.payload = payload;
        this.iv = null; // RSA no utiliza IV
    }
}

