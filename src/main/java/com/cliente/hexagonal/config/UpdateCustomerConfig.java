package com.cliente.hexagonal.config;

import com.cliente.hexagonal.adapters.out.FindAddressByZipCodeAdapter;
import com.cliente.hexagonal.adapters.out.UpdateCustomerAdapter;
import com.cliente.hexagonal.application.core.usecase.UpdateCustomerUseCase;
import com.cliente.hexagonal.application.ports.in.FindCustomerByIdInputPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UpdateCustomerConfig {
    @Bean
    public UpdateCustomerUseCase updateCustomerUseCase(FindCustomerByIdInputPort findCustomerByIdInputPort,
                                                       FindAddressByZipCodeAdapter findAddressByZipCodeAdapter,
                                                       UpdateCustomerAdapter updateCustomerAdapter) {
        return new UpdateCustomerUseCase(findCustomerByIdInputPort, findAddressByZipCodeAdapter, updateCustomerAdapter);
    }
}