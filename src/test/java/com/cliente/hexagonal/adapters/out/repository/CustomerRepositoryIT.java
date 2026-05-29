package com.cliente.hexagonal.adapters.out.repository;

import com.cliente.hexagonal.adapters.out.repository.entity.AddressEntity;
import com.cliente.hexagonal.adapters.out.repository.entity.CustomerEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@Testcontainers
class CustomerRepositoryIT {

    @Container
    @ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0");

    @Autowired
    CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        customerRepository.deleteAll();
    }

    @Test
    void shouldSaveAndFindCustomerById() {
        CustomerEntity entity = buildCustomerEntity("id-1", "Maria Silva", "12345678901");

        customerRepository.save(entity);

        Optional<CustomerEntity> found = customerRepository.findById("id-1");
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Maria Silva");
        assertThat(found.get().getCpf()).isEqualTo("12345678901");
        assertThat(found.get().getIsValidCpf()).isTrue();
    }

    @Test
    void shouldDeleteCustomerById() {
        CustomerEntity entity = buildCustomerEntity("id-2", "João Costa", "98765432100");
        customerRepository.save(entity);

        customerRepository.deleteById("id-2");

        assertThat(customerRepository.findById("id-2")).isEmpty();
    }

    @Test
    void shouldUpdateCustomer() {
        CustomerEntity entity = buildCustomerEntity("id-3", "Ana Souza", "11122233344");
        customerRepository.save(entity);

        entity.setName("Ana Oliveira");
        customerRepository.save(entity);

        Optional<CustomerEntity> updated = customerRepository.findById("id-3");
        assertThat(updated).isPresent();
        assertThat(updated.get().getName()).isEqualTo("Ana Oliveira");
    }

    private CustomerEntity buildCustomerEntity(String id, String name, String cpf) {
        AddressEntity address = new AddressEntity();
        address.setStreet("Rua das Flores");
        address.setCity("Fortaleza");
        address.setState("CE");

        CustomerEntity entity = new CustomerEntity();
        entity.setId(id);
        entity.setName(name);
        entity.setCpf(cpf);
        entity.setAddress(address);
        entity.setIsValidCpf(true);
        return entity;
    }
}