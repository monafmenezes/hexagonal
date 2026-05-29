package com.cliente.hexagonal.adapters.out;

import com.cliente.hexagonal.adapters.out.repository.CustomerRepository;
import com.cliente.hexagonal.adapters.out.repository.entity.CustomerEntity;
import com.cliente.hexagonal.adapters.out.repository.mapper.CustomerEntityMapper;
import com.cliente.hexagonal.application.core.domain.Address;
import com.cliente.hexagonal.application.core.domain.Customer;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class UpdateCustomerAdapterTest {

    @Test
    void shouldMapCustomerAndSaveEntity() {
        CustomerRepository customerRepository = mock(CustomerRepository.class);
        UpdateCustomerAdapter adapter = new UpdateCustomerAdapter();
        ReflectionTestUtils.setField(adapter, "customerRepository", customerRepository);
        ReflectionTestUtils.setField(adapter, "customerEntityMapper", Mappers.getMapper(CustomerEntityMapper.class));
        Customer customer = new Customer("id-1", "Maria", "12345678901", new Address("Rua A", "Fortaleza", "CE"), true);

        adapter.update(customer);

        ArgumentCaptor<CustomerEntity> entityCaptor = ArgumentCaptor.forClass(CustomerEntity.class);
        verify(customerRepository).save(entityCaptor.capture());
        CustomerEntity entity = entityCaptor.getValue();
        assertEquals("id-1", entity.getId());
        assertEquals("Maria", entity.getName());
        assertEquals("12345678901", entity.getCpf());
        assertEquals(Boolean.TRUE, entity.getIsValidCpf());
        assertEquals("Rua A", entity.getAddress().getStreet());
    }
}
