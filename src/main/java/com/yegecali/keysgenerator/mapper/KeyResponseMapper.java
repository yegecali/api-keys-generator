package com.yegecali.keysgenerator.mapper;

import com.yegecali.keysgenerator.dto.KeyResponse;
import com.yegecali.keysgenerator.model.KeyModel;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class KeyResponseMapper {

    public KeyResponse map(KeyModel model) {
        return new KeyResponse(
            model.getType(),
            model.getKeySize(),
            model.getPublicKey(),
            model.getPrivateKey(),
            model.getSymmetricKey(),
            model.getIv(),
            model.getMetadata()
        );
    }
}
