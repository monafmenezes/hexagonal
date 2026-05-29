package com.cliente.hexagonal.application.core.usecase;

import com.cliente.hexagonal.application.core.domain.Customer;
import com.cliente.hexagonal.application.ports.out.FindAllCustomersOutputPort;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FindAllCustomersUseCaseTest {

    private final FindAllCustomersOutputPort outputPort = mock(FindAllCustomersOutputPort.class);
    private final FindAllCustomersUseCase useCase = new FindAllCustomersUseCase(outputPort);

    @Test
    void shouldDelegateToOutputPortAndReturnPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Customer> expected = new PageImpl<>(List.of(new Customer()), pageable, 1);
        when(outputPort.findAll(pageable)).thenReturn(expected);

        Page<Customer> result = useCase.findAll(pageable);

        verify(outputPort).findAll(pageable);
        assertEquals(1, result.getTotalElements());
    }
}
