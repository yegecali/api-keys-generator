package com.yegecali.keysgenerator.service.strategies;

import com.yegecali.keysgenerator.dto.CryptoAlgorithm;
import com.yegecali.keysgenerator.exception.KeyGenerationException;
import com.yegecali.keysgenerator.model.KeyModel;
import com.yegecali.keysgenerator.service.KeyGenerationRequest;
import com.yegecali.keysgenerator.service.KeyGenerator;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

@ApplicationScoped
public class RsaKeyGeneration implements KeyGenerator {

    private static final Logger LOG = Logger.getLogger(RsaKeyGeneration.class);

    @Override
    public String getType() {
        return CryptoAlgorithm.RSA.getValue();
    }

    @Override
    public KeyModel generate(KeyGenerationRequest request) {
        int keySize = (request.getKeySize() == null) ? 2048 : request.getKeySize();
        try {
            LOG.debugf("Generating RSA key pair with size %d", keySize);
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(keySize);
            KeyPair kp = kpg.generateKeyPair();
            PublicKey pub = kp.getPublic();
            PrivateKey priv = kp.getPrivate();

            String publicPem = toPem(pub.getEncoded());
            String privatePem = toPem(priv.getEncoded());

            KeyModel model = new KeyModel();
            model.setType(CryptoAlgorithm.RSA.getValue());
            model.setKeySize(keySize);
            model.setPublicKey(publicPem);
            model.setPrivateKey(privatePem);
            return model;
        } catch (Exception e) {
            LOG.error("Error generating RSA key pair", e);
            throw new KeyGenerationException("Failed to generate RSA key pair: " + e.getMessage(), e);
        }
    }

    private static String toPem(byte[] derBytes) {
        // Usar Base64 simple sin saltos de línea para evitar caracteres de escape en JSON
        return Base64.getEncoder().encodeToString(derBytes);
    }
}
