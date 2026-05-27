package com.cliente.hexagonal.adapters.out.client.mapper;

import com.cliente.hexagonal.adapters.out.client.response.AddressResponse;
import com.cliente.hexagonal.application.core.domain.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressResponseMapper {
    Address toAddress(AddressResponse addressResponse);
}
