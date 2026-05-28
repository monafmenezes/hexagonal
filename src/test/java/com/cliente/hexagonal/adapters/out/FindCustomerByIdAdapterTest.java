package com.cliente.hexagonal.adapters.out;

import com.cliente.hexagonal.adapters.out.repository.CustomerRepository;
import com.cliente.hexagonal.adapters.out.repository.entity.AddressEntity;
import com.cliente.hexagonal.adapters.out.repository.entity.CustomerEntity;
import com.cliente.hexagonal.adapters.out.repository.mapper.CustomerEntityMapper;
import com.cliente.hexagonal.application.core.domain.Customer;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FindCustomerByIdAdapterTest {

    @Test
    void shouldReturnMappedCustomerWhenEntityExists() {
        CustomerRepository customerRepository = mock(CustomerRepository.class);
        FindCustomerByIdAdapter adapter = new FindCustomerByIdAdapter();
        ReflectionTestUtils.setField(adapter, "customerRepository", customerRepository);
        ReflectionTestUtils.setField(adapter, "customerEntityMapper", Mappers.getMapper(CustomerEntityMapper.class));
        CustomerEntity entity = customerEntity();

        when(customerRepository.findById("id-1")).thenReturn(Optional.of(entity));

        Optional<Customer> customer = adapter.find("id-1");

        verify(customerRepository).findById("id-1");
        assertTrue(customer.isPresent());
        assertEquals("id-1", customer.get().getId());
        assertEquals("Maria", customer.get().getName());
        assertEquals(Boolean.TRUE, customer.get().getValidCpf());
        assertEquals("Rua A", customer.get().getAddress().getStreet());
    }

    @Test
    void shouldReturnEmptyWhenEntityDoesNotExist() {
        CustomerRepository customerRepository = mock(CustomerRepository.class);
        FindCustomerByIdAdapter adapter = new FindCustomerByIdAdapter();
        ReflectionTestUtils.setField(adapter, "customerRepository", customerRepository);
        ReflectionTestUtils.setField(adapter, "customerEntityMapper", Mappers.getMapper(CustomerEntityMapper.class));

        when(customerRepository.findById("id-1")).thenReturn(Optional.empty());

        Optional<Customer> customer = adapter.find("id-1");

        verify(customerRepository).findById("id-1");
        assertTrue(customer.isEmpty());
    }

    private CustomerEntity customerEntity() {
        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setStreet("Rua A");
        addressEntity.setCity("Fortaleza");
        addressEntity.setState("CE");
        CustomerEntity entity = new CustomerEntity();
        entity.setId("id-1");
        entity.setName("Maria");
        entity.setCpf("12345678901");
        entity.setAddress(addressEntity);
        entity.setIsValidCpf(true);
        return entity;
    }
}
