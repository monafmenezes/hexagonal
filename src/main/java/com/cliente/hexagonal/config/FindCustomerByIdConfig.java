package com.cliente.hexagonal.config;

import com.cliente.hexagonal.application.core.usecase.FindCustomerByIdUseCase;
import com.cliente.hexagonal.application.ports.in.FindCustomerByIdInputPort;
import com.cliente.hexagonal.application.ports.out.FindCustomerByIdOutputPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FindCustomerByIdConfig {

    @Bean
    public FindCustomerByIdInputPort findCustomerByIdInputPort(
            FindCustomerByIdOutputPort findCustomerByIdOutputPort) {
        return new FindCustomerByIdUseCase(findCustomerByIdOutputPort);
    }
}
