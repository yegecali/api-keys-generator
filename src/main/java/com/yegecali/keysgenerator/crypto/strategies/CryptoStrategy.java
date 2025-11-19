package com.yegecali.keysgenerator.crypto.strategies;

import com.yegecali.keysgenerator.dto.CryptoRequest;
import com.yegecali.keysgenerator.dto.CryptoResponse;

public interface CryptoStrategy {
    String getAlgorithm();
    CryptoResponse encrypt(CryptoRequest request) throws Exception;
    CryptoResponse decrypt(CryptoRequest request) throws Exception;
}

