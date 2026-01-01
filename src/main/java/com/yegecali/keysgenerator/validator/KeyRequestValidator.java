package com.yegecali.keysgenerator.validator;

import com.yegecali.keysgenerator.openapi.model.KeyGenerationRequest;
import com.yegecali.keysgenerator.exception.ApplicationException;
import com.yegecali.keysgenerator.config.AppConfig;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Locale;

@ApplicationScoped
public class KeyRequestValidator {

    @Inject
    AppConfig appConfig;

    public KeyGenerationRequest validateAndBuild(String typeParam, Integer size) {
        ValidationContext ctx = new ValidationContext(typeParam, size, appConfig);
        ValidationHandler chain = new NormalizeTypeHandler()
                .setNext(new SizeValidationHandler());
        chain.handle(ctx);
        if (ctx.result == null) {
            throw ApplicationException.invalidRequest("Failed to build KeyGenerationRequest");
        }
        return ctx.result;
    }

    private static class ValidationContext {
        final String typeParam;
        final Integer size;
        final AppConfig config;
        String normalizedType;
        KeyGenerationRequest result;

        ValidationContext(String typeParam, Integer size, AppConfig config) {
            this.typeParam = typeParam;
            this.size = size;
            this.config = config;
        }
    }

    private interface ValidationHandler {
        ValidationHandler setNext(ValidationHandler next);
        void handle(ValidationContext ctx) throws ApplicationException;
    }

    private abstract static class AbstractHandler implements ValidationHandler {
        private ValidationHandler next;

        @Override
        public ValidationHandler setNext(ValidationHandler next) {
            this.next = next;
            return next;
        }

        protected void next(ValidationContext ctx) {
            if (next != null) next.handle(ctx);
        }
    }

    private static class NormalizeTypeHandler extends AbstractHandler {
        @Override
        public void handle(ValidationContext ctx) {
            String t = (ctx.typeParam == null) ? "RSA" : ctx.typeParam.toUpperCase(Locale.ROOT);
            if ("AES".equals(t)) t = "AES_GCM";
            ctx.normalizedType = t;
            next(ctx);
        }
    }

    private static class SizeValidationHandler extends AbstractHandler {
        @Override
        public void handle(ValidationContext ctx) {
            String type = ctx.normalizedType;
            List<Integer> rsaAllowed = ctx.config.keyGeneration().rsa().allowedSizes();
            List<Integer> aesAllowed = ctx.config.keyGeneration().aes().allowedSizes();

            if ("RSA".equals(type)) {
                int keySize = (ctx.size == null) ? ctx.config.keyGeneration().rsa().defaultSize() : ctx.size;
                if (!rsaAllowed.contains(keySize)) {
                    throw ApplicationException.invalidRequest("Invalid RSA key size. Allowed: " + rsaAllowed);
                }
                KeyGenerationRequest r = new KeyGenerationRequest();
                r.setType(KeyGenerationRequest.TypeEnum.RSA);
                r.setKeySize(keySize);
                ctx.result = r;
                return;
            }
            if ("AES_GCM".equals(type)) {
                int keySize = (ctx.size == null) ? ctx.config.keyGeneration().aes().defaultSize() : ctx.size;
                if (!aesAllowed.contains(keySize)) {
                    throw ApplicationException.invalidRequest("Invalid AES key size. Allowed: " + aesAllowed);
                }
                KeyGenerationRequest r = new KeyGenerationRequest();
                r.setType(KeyGenerationRequest.TypeEnum.AES_GCM);
                r.setKeySize(keySize);
                ctx.result = r;
                return;
            }
            throw ApplicationException.unknownType("Unknown key type: " + ctx.typeParam);
        }
    }
}
