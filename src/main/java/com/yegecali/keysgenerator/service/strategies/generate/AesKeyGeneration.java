package com.yegecali.keysgenerator.service.strategies.generate;

import com.yegecali.keysgenerator.openapi.model.KeyGenerationRequest;
import com.yegecali.keysgenerator.model.KeyModel;
import com.yegecali.keysgenerator.openapi.model.KeyGenerationRequest.TypeEnum;
import jakarta.enterprise.context.ApplicationScoped;

import javax.crypto.SecretKey;
import java.security.SecureRandom;

@ApplicationScoped
public class AesKeyGeneration extends AbstractKeyGenerator implements KeyGenerator {

    @Override
    public String getType() {
        return TypeEnum.AES_GCM.getValue();
    }

    @Override
    public KeyModel generate(KeyGenerationRequest request) {
        int keySize = resolveKeySize(request, 256);
        try {
            getLogger().debugf("Generating AES-GCM key with size %d", keySize);
            javax.crypto.KeyGenerator kg = javax.crypto.KeyGenerator.getInstance("AES");
            kg.init(keySize);
            SecretKey secretKey = kg.generateKey();

            byte[] iv = new byte[12];
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
            throw wrapException("Failed to generate AES-GCM key", e);
        }
    }
}
