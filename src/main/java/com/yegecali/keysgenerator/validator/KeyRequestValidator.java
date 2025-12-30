package com.yegecali.keysgenerator.validator;

import com.yegecali.keysgenerator.openapi.model.KeyGenerationRequest;
import com.yegecali.keysgenerator.exception.ApplicationException;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@ApplicationScoped
public class KeyRequestValidator {

    private static final List<Integer> RSA_ALLOWED = Arrays.asList(2048, 3072, 4096);
    private static final List<Integer> AES_ALLOWED = Arrays.asList(128, 192, 256);

    public KeyGenerationRequest validateAndBuild(String typeParam, Integer size) {
        ValidationContext ctx = new ValidationContext(typeParam, size);
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
        String normalizedType;
        KeyGenerationRequest result;

        ValidationContext(String typeParam, Integer size) {
            this.typeParam = typeParam;
            this.size = size;
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
            if ("RSA".equals(type)) {
                int keySize = (ctx.size == null) ? 2048 : ctx.size;
                if (!RSA_ALLOWED.contains(keySize)) {
                    throw ApplicationException.invalidRequest("Invalid RSA key size. Allowed: " + RSA_ALLOWED);
                }
                KeyGenerationRequest r = new KeyGenerationRequest();
                r.setType(KeyGenerationRequest.TypeEnum.RSA);
                r.setKeySize(keySize);
                ctx.result = r;
                return;
            }
            if ("AES_GCM".equals(type)) {
                int keySize = (ctx.size == null) ? 256 : ctx.size;
                if (!AES_ALLOWED.contains(keySize)) {
                    throw ApplicationException.invalidRequest("Invalid AES key size. Allowed: " + AES_ALLOWED);
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
