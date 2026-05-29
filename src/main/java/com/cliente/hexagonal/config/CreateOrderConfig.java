package com.cliente.hexagonal.config;

import com.cliente.hexagonal.adapters.out.CreateOrderAdapter;
import com.cliente.hexagonal.application.core.usecase.CreateOrderUseCase;
import com.cliente.hexagonal.application.ports.in.CreateOrderInputPort;
import com.cliente.hexagonal.application.ports.in.FindCustomerByIdInputPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CreateOrderConfig {

    @Bean
    public CreateOrderInputPort createOrderInputPort(FindCustomerByIdInputPort findCustomerByIdInputPort,
                                                     CreateOrderAdapter createOrderAdapter) {
        return new CreateOrderUseCase(findCustomerByIdInputPort, createOrderAdapter);
    }
}