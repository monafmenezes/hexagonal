package com.cliente.hexagonal.adapters.out;

import com.cliente.hexagonal.adapters.out.client.FindAddressByZipCodeClient;
import com.cliente.hexagonal.adapters.out.client.mapper.AddressResponseMapper;
import com.cliente.hexagonal.adapters.out.client.response.AddressResponse;
import com.cliente.hexagonal.application.core.domain.Address;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FindAddressByZipCodeAdapterTest {

    @Test
    void shouldFindAddressByZipCodeAndMapResponse() {
        FindAddressByZipCodeClient client = mock(FindAddressByZipCodeClient.class);
        FindAddressByZipCodeAdapter adapter = new FindAddressByZipCodeAdapter();
        ReflectionTestUtils.setField(adapter, "findAddressByZipCodeClient", client);
        ReflectionTestUtils.setField(adapter, "addressResponseMapper", Mappers.getMapper(AddressResponseMapper.class));
        AddressResponse response = new AddressResponse();
        response.setStreet("Rua A");
        response.setCity("Fortaleza");
        response.setState("CE");

        when(client.find("60100-000")).thenReturn(response);

        Address address = adapter.find("60100-000");

        verify(client).find("60100-000");
        assertEquals("Rua A", address.getStreet());
        assertEquals("Fortaleza", address.getCity());
        assertEquals("CE", address.getState());
    }
}
