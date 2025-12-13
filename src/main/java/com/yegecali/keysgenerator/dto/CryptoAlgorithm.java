package com.yegecali.keysgenerator.dto;

public enum CryptoAlgorithm {
    RSA("RSA"),
    AES_GCM("AES_GCM");

    private final String value;

    CryptoAlgorithm(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static CryptoAlgorithm fromString(String value) {
        if (value == null) {
            return RSA;
        }
        for (CryptoAlgorithm algo : CryptoAlgorithm.values()) {
            if (algo.value.equalsIgnoreCase(value)) {
                return algo;
            }
        }
        throw new IllegalArgumentException("Unknown algorithm: " + value);
    }
}

