package com.cliente.hexagonal.adapters.out;

import com.cliente.hexagonal.adapters.out.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class DeleteCustomerByIdAdapterTest {

    @Test
    void shouldCallDeleteByIdOnRepository() {
        CustomerRepository customerRepository = mock(CustomerRepository.class);
        DeleteCustomerByIdAdatper adapter = new DeleteCustomerByIdAdatper();
        ReflectionTestUtils.setField(adapter, "customerRepository", customerRepository);

        adapter.delete("id-1");

        verify(customerRepository).deleteById("id-1");
    }
}