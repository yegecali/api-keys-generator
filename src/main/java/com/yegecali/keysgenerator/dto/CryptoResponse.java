package com.yegecali.keysgenerator.dto;

public class CryptoResponse {
    private boolean success;
    private String ciphertext;
    private String iv;
    private String plaintext;
    private String error;

    public CryptoResponse() {}

    public CryptoResponse(boolean success, String ciphertext, String iv, String plaintext, String error) {
        this.success = success;
        this.ciphertext = ciphertext;
        this.iv = iv;
        this.plaintext = plaintext;
        this.error = error;
    }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getCiphertext() { return ciphertext; }
    public void setCiphertext(String ciphertext) { this.ciphertext = ciphertext; }
    public String getIv() { return iv; }
    public void setIv(String iv) { this.iv = iv; }
    public String getPlaintext() { return plaintext; }
    public void setPlaintext(String plaintext) { this.plaintext = plaintext; }
    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
}
