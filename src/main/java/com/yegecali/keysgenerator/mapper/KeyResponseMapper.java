package com.yegecali.keysgenerator.mapper;

import com.yegecali.keysgenerator.openapi.model.KeyResponse;
import com.yegecali.keysgenerator.model.KeyModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface KeyResponseMapper {
    KeyResponse map(KeyModel model);
}
