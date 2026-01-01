package com.yegecali.keysgenerator.service.strategies.encrypt;

import com.yegecali.keysgenerator.openapi.model.CryptoRequest;
import com.yegecali.keysgenerator.openapi.model.CryptoResponse;
import com.yegecali.keysgenerator.exception.ApplicationException;
import com.yegecali.keysgenerator.config.AppConfig;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@ApplicationScoped
public class RsaCryptoStrategy extends AbstractCryptoStrategy implements CryptoStrategy {

    @Inject
    AppConfig appConfig;

    @Override
    public String getAlgorithm() { return "RSA"; }

    @Override
    public CryptoResponse encrypt(CryptoRequest request) {
        try {
            String algorithm = appConfig.crypto().rsa().algorithm();
            String keyB64 = request.getKey();
            if (keyB64 == null) throw ApplicationException.missingKey("Missing public key for RSA encryption");
            PublicKey pub = parsePublicKey(keyB64);
            byte[] plaintext = toUtf8Bytes(request.getPayload());

            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, pub);
            byte[] ct = cipher.doFinal(plaintext);

            return successWithCiphertext(ct);
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw ApplicationException.encryptionFailed("RSA encryption failed", e);
        }
    }

    @Override
    public CryptoResponse decrypt(CryptoRequest request) {
        try {
            String algorithm = appConfig.crypto().rsa().algorithm();
            String keyB64 = request.getKey();
            if (keyB64 == null) throw ApplicationException.missingKey("Missing private key for RSA decryption");
            PrivateKey priv = parsePrivateKey(keyB64);
            byte[] ct = base64Decode(request.getPayload());

            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, priv);
            byte[] pt = cipher.doFinal(ct);

            return successWithPlaintext(utf8String(pt));
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw ApplicationException.decryptionFailed("RSA decryption failed", e);
        }
    }

    private String normalizePem(String pem) {
        if (pem == null) return null;
        return pem.replaceAll("\\s", "");
    }

    private PublicKey parsePublicKey(String pem) throws Exception {
        String cleaned = normalizePem(pem);
        byte[] der = base64Decode(cleaned);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(der);
        String keyFactoryAlgorithm = appConfig.crypto().rsa().keyFactoryAlgorithm();
        KeyFactory kf = KeyFactory.getInstance(keyFactoryAlgorithm);
        return kf.generatePublic(spec);
    }

    private PrivateKey parsePrivateKey(String pem) throws Exception {
        String cleaned = normalizePem(pem);
        byte[] der = base64Decode(cleaned);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(der);
        String keyFactoryAlgorithm = appConfig.crypto().rsa().keyFactoryAlgorithm();
        KeyFactory kf = KeyFactory.getInstance(keyFactoryAlgorithm);
        return kf.generatePrivate(spec);
    }
}
