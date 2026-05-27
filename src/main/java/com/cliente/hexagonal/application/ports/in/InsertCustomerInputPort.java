package com.cliente.hexagonal.application.ports.in;

import com.cliente.hexagonal.application.core.domain.Customer;

public interface InsertCustomerInputPort {
    void insert(Customer customer, String zipCode);
}
