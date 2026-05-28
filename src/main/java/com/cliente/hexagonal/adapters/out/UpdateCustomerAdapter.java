package com.cliente.hexagonal.adapters.out;

import com.cliente.hexagonal.adapters.out.repository.CustomerRepository;
import com.cliente.hexagonal.adapters.out.repository.entity.CustomerEntity;
import com.cliente.hexagonal.adapters.out.repository.mapper.CustomerEntityMapper;
import com.cliente.hexagonal.application.core.domain.Customer;
import com.cliente.hexagonal.application.ports.out.UpdateCustomerOutputPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UpdateCustomerAdapter implements UpdateCustomerOutputPort {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired private CustomerEntityMapper customerEntityMapper;

    @Override
    public void update(Customer customer) {
        CustomerEntity customerEntity = customerEntityMapper.toCustomerEntity(customer);
        customerRepository.save(customerEntity);
    }
}
