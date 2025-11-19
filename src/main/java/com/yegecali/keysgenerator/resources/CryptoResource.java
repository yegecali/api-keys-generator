package com.yegecali.keysgenerator.resources;

import com.yegecali.keysgenerator.dto.CryptoRequest;
import com.yegecali.keysgenerator.dto.CryptoResponse;
import com.yegecali.keysgenerator.crypto.strategies.CryptoStrategy;
import com.yegecali.keysgenerator.factory.CryptoStrategyFactory;
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

