package com.cliente.hexagonal.application.core.exception;

public class CustomerNotFoundException extends RuntimeException {

    public CustomerNotFoundException(String id) {
        super("Cliente não encontrado com o id: " + id);
    }
}
