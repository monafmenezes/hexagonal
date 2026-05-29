package com.cliente.hexagonal.application.ports.out;

import com.cliente.hexagonal.application.core.domain.Customer;

public interface UpdateCustomerOutputPort {
    void update(Customer customer);
}