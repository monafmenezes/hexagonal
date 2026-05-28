package com.cliente.hexagonal.config;

import com.cliente.hexagonal.adapters.out.FindAddressByZipCodeAdapter;
import com.cliente.hexagonal.adapters.out.InsertCustomerAdapter;
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
            FindAddressByZipCodeAdapter findAddressByZipCodeAdapter,
            InsertCustomerAdapter insertCustomerAdapter) {
        return new InsertCustomerUseCase(findAddressByZipCodeAdapter, insertCustomerAdapter);
    }
}
