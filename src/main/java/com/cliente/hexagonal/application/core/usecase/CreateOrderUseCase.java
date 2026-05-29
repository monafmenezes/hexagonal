package com.cliente.hexagonal.application.core.usecase;

import com.cliente.hexagonal.application.core.domain.Order;
import com.cliente.hexagonal.application.ports.in.CreateOrderInputPort;
import com.cliente.hexagonal.application.ports.in.FindCustomerByIdInputPort;
import com.cliente.hexagonal.application.ports.out.CreateOrderOutputPort;

public class CreateOrderUseCase implements CreateOrderInputPort {

    private final FindCustomerByIdInputPort findCustomerByIdInputPort;
    private final CreateOrderOutputPort createOrderOutputPort;

    public CreateOrderUseCase(FindCustomerByIdInputPort findCustomerByIdInputPort,
                              CreateOrderOutputPort createOrderOutputPort) {
        this.findCustomerByIdInputPort = findCustomerByIdInputPort;
        this.createOrderOutputPort = createOrderOutputPort;
    }

    @Override
    public Order create(Order order) {
        findCustomerByIdInputPort.find(order.getCustomerId());
        return createOrderOutputPort.create(order);
    }
}