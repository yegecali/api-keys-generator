package com.yegecali.keysgenerator.resources;

import com.yegecali.keysgenerator.dto.KeyResponse;
import com.yegecali.keysgenerator.model.KeyModel;
import com.yegecali.keysgenerator.service.KeyGenerationRequest;
import com.yegecali.keysgenerator.service.KeyGenerator;
import com.yegecali.keysgenerator.factory.KeyGeneratorFactory;
import com.yegecali.keysgenerator.validator.KeyRequestValidator;
import com.yegecali.keysgenerator.mapper.KeyResponseMapper;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

@Path("/keys")
public class RsaKeyResource {

    private static final Logger LOG = Logger.getLogger(RsaKeyResource.class);

    @Inject
    KeyRequestValidator validator;

    @Inject
    KeyGeneratorFactory factory;

    @Inject
    KeyResponseMapper mapper;

    // Legacy endpoint for compatibility: /keys/rsa
    @GET
    @Path("/rsa")
    @Produces(MediaType.APPLICATION_JSON)
    public Response generateRsa(@QueryParam("size") Integer size) {
        return generateGeneric("RSA", size);
    }

    // Generic endpoint: /keys?type=RSA|AES_GCM&size=...
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response generateGeneric(@QueryParam("type") String typeParam,
                                    @QueryParam("size") Integer size) {
        KeyGenerationRequest req = validator.validateAndBuild(typeParam, size);
        KeyGenerator generator = factory.get(req.getType());
        KeyModel model = generator.generate(req);
        KeyResponse resp = mapper.map(model);
        return Response.ok(resp).build();
    }
}
