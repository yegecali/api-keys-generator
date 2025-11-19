package com.yegecali.keysgenerator.crypto.strategies;

import com.yegecali.keysgenerator.dto.CryptoRequest;
import com.yegecali.keysgenerator.dto.CryptoResponse;
import com.yegecali.keysgenerator.exception.KeyGenerationException;
import jakarta.enterprise.context.ApplicationScoped;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@ApplicationScoped
public class RsaCryptoStrategy implements CryptoStrategy {

    @Override
    public String getAlgorithm() { return "RSA"; }

    @Override
    public CryptoResponse encrypt(CryptoRequest request) throws Exception {
        try {
            String publicPem = request.getKey();
            if (publicPem == null) throw new KeyGenerationException("Missing public key for RSA encryption");
            PublicKey pub = parsePublicKey(publicPem);
            byte[] plaintext = request.getPayload().getBytes(java.nio.charset.StandardCharsets.UTF_8);

            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, pub);
            byte[] ct = cipher.doFinal(plaintext);

            CryptoResponse resp = new CryptoResponse();
            resp.setSuccess(true);
            resp.setCiphertext(Base64.getEncoder().encodeToString(ct));
            return resp;
        } catch (Exception e) {
            throw new KeyGenerationException("RSA encryption failed: " + e.getMessage(), e);
        }
    }

    @Override
    public CryptoResponse decrypt(CryptoRequest request) throws Exception {
        try {
            String privatePem = request.getKey();
            if (privatePem == null) throw new KeyGenerationException("Missing private key for RSA decryption");
            PrivateKey priv = parsePrivateKey(privatePem);
            byte[] ct = Base64.getDecoder().decode(request.getPayload());

            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
            cipher.init(Cipher.DECRYPT_MODE, priv);
            byte[] pt = cipher.doFinal(ct);

            CryptoResponse resp = new CryptoResponse();
            resp.setSuccess(true);
            resp.setPlaintext(new String(pt, java.nio.charset.StandardCharsets.UTF_8));
            return resp;
        } catch (Exception e) {
            throw new KeyGenerationException("RSA decryption failed: " + e.getMessage(), e);
        }
    }

    private PublicKey parsePublicKey(String pem) throws Exception {
        String cleaned = pem.replaceAll("-----BEGIN PUBLIC KEY-----", "")
                .replaceAll("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");
        byte[] der = Base64.getDecoder().decode(cleaned);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(der);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

    private PrivateKey parsePrivateKey(String pem) throws Exception {
        String cleaned = pem.replaceAll("-----BEGIN PRIVATE KEY-----", "")
                .replaceAll("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");
        byte[] der = Base64.getDecoder().decode(cleaned);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(der);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }
}

