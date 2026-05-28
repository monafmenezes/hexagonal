package com.cliente.hexagonal.application.core.usecase;

import com.cliente.hexagonal.application.core.domain.Customer;
import com.cliente.hexagonal.application.ports.in.FindCustomerByIdInputPort;
import com.cliente.hexagonal.application.ports.out.FindCustomerByIdOutputPort;

import java.util.Optional;

public class FindCustomerByIdUseCase implements FindCustomerByIdInputPort {

    private final FindCustomerByIdOutputPort findCustomerByIdOutputPort;

    public FindCustomerByIdUseCase(FindCustomerByIdOutputPort findCustomerByIdOutputPort) {
        this.findCustomerByIdOutputPort = findCustomerByIdOutputPort;
    }

    @Override
    public Customer find(String id) {
        return findCustomerByIdOutputPort.find(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }
}
