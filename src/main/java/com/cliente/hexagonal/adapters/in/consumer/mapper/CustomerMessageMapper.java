package com.cliente.hexagonal.adapters.in.consumer.mapper;

import com.cliente.hexagonal.adapters.in.consumer.message.CustomerMessage;
import com.cliente.hexagonal.application.core.domain.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerMessageMapper {

    @Mapping(target = "address", ignore = true)
    @Mapping(target = "validCpf", source = "isValidCpf")
    Customer toCustomer(CustomerMessage customerMessage);
}
