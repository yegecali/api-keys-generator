package com.yegecali.keysgenerator.model;

import java.util.Map;

public class KeyModel {
    private String type;
    private Integer keySize;
    private String publicKey;
    private String privateKey;
    private String symmetricKey;
    private String iv;
    private Map<String, String> metadata;

    public KeyModel() {}

    public KeyModel(String type, Integer keySize, String publicKey, String privateKey,
                    String symmetricKey, String iv, Map<String, String> metadata) {
        this.type = type;
        this.keySize = keySize;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        this.symmetricKey = symmetricKey;
        this.iv = iv;
        this.metadata = metadata;
    }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Integer getKeySize() { return keySize; }
    public void setKeySize(Integer keySize) { this.keySize = keySize; }
    public String getPublicKey() { return publicKey; }
    public void setPublicKey(String publicKey) { this.publicKey = publicKey; }
    public String getPrivateKey() { return privateKey; }
    public void setPrivateKey(String privateKey) { this.privateKey = privateKey; }
    public String getSymmetricKey() { return symmetricKey; }
    public void setSymmetricKey(String symmetricKey) { this.symmetricKey = symmetricKey; }
    public String getIv() { return iv; }
    public void setIv(String iv) { this.iv = iv; }
    public Map<String, String> getMetadata() { return metadata; }
    public void setMetadata(Map<String, String> metadata) { this.metadata = metadata; }
}
