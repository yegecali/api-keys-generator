package com.yegecali.keysgenerator.service.strategies.encrypt;

import com.yegecali.keysgenerator.openapi.model.CryptoRequest;
import com.yegecali.keysgenerator.openapi.model.CryptoResponse;
import com.yegecali.keysgenerator.exception.KeyGenerationException;
import com.yegecali.keysgenerator.config.AppConfig;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

@ApplicationScoped
public class AesGcmCryptoStrategy extends AbstractCryptoStrategy implements CryptoStrategy {

    @Inject
    AppConfig appConfig;

    @Override
    public String getAlgorithm() { return CryptoRequest.TypeEnum.AES_GCM.getValue(); }

    @Override
    public CryptoResponse encrypt(CryptoRequest request){
        try {
            int gcmTagLength = appConfig.crypto().aesGcm().tagLength();
            int ivSize = appConfig.crypto().aesGcm().ivSize();

            String keyB64 = request.getKey();
            if (keyB64 == null) throw new KeyGenerationException("Missing symmetric key for AES encryption");
            byte[] key = base64Decode(keyB64);
            byte[] iv = new byte[ivSize];
            SecureRandom rnd = new SecureRandom();
            rnd.nextBytes(iv);

            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
            GCMParameterSpec spec = new GCMParameterSpec(gcmTagLength, iv);
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, spec);
            byte[] plaintext = toUtf8Bytes(request.getPayload());
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
        try {
            int gcmTagLength = appConfig.crypto().aesGcm().tagLength();

            String keyB64 = request.getKey();
            String ivB64 = request.getIv();
            if (keyB64 == null) throw new KeyGenerationException("Missing symmetric key for AES decryption");
            if (ivB64 == null) throw new KeyGenerationException("Missing IV for AES decryption");

            byte[] key = base64Decode(keyB64);
            byte[] iv = base64Decode(ivB64);

            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
            GCMParameterSpec spec = new GCMParameterSpec(gcmTagLength, iv);
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, spec);

            byte[] ct = base64Decode(request.getPayload());
            byte[] pt = cipher.doFinal(ct);

            return successWithPlaintext(utf8String(pt));
        } catch (Exception e) {
            throw wrapException("AES-GCM decryption failed", e);
        }
    }
}
