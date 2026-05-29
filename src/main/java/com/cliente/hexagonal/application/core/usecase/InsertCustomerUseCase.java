package com.cliente.hexagonal.application.core.usecase;

import com.cliente.hexagonal.application.core.domain.Address;
import com.cliente.hexagonal.application.core.domain.Customer;
import com.cliente.hexagonal.application.ports.in.InsertCustomerInputPort;
import com.cliente.hexagonal.application.ports.out.FindAddresByZipCodeOutputPort;
import com.cliente.hexagonal.application.ports.out.InsertCustomerOutputPort;
import com.cliente.hexagonal.application.ports.out.SendCpfForValidationOutputPort;

public class InsertCustomerUseCase implements InsertCustomerInputPort {
    private final FindAddresByZipCodeOutputPort findAddresByZipCodeOutputPort;

    private final InsertCustomerOutputPort insertCustomerOutputPort;

    private final SendCpfForValidationOutputPort sendCpfForValidationOutputPort;

    public InsertCustomerUseCase(FindAddresByZipCodeOutputPort findAddresByZipCodeOutputPort,
                                 InsertCustomerOutputPort insertCustomerOutputPort, SendCpfForValidationOutputPort sendCpfForValidationOutputPort) {
        this.findAddresByZipCodeOutputPort = findAddresByZipCodeOutputPort;
        this.insertCustomerOutputPort = insertCustomerOutputPort;
        this.sendCpfForValidationOutputPort = sendCpfForValidationOutputPort;
    }

    @Override
    public void insert(Customer customer, String zipCode) {
        Address address = findAddresByZipCodeOutputPort.find(zipCode);
        customer.setAddress(address);
        insertCustomerOutputPort.insert(customer);
        sendCpfForValidationOutputPort.send(customer.getCpf());
    }
}
