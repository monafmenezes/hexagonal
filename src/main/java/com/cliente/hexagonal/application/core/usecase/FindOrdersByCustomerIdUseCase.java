package com.cliente.hexagonal.application.core.usecase;

import com.cliente.hexagonal.application.core.domain.Order;
import com.cliente.hexagonal.application.ports.in.FindCustomerByIdInputPort;
import com.cliente.hexagonal.application.ports.in.FindOrdersByCustomerIdInputPort;
import com.cliente.hexagonal.application.ports.out.FindOrdersByCustomerIdOutputPort;

import java.util.List;

public class FindOrdersByCustomerIdUseCase implements FindOrdersByCustomerIdInputPort {

    private final FindCustomerByIdInputPort findCustomerByIdInputPort;
    private final FindOrdersByCustomerIdOutputPort findOrdersByCustomerIdOutputPort;

    public FindOrdersByCustomerIdUseCase(FindCustomerByIdInputPort findCustomerByIdInputPort,
                                         FindOrdersByCustomerIdOutputPort findOrdersByCustomerIdOutputPort) {
        this.findCustomerByIdInputPort = findCustomerByIdInputPort;
        this.findOrdersByCustomerIdOutputPort = findOrdersByCustomerIdOutputPort;
    }

    @Override
    public List<Order> findByCustomerId(String customerId) {
        findCustomerByIdInputPort.find(customerId);
        return findOrdersByCustomerIdOutputPort.findByCustomerId(customerId);
    }
}