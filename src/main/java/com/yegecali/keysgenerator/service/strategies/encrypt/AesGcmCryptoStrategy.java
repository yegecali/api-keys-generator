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

@ApplicationScoped
public class AesGcmCryptoStrategy extends AbstractCryptoStrategy {

    private static final int GCM_TAG_LENGTH = 128;
    private static final int IV_SIZE = 12;

    @Override
    public String getAlgorithm() { return CryptoAlgorithm.AES_GCM.getValue(); }

    @Override
    public CryptoResponse encrypt(CryptoRequest request){
        EncryptAesRequest aesRequest = (request instanceof EncryptAesRequest)
            ? (EncryptAesRequest) request
            : castToEncryptAesRequest(request);

        try {
            String keyB64 = aesRequest.getKey();
            if (keyB64 == null) throw new KeyGenerationException("Missing symmetric key for AES encryption");
            byte[] key = base64Decode(keyB64);
            byte[] iv = new byte[IV_SIZE];
            SecureRandom rnd = new SecureRandom();
            rnd.nextBytes(iv);

            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
            GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, spec);
            byte[] plaintext = toUtf8Bytes(aesRequest.getPayload());
            byte[] ct = cipher.doFinal(plaintext);

            CryptoResponse resp = successWithCiphertext(ct);
            resp.setIv(base64Encode(iv));
            return resp;
        } catch (Exception e) {
            throw wrapException("AES-GCM encryption failed", e);
        }
    }

    @Override
    public CryptoResponse decrypt(CryptoRequest request) {
        EncryptAesRequest aesRequest = (request instanceof EncryptAesRequest)
            ? (EncryptAesRequest) request
            : castToEncryptAesRequest(request);

        try {
            String keyB64 = aesRequest.getKey();
            String ivB64 = aesRequest.getIv();
            if (keyB64 == null) throw new KeyGenerationException("Missing symmetric key for AES decryption");
            if (ivB64 == null) throw new KeyGenerationException("Missing IV for AES decryption");

            byte[] key = base64Decode(keyB64);
            byte[] iv = base64Decode(ivB64);

            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
            GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, spec);

            byte[] ct = base64Decode(aesRequest.getPayload());
            byte[] pt = cipher.doFinal(ct);

            return successWithPlaintext(utf8String(pt));
        } catch (Exception e) {
            throw wrapException("AES-GCM decryption failed", e);
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
