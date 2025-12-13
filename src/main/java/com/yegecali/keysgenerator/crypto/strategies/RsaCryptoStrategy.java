package com.yegecali.keysgenerator.crypto.strategies;

import com.yegecali.keysgenerator.dto.CryptoRequest;
import com.yegecali.keysgenerator.dto.EncryptRsaRequest;
import com.yegecali.keysgenerator.dto.CryptoResponse;
import com.yegecali.keysgenerator.dto.CryptoAlgorithm;
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
    public String getAlgorithm() { return CryptoAlgorithm.RSA.getValue(); }

    @Override
    public CryptoResponse encrypt(CryptoRequest request) throws Exception {
        // Cast a EncryptRsaRequest si es posible, sino usar CryptoRequest como fallback
        EncryptRsaRequest rsaRequest = (request instanceof EncryptRsaRequest)
            ? (EncryptRsaRequest) request
            : castToEncryptRsaRequest(request);

        try {
            String publicPem = rsaRequest.getKey();
            if (publicPem == null) throw new KeyGenerationException("Missing public key for RSA encryption");
            PublicKey pub = parsePublicKey(publicPem);
            byte[] plaintext = rsaRequest.getPayload().getBytes(java.nio.charset.StandardCharsets.UTF_8);

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
        // Cast a EncryptRsaRequest si es posible, sino usar CryptoRequest como fallback
        EncryptRsaRequest rsaRequest = (request instanceof EncryptRsaRequest)
            ? (EncryptRsaRequest) request
            : castToEncryptRsaRequest(request);

        try {
            String privatePem = rsaRequest.getKey();
            if (privatePem == null) throw new KeyGenerationException("Missing private key for RSA decryption");
            PrivateKey priv = parsePrivateKey(privatePem);
            byte[] ct = Base64.getDecoder().decode(rsaRequest.getPayload());

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

    private EncryptRsaRequest castToEncryptRsaRequest(CryptoRequest request) {
        EncryptRsaRequest rsaRequest = new EncryptRsaRequest();
        rsaRequest.setType(request.getType());
        rsaRequest.setKey(request.getKey());
        rsaRequest.setPayload(request.getPayload());
        rsaRequest.setIv(request.getIv());
        return rsaRequest;
    }

    private String normalizePem(String pem) {
        if (pem == null) return null;
        return pem.replaceAll("\\s", "");
    }

    private PublicKey parsePublicKey(String pem) throws Exception {
        String cleaned = normalizePem(pem);
        byte[] der = Base64.getDecoder().decode(cleaned);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(der);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

    private PrivateKey parsePrivateKey(String pem) throws Exception {
        String cleaned = normalizePem(pem);
        byte[] der = Base64.getDecoder().decode(cleaned);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(der);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }
}

