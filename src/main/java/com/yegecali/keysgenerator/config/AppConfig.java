package com.yegecali.keysgenerator.config;

import io.smallrye.config.ConfigMapping;
import java.util.List;
import java.util.Optional;

@ConfigMapping(prefix = "app")
public interface AppConfig {

    KeyGenerationConfig keyGeneration();
    CryptoConfig crypto();
    ApiConfig api();

    interface KeyGenerationConfig {
        RsaConfig rsa();
        AesConfig aes();
    }

    interface RsaConfig {
        List<Integer> allowedSizes();
        int defaultSize();
        String algorithm();
    }

    interface AesConfig {
        List<Integer> allowedSizes();
        int defaultSize();
        String algorithm();
    }

    interface CryptoConfig {
        RsaCryptoConfig rsa();
        AesGcmConfig aesGcm();
    }

    interface RsaCryptoConfig {
        String algorithm();
        String keyFactoryAlgorithm();
    }

    interface AesGcmConfig {
        String algorithm();
        int tagLength();
        int ivSize();
        String cipherInstance();
    }

    interface ApiConfig {
        EndpointsConfig endpoints();
    }

    interface EndpointsConfig {
        String keysPath();
        String cryptoPath();
        String rsaLegacyPath();
        String mediaType();
    }
}

