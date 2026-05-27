package com.cliente.hexagonal.adapters.out.repository.mapper;

import com.cliente.hexagonal.adapters.out.repository.entity.CustomerEntity;
import com.cliente.hexagonal.application.core.domain.Address;
import com.cliente.hexagonal.application.core.domain.Customer;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomerEntityMapperTest {

    private final CustomerEntityMapper customerEntityMapper = Mappers.getMapper(CustomerEntityMapper.class);

    @Test
    void shouldMapDomainToEntity() {
        Customer customer = new Customer("id-1", "Maria", "12345678901", new Address("Rua A", "Fortaleza", "CE"), true);

        CustomerEntity entity = customerEntityMapper.toCustomerEntity(customer);

        assertEquals("id-1", entity.getId());
        assertEquals("Maria", entity.getName());
        assertEquals("12345678901", entity.getCpf());
        assertEquals(Boolean.TRUE, entity.getIsValidCpf());
        assertEquals("Rua A", entity.getAddress().getStreet());
        assertEquals("Fortaleza", entity.getAddress().getCity());
        assertEquals("CE", entity.getAddress().getState());
    }
}
