package com.yegecali.keysgenerator.service.strategies;

import com.yegecali.keysgenerator.dto.CryptoAlgorithm;
import com.yegecali.keysgenerator.exception.KeyGenerationException;
import com.yegecali.keysgenerator.model.KeyModel;
import com.yegecali.keysgenerator.service.KeyGenerationRequest;
import com.yegecali.keysgenerator.service.KeyGenerator;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;

import javax.crypto.SecretKey;
import java.security.SecureRandom;
import java.util.Base64;

@ApplicationScoped
public class AesKeyGeneration implements KeyGenerator {

    private static final Logger LOG = Logger.getLogger(AesKeyGeneration.class);

    @Override
    public String getType() {
        return CryptoAlgorithm.AES_GCM.getValue();
    }

    @Override
    public KeyModel generate(KeyGenerationRequest request) {
        int keySize = (request.getKeySize() == null) ? 256 : request.getKeySize();
        try {
            LOG.debugf("Generating AES-GCM key with size %d", keySize);
            javax.crypto.KeyGenerator kg = javax.crypto.KeyGenerator.getInstance("AES");
            kg.init(keySize);
            SecretKey secretKey = kg.generateKey();

            byte[] iv = new byte[12];
            SecureRandom rnd = new SecureRandom();
            rnd.nextBytes(iv);

            String keyB64 = Base64.getEncoder().encodeToString(secretKey.getEncoded());
            String ivB64 = Base64.getEncoder().encodeToString(iv);

            KeyModel model = new KeyModel();
            model.setType(CryptoAlgorithm.AES_GCM.getValue());
            model.setKeySize(keySize);
            model.setSymmetricKey(keyB64);
            model.setIv(ivB64);
            return model;
        } catch (Exception e) {
            LOG.error("Error generating AES-GCM key", e);
            throw new KeyGenerationException("Failed to generate AES-GCM key: " + e.getMessage(), e);
        }
    }
}
