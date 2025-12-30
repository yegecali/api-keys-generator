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

@ApplicationScoped
public class RsaCryptoStrategy extends AbstractCryptoStrategy {

    @Override
    public String getAlgorithm() { return CryptoAlgorithm.RSA.getValue(); }

    @Override
    public CryptoResponse encrypt(CryptoRequest request) {
        EncryptRsaRequest rsaRequest = (request instanceof EncryptRsaRequest)
            ? (EncryptRsaRequest) request
            : castToEncryptRsaRequest(request);

        try {
            String publicPem = rsaRequest.getKey();
            if (publicPem == null) throw new KeyGenerationException("Missing public key for RSA encryption");
            PublicKey pub = parsePublicKey(publicPem);
            byte[] plaintext = toUtf8Bytes(rsaRequest.getPayload());

            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, pub);
            byte[] ct = cipher.doFinal(plaintext);

            return successWithCiphertext(ct);
        } catch (Exception e) {
            throw wrapException("RSA encryption failed", e);
        }
    }

    @Override
    public CryptoResponse decrypt(CryptoRequest request) {
        EncryptRsaRequest rsaRequest = (request instanceof EncryptRsaRequest)
            ? (EncryptRsaRequest) request
            : castToEncryptRsaRequest(request);

        try {
            String privatePem = rsaRequest.getKey();
            if (privatePem == null) throw new KeyGenerationException("Missing private key for RSA decryption");
            PrivateKey priv = parsePrivateKey(privatePem);
            byte[] ct = base64Decode(rsaRequest.getPayload());

            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
            cipher.init(Cipher.DECRYPT_MODE, priv);
            byte[] pt = cipher.doFinal(ct);

            return successWithPlaintext(utf8String(pt));
        } catch (Exception e) {
            throw wrapException("RSA decryption failed", e);
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
        byte[] der = base64Decode(cleaned);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(der);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

    private PrivateKey parsePrivateKey(String pem) throws Exception {
        String cleaned = normalizePem(pem);
        byte[] der = base64Decode(cleaned);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(der);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }
}
