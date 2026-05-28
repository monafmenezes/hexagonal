package com.cliente.hexagonal.adapters.out.repository.mapper;

import com.cliente.hexagonal.adapters.out.repository.entity.CustomerEntity;
import com.cliente.hexagonal.application.core.domain.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerEntityMapper {
    @Mapping(target = "isValidCpf", source = "validCpf")
    CustomerEntity toCustomerEntity(Customer customer);

    @Mapping(target = "validCpf", source = "isValidCpf")
    Customer toCustomer(CustomerEntity customerEntity);
}
