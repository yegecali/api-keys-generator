package com.yegecali.keysgenerator.dto;

public class EncryptAesRequest extends CryptoRequest {

    public EncryptAesRequest() {
        this.type = CryptoAlgorithm.AES_GCM;
    }

    public EncryptAesRequest(String symmetricKey, String payload, String iv) {
        this.type = CryptoAlgorithm.AES_GCM;
        this.key = symmetricKey;
        this.payload = payload;
        this.iv = iv;
    }
}

