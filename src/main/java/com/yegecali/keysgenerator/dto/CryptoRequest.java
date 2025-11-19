package com.yegecali.keysgenerator.dto;

public class CryptoRequest {
    private String type;
    private String key;
    private String payload;
    private String iv;

    public CryptoRequest() {}

    public CryptoRequest(String type, String key, String payload, String iv) {
        this.type = type;
        this.key = key;
        this.payload = payload;
        this.iv = iv;
    }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }
    public String getPayload() { return payload; }
    public void setPayload(String payload) { this.payload = payload; }
    public String getIv() { return iv; }
    public void setIv(String iv) { this.iv = iv; }
}
