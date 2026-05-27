package com.cliente.hexagonal.application.ports.out;

import com.cliente.hexagonal.application.core.domain.Address;

public interface FindAddresByZipCodeOutputPort {
    Address find(String zipCode);
}
