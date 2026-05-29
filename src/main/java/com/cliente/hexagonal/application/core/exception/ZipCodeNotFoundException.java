package com.cliente.hexagonal.application.core.exception;

public class ZipCodeNotFoundException extends RuntimeException {

    public ZipCodeNotFoundException(String zipCode) {
        super("Endereço não encontrado para o CEP: " + zipCode);
    }
}
