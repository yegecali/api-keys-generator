package com.yegecali.keysgenerator.service.strategies.generate;

import com.yegecali.keysgenerator.openapi.model.KeyGenerationRequest;
import com.yegecali.keysgenerator.model.KeyModel;
import com.yegecali.keysgenerator.openapi.model.KeyGenerationRequest.TypeEnum;
import com.yegecali.keysgenerator.config.AppConfig;
import com.yegecali.keysgenerator.exception.ApplicationException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.crypto.SecretKey;
import java.security.SecureRandom;

@ApplicationScoped
public class AesKeyGeneration extends AbstractKeyGenerator implements KeyGenerator {

    @Inject
    AppConfig appConfig;

    @Override
    public String getType() {
        return TypeEnum.AES_GCM.getValue();
    }

    @Override
    public KeyModel generate(KeyGenerationRequest request) {
        int defaultSize = appConfig.keyGeneration().aes().defaultSize();
        int keySize = resolveKeySize(request, defaultSize);
        try {
            getLogger().debugf("Generating AES-GCM key with size %d", keySize);
            javax.crypto.KeyGenerator kg = javax.crypto.KeyGenerator.getInstance("AES");
            kg.init(keySize);
            SecretKey secretKey = kg.generateKey();

            int ivSize = appConfig.crypto().aesGcm().ivSize();
            byte[] iv = new byte[ivSize];
            SecureRandom rnd = new SecureRandom();
            rnd.nextBytes(iv);

            String keyB64 = base64Encode(secretKey.getEncoded());
            String ivB64 = base64Encode(iv);

            KeyModel model = baseModel(TypeEnum.AES_GCM.getValue(), keySize);
            model.setSymmetricKey(keyB64);
            model.setIv(ivB64);
            return model;
        } catch (Exception e) {
            getLogger().error("Error generating AES-GCM key", e);
            throw ApplicationException.keyGenerationFailed("Failed to generate AES-GCM key", e);
        }
    }
}
