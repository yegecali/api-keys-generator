package com.yegecali.keysgenerator.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = EncryptRsaRequest.class, name = "RSA"),
    @JsonSubTypes.Type(value = EncryptAesRequest.class, name = "AES_GCM")
})
public abstract class CryptoRequest {
    protected CryptoAlgorithm type;
    protected String key;
    protected String payload;
    protected String iv;

    public CryptoRequest() {}

    public CryptoRequest(CryptoAlgorithm type, String key, String payload, String iv) {
        this.type = type;
        this.key = key;
        this.payload = payload;
        this.iv = iv;
    }

    public CryptoAlgorithm getType() { return type; }
    public void setType(CryptoAlgorithm type) { this.type = type; }
    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }
    public String getPayload() { return payload; }
    public void setPayload(String payload) { this.payload = payload; }
    public String getIv() { return iv; }
    public void setIv(String iv) { this.iv = iv; }
}
