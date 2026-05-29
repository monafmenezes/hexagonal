package com.cliente.hexagonal.config;

import com.cliente.hexagonal.adapters.out.FindAllCustomersAdapter;
import com.cliente.hexagonal.application.core.usecase.FindAllCustomersUseCase;
import com.cliente.hexagonal.application.ports.in.FindAllCustomersInputPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FindAllCustomersConfig {

    @Bean
    public FindAllCustomersInputPort findAllCustomersInputPort(FindAllCustomersAdapter adapter) {
        return new FindAllCustomersUseCase(adapter);
    }
}