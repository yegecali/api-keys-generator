package com.yegecali.keysgenerator.service;

import java.util.Map;

public class KeyGenerationRequest {
    private String type;
    private Integer keySize;
    private Map<String, String> options;

    public KeyGenerationRequest() {}

    public KeyGenerationRequest(String type, Integer keySize, Map<String, String> options) {
        this.type = type;
        this.keySize = keySize;
        this.options = options;
    }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Integer getKeySize() { return keySize; }
    public void setKeySize(Integer keySize) { this.keySize = keySize; }
    public Map<String, String> getOptions() { return options; }
    public void setOptions(Map<String, String> options) { this.options = options; }
}
