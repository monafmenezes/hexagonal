package com.cliente.hexagonal.application.ports.out;

import com.cliente.hexagonal.application.core.domain.Customer;

import java.util.Optional;

public interface FindCustomerByIdOutputPort {
    Optional<Customer> find(String id);
}
