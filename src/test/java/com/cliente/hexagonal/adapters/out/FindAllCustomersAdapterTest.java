package com.cliente.hexagonal.adapters.out;

import com.cliente.hexagonal.adapters.out.repository.CustomerRepository;
import com.cliente.hexagonal.adapters.out.repository.entity.CustomerEntity;
import com.cliente.hexagonal.adapters.out.repository.mapper.CustomerEntityMapper;
import com.cliente.hexagonal.application.core.domain.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FindAllCustomersAdapterTest {

    private final CustomerRepository repository = mock(CustomerRepository.class);
    private final CustomerEntityMapper mapper = mock(CustomerEntityMapper.class);
    private final FindAllCustomersAdapter adapter = new FindAllCustomersAdapter();

    {
        ReflectionTestUtils.setField(adapter, "customerRepository", repository);
        ReflectionTestUtils.setField(adapter, "customerEntityMapper", mapper);
    }

    @Test
    void shouldReturnMappedPageFromRepository() {
        Pageable pageable = PageRequest.of(0, 5);
        CustomerEntity entity = new CustomerEntity();
        Customer customer = new Customer();
        Page<CustomerEntity> entityPage = new PageImpl<>(List.of(entity), pageable, 1);

        when(repository.findAll(pageable)).thenReturn(entityPage);
        when(mapper.toCustomer(entity)).thenReturn(customer);

        Page<Customer> result = adapter.findAll(pageable);

        verify(repository).findAll(pageable);
        assertEquals(1, result.getTotalElements());
        assertEquals(customer, result.getContent().get(0));
    }
}
