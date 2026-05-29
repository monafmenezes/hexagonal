package com.cliente.hexagonal.application.ports.in;

import com.cliente.hexagonal.application.core.domain.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FindAllCustomersInputPort {
    Page<Customer> findAll(Pageable pageable);
}