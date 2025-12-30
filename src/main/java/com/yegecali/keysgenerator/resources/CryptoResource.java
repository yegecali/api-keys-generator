package com.yegecali.keysgenerator.resources;

import com.yegecali.keysgenerator.openapi.model.CryptoRequest;
import com.yegecali.keysgenerator.openapi.model.CryptoResponse;
import com.yegecali.keysgenerator.service.strategies.encrypt.CryptoStrategy;
import com.yegecali.keysgenerator.service.strategies.encrypt.CryptoStrategyFactory;
import com.yegecali.keysgenerator.validator.CryptoRequestValidator;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/crypto")
public class CryptoResource {

    @Inject
    CryptoRequestValidator validator;

    @Inject
    CryptoStrategyFactory factory;

    @POST
    @Path("/encrypt")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CryptoResponse encrypt(CryptoRequest req) throws Exception {
        validator.validateForEncrypt(req);
        CryptoStrategy strategy = factory.get(req.getType());
        return strategy.encrypt(req);
    }

    @POST
    @Path("/decrypt")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CryptoResponse decrypt(CryptoRequest req) throws Exception {
        validator.validateForDecrypt(req);
        CryptoStrategy strategy = factory.get(req.getType());
        return strategy.decrypt(req);
    }
}
