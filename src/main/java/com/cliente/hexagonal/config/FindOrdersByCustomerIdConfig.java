package com.cliente.hexagonal.config;

import com.cliente.hexagonal.adapters.out.FindOrdersByCustomerIdAdapter;
import com.cliente.hexagonal.application.core.usecase.FindOrdersByCustomerIdUseCase;
import com.cliente.hexagonal.application.ports.in.FindCustomerByIdInputPort;
import com.cliente.hexagonal.application.ports.in.FindOrdersByCustomerIdInputPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FindOrdersByCustomerIdConfig {

    @Bean
    public FindOrdersByCustomerIdInputPort findOrdersByCustomerIdInputPort(
            FindCustomerByIdInputPort findCustomerByIdInputPort,
            FindOrdersByCustomerIdAdapter findOrdersByCustomerIdAdapter) {
        return new FindOrdersByCustomerIdUseCase(findCustomerByIdInputPort, findOrdersByCustomerIdAdapter);
    }
}