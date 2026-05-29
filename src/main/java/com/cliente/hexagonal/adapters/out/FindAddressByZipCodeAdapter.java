package com.cliente.hexagonal.adapters.out;

import com.cliente.hexagonal.adapters.out.client.FindAddressByZipCodeClient;
import com.cliente.hexagonal.adapters.out.client.mapper.AddressResponseMapper;
import com.cliente.hexagonal.adapters.out.client.response.AddressResponse;
import com.cliente.hexagonal.application.core.domain.Address;
import com.cliente.hexagonal.application.core.exception.ZipCodeNotFoundException;
import com.cliente.hexagonal.application.ports.out.FindAddresByZipCodeOutputPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FindAddressByZipCodeAdapter implements FindAddresByZipCodeOutputPort {

    @Autowired
    private FindAddressByZipCodeClient findAddressByZipCodeClient;

    @Autowired
    private AddressResponseMapper addressResponseMapper;

    @Override
    public Address find(String zipCode) {
        AddressResponse addressResponse = findAddressByZipCodeClient.find(zipCode);
        if (addressResponse == null || addressResponse.getStreet() == null) {
            throw new ZipCodeNotFoundException(zipCode);
        }
        return addressResponseMapper.toAddress(addressResponse);
    }
}