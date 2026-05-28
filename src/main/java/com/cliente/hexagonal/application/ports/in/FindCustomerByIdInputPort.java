package com.cliente.hexagonal.application.ports.in;

import com.cliente.hexagonal.application.core.domain.Customer;

public interface FindCustomerByIdInputPort {
    Customer find(String id);
}
