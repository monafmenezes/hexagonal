package com.cliente.hexagonal.config;

import com.cliente.hexagonal.adapters.out.DeleteCustomerByIdAdatper;
import com.cliente.hexagonal.application.core.usecase.DeleteCustomerUseCase;
import com.cliente.hexagonal.application.ports.in.DeleteCustomerByIdInputPort;
import com.cliente.hexagonal.application.ports.in.FindCustomerByIdInputPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeleteCustomerDeleteConfig {

    @Bean
    public DeleteCustomerByIdInputPort deleteCustomerByIdInputPort(FindCustomerByIdInputPort findCustomerByIdInputPort,
                                                                   DeleteCustomerByIdAdatper deleteCustomerByIdAdatper) {
        return new DeleteCustomerUseCase(findCustomerByIdInputPort, deleteCustomerByIdAdatper);
    }
}
