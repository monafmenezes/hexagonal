package com.cliente.hexagonal.config;

import com.cliente.hexagonal.application.core.usecase.InsertCustomerUseCase;
import com.cliente.hexagonal.application.ports.in.InsertCustomerInputPort;
import com.cliente.hexagonal.application.ports.out.FindAddresByZipCodeOutputPort;
import com.cliente.hexagonal.application.ports.out.InsertCustomerOutputPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InsertCustomerConfig {

    @Bean
    public InsertCustomerInputPort insertCustomerInputPort(
            FindAddresByZipCodeOutputPort findAddresByZipCodeOutputPort,
            InsertCustomerOutputPort insertCustomerOutputPort) {
        return new InsertCustomerUseCase(findAddresByZipCodeOutputPort, insertCustomerOutputPort);
    }
}
