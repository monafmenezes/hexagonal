package com.cliente.hexagonal.adapters.out.client.mapper;

import com.cliente.hexagonal.adapters.out.client.response.AddressResponse;
import com.cliente.hexagonal.application.core.domain.Address;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AddressResponseMapperTest {

    private final AddressResponseMapper addressResponseMapper = Mappers.getMapper(AddressResponseMapper.class);

    @Test
    void shouldMapResponseToDomain() {
        AddressResponse response = new AddressResponse();
        response.setStreet("Rua A");
        response.setCity("Fortaleza");
        response.setState("CE");

        Address address = addressResponseMapper.toAddress(response);

        assertEquals("Rua A", address.getStreet());
        assertEquals("Fortaleza", address.getCity());
        assertEquals("CE", address.getState());
    }
}
