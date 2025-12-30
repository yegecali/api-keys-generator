package com.yegecali.keysgenerator.service.strategies.generate;

import com.yegecali.keysgenerator.openapi.model.KeyGenerationRequest;
import com.yegecali.keysgenerator.model.KeyModel;
import com.yegecali.keysgenerator.openapi.model.KeyGenerationRequest.TypeEnum;
import jakarta.enterprise.context.ApplicationScoped;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

@ApplicationScoped
public class RsaKeyGeneration extends AbstractKeyGenerator implements KeyGenerator {

    @Override
    public String getType() {
        return TypeEnum.RSA.getValue();
    }

    @Override
    public KeyModel generate(KeyGenerationRequest request) {
        int keySize = resolveKeySize(request, 2048);
        try {
            getLogger().debugf("Generating RSA key pair with size %d", keySize);
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(keySize);
            KeyPair kp = kpg.generateKeyPair();
            PublicKey pub = kp.getPublic();
            PrivateKey priv = kp.getPrivate();

            String publicPem = base64Encode(pub.getEncoded());
            String privatePem = base64Encode(priv.getEncoded());

            KeyModel model = baseModel(TypeEnum.RSA.getValue(), keySize);
            model.setPublicKey(publicPem);
            model.setPrivateKey(privatePem);
            return model;
        } catch (Exception e) {
            getLogger().error("Error generating RSA key pair", e);
            throw wrapException("Failed to generate RSA key pair", e);
        }
    }
}
