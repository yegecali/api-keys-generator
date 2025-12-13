package com.yegecali.keysgenerator.crypto.strategies;

import com.yegecali.keysgenerator.dto.CryptoRequest;
import com.yegecali.keysgenerator.dto.EncryptAesRequest;
import com.yegecali.keysgenerator.dto.CryptoResponse;
import com.yegecali.keysgenerator.dto.CryptoAlgorithm;
import com.yegecali.keysgenerator.exception.KeyGenerationException;
import jakarta.enterprise.context.ApplicationScoped;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

@ApplicationScoped
public class AesGcmCryptoStrategy implements CryptoStrategy {

    private static final int GCM_TAG_LENGTH = 128;
    private static final int IV_SIZE = 12;

    @Override
    public String getAlgorithm() { return CryptoAlgorithm.AES_GCM.getValue(); }

    @Override
    public CryptoResponse encrypt(CryptoRequest request) throws Exception {
        // Cast a EncryptAesRequest si es posible, sino usar CryptoRequest como fallback
        EncryptAesRequest aesRequest = (request instanceof EncryptAesRequest)
            ? (EncryptAesRequest) request
            : castToEncryptAesRequest(request);

        try {
            String keyB64 = aesRequest.getKey();
            if (keyB64 == null) throw new KeyGenerationException("Missing symmetric key for AES encryption");
            byte[] key = Base64.getDecoder().decode(keyB64);
            byte[] iv = new byte[IV_SIZE];
            SecureRandom rnd = new SecureRandom();
            rnd.nextBytes(iv);

            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
            GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, spec);
            byte[] plaintext = aesRequest.getPayload().getBytes(java.nio.charset.StandardCharsets.UTF_8);
            byte[] ct = cipher.doFinal(plaintext);

            CryptoResponse resp = new CryptoResponse();
            resp.setSuccess(true);
            resp.setCiphertext(Base64.getEncoder().encodeToString(ct));
            resp.setIv(Base64.getEncoder().encodeToString(iv));
            return resp;
        } catch (Exception e) {
            throw new KeyGenerationException("AES-GCM encryption failed: " + e.getMessage(), e);
        }
    }

    @Override
    public CryptoResponse decrypt(CryptoRequest request) throws Exception {
        // Cast a EncryptAesRequest si es posible, sino usar CryptoRequest como fallback
        EncryptAesRequest aesRequest = (request instanceof EncryptAesRequest)
            ? (EncryptAesRequest) request
            : castToEncryptAesRequest(request);

        try {
            String keyB64 = aesRequest.getKey();
            String ivB64 = aesRequest.getIv();
            if (keyB64 == null) throw new KeyGenerationException("Missing symmetric key for AES decryption");
            if (ivB64 == null) throw new KeyGenerationException("Missing IV for AES decryption");

            byte[] key = Base64.getDecoder().decode(keyB64);
            byte[] iv = Base64.getDecoder().decode(ivB64);

            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
            GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, spec);

            byte[] ct = Base64.getDecoder().decode(aesRequest.getPayload());
            byte[] pt = cipher.doFinal(ct);

            CryptoResponse resp = new CryptoResponse();
            resp.setSuccess(true);
            resp.setPlaintext(new String(pt, java.nio.charset.StandardCharsets.UTF_8));
            return resp;
        } catch (Exception e) {
            throw new KeyGenerationException("AES-GCM decryption failed: " + e.getMessage(), e);
        }
    }

    private EncryptAesRequest castToEncryptAesRequest(CryptoRequest request) {
        EncryptAesRequest aesRequest = new EncryptAesRequest();
        aesRequest.setType(request.getType());
        aesRequest.setKey(request.getKey());
        aesRequest.setPayload(request.getPayload());
        aesRequest.setIv(request.getIv());
        return aesRequest;
    }
}

