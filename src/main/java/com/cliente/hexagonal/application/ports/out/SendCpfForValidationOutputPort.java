package com.cliente.hexagonal.application.ports.out;

public interface SendCpfForValidation {
    void send(String cpf);
}
