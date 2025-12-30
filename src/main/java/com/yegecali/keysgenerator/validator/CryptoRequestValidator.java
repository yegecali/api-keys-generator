package com.yegecali.keysgenerator.validator;

import com.yegecali.keysgenerator.openapi.model.CryptoRequest;
import com.yegecali.keysgenerator.exception.ApplicationException;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CryptoRequestValidator {

    public void validateForEncrypt(CryptoRequest req) {
        ValidationHandler chain = buildChain(true);
        chain.handle(req);
    }

    public void validateForDecrypt(CryptoRequest req) {
        ValidationHandler chain = buildChain(false);
        chain.handle(req);
    }

    private ValidationHandler buildChain(boolean forEncrypt) {
        ValidationHandler head = new NullRequestHandler();
        ValidationHandler current = head.setNext(new TypeValidationHandler());
        current = current.setNext(new PayloadValidationHandler());
        if (forEncrypt) {
            current.setNext(new EncryptKeyValidationHandler());
        } else {
            current.setNext(new DecryptKeyValidationHandler());
        }
        return head;
    }

    private interface ValidationHandler {
        ValidationHandler setNext(ValidationHandler next);
        void handle(CryptoRequest req) throws ApplicationException;
    }

    private abstract static class AbstractValidationHandler implements ValidationHandler {
        private ValidationHandler next;

        @Override
        public ValidationHandler setNext(ValidationHandler next) {
            this.next = next;
            return next;
        }

        protected void next(CryptoRequest req) {
            if (next != null) next.handle(req);
        }
    }

    private static class NullRequestHandler extends AbstractValidationHandler {
        @Override
        public void handle(CryptoRequest req) {
            if (req == null) throw ApplicationException.invalidRequest("Request body is required");
            next(req);
        }
    }

    private static class TypeValidationHandler extends AbstractValidationHandler {
        @Override
        public void handle(CryptoRequest req) {
            CryptoRequest.TypeEnum type = req.getType();
            if (type != CryptoRequest.TypeEnum.RSA && type != CryptoRequest.TypeEnum.AES_GCM) {
                throw ApplicationException.invalidRequest("Type is required (RSA or AES_GCM). Unknown/unsupported: " + type);
            }
            next(req);
        }
    }

    private static class PayloadValidationHandler extends AbstractValidationHandler {
        @Override
        public void handle(CryptoRequest req) {
            if (req.getPayload() == null) throw ApplicationException.invalidRequest("Payload is required");
            next(req);
        }
    }

    private static class EncryptKeyValidationHandler extends AbstractValidationHandler {
        @Override
        public void handle(CryptoRequest req) {
            CryptoRequest.TypeEnum type = req.getType();
            switch (type) {
                case RSA:
                    if (req.getKey() == null) throw ApplicationException.invalidRequest("publicKey (PEM) is required for RSA encryption");
                    break;
                case AES_GCM:
                    if (req.getKey() == null) throw ApplicationException.invalidRequest("symmetric key (base64) is required for AES-GCM encryption");
                    break;
                default:
                    throw ApplicationException.invalidRequest("Unknown type: " + req.getType());
            }
            next(req);
        }
    }

    private static class DecryptKeyValidationHandler extends AbstractValidationHandler {
        @Override
        public void handle(CryptoRequest req) {
            CryptoRequest.TypeEnum type = req.getType();
            switch (type) {
                case RSA:
                    if (req.getKey() == null) throw ApplicationException.invalidRequest("privateKey (PEM) is required for RSA decryption");
                    break;
                case AES_GCM:
                    if (req.getKey() == null) throw ApplicationException.invalidRequest("symmetric key (base64) is required for AES-GCM decryption");
                    if (req.getIv() == null) throw ApplicationException.invalidRequest("iv (base64) is required for AES-GCM decryption");
                    break;
                default:
                    throw ApplicationException.invalidRequest("Unknown type: " + req.getType());
            }
            next(req);
        }
    }
}
