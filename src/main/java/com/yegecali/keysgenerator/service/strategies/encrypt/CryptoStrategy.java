package com.yegecali.keysgenerator.service.strategies.encrypt;

import com.yegecali.keysgenerator.openapi.model.CryptoRequest;
import com.yegecali.keysgenerator.openapi.model.CryptoResponse;

public interface CryptoStrategy {
    String getAlgorithm();
    CryptoResponse encrypt(CryptoRequest request) ;
    CryptoResponse decrypt(CryptoRequest request) ;
}
