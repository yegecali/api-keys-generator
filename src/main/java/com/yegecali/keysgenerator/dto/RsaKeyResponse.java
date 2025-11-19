package com.yegecali.keysgenerator.dto;

public class RsaKeyResponse {
    private String algorithm;
    private int keySize;
    private String publicKey;
    private String privateKey;

    public RsaKeyResponse() {}

    public RsaKeyResponse(String algorithm, int keySize, String publicKey, String privateKey) {
        this.algorithm = algorithm;
        this.keySize = keySize;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public String getAlgorithm() { return algorithm; }
    public void setAlgorithm(String algorithm) { this.algorithm = algorithm; }

    public int getKeySize() { return keySize; }
    public void setKeySize(int keySize) { this.keySize = keySize; }

    public String getPublicKey() { return publicKey; }
    public void setPublicKey(String publicKey) { this.publicKey = publicKey; }

    public String getPrivateKey() { return privateKey; }
    public void setPrivateKey(String privateKey) { this.privateKey = privateKey; }
}

