package com.cliente.hexagonal.application.ports.out;

import com.cliente.hexagonal.application.core.domain.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FindAllCustomersOutputPort {
    Page<Customer> findAll(Pageable pageable);
}
