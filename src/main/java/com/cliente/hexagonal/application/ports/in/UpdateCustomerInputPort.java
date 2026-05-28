package com.cliente.hexagonal.application.ports.in;

import com.cliente.hexagonal.application.core.domain.Customer;

public interface UpdateCustomerInputPort {
    void update(Customer customer, String zipCode);
}
