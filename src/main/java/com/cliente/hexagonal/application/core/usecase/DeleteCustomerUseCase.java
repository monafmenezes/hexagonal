package com.cliente.hexagonal.application.core.usecase;

import com.cliente.hexagonal.application.ports.in.FindCustomerByIdInputPort;
import com.cliente.hexagonal.application.ports.out.DeleteCustomerOutputPort;

public class DeleteCustomerUseCase implements DeleteCustomerOutputPort {

    private final FindCustomerByIdInputPort findCustomerByIdInputPort;

    private final DeleteCustomerOutputPort deleteCustomerOutputPort;

    public DeleteCustomerUseCase(FindCustomerByIdInputPort findCustomerByIdInputPort, DeleteCustomerOutputPort deleteCustomerOutputPort) {
        this.findCustomerByIdInputPort = findCustomerByIdInputPort;
        this.deleteCustomerOutputPort = deleteCustomerOutputPort;
    }


    @Override
    public void delete(String id) {
        findCustomerByIdInputPort.find(id);
        deleteCustomerOutputPort.delete(id);
    }
}
