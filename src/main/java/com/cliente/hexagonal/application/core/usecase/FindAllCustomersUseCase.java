package com.cliente.hexagonal.application.core.usecase;

import com.cliente.hexagonal.application.core.domain.Customer;
import com.cliente.hexagonal.application.ports.in.FindAllCustomersInputPort;
import com.cliente.hexagonal.application.ports.out.FindAllCustomersOutputPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class FindAllCustomersUseCase implements FindAllCustomersInputPort {

    private final FindAllCustomersOutputPort findAllCustomersOutputPort;

    public FindAllCustomersUseCase(FindAllCustomersOutputPort findAllCustomersOutputPort) {
        this.findAllCustomersOutputPort = findAllCustomersOutputPort;
    }

    @Override
    public Page<Customer> findAll(Pageable pageable) {
        return findAllCustomersOutputPort.findAll(pageable);
    }
}