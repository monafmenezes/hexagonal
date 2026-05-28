package com.cliente.hexagonal.adapters.in.controller;

import com.cliente.hexagonal.adapters.in.controller.mapper.CustomerMapper;
import com.cliente.hexagonal.adapters.in.controller.request.CustomerRequest;
import com.cliente.hexagonal.adapters.in.controller.response.CustomerResponse;
import com.cliente.hexagonal.application.core.domain.Address;
import com.cliente.hexagonal.application.core.domain.Customer;
import com.cliente.hexagonal.application.ports.in.FindCustomerByIdInputPort;
import com.cliente.hexagonal.application.ports.in.InsertCustomerInputPort;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class CustomerControllerTest {

    @Test
    void shouldMapRequestAndDelegateToInputPort() {
        InsertCustomerInputPort insertCustomerInputPort = mock(InsertCustomerInputPort.class);
        CustomerController controller = new CustomerController();
        ReflectionTestUtils.setField(controller, "insertCustomerInputPort", insertCustomerInputPort);
        ReflectionTestUtils.setField(controller, "customerMapper", Mappers.getMapper(CustomerMapper.class));
        CustomerRequest request = new CustomerRequest();
        request.setName("Maria");
        request.setCpf("12345678901");
        request.setZipCode("60100-000");

        ResponseEntity<Void> response = controller.insert(request);

        ArgumentCaptor<Customer> customerCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(insertCustomerInputPort).insert(customerCaptor.capture(), org.mockito.ArgumentMatchers.eq("60100-000"));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
        assertEquals("Maria", customerCaptor.getValue().getName());
        assertEquals("12345678901", customerCaptor.getValue().getCpf());
    }

    @Test
    void shouldFindCustomerByIdAndReturnResponse() {
        FindCustomerByIdInputPort findCustomerByIdInputPort = mock(FindCustomerByIdInputPort.class);
        CustomerController controller = new CustomerController();
        ReflectionTestUtils.setField(controller, "findCustomerByIdInputPort", findCustomerByIdInputPort);
        ReflectionTestUtils.setField(controller, "customerMapper", Mappers.getMapper(CustomerMapper.class));
        Customer customer = new Customer(
                "id-1",
                "Maria",
                "12345678901",
                new Address("Rua A", "Fortaleza", "CE"),
                true);

        org.mockito.Mockito.when(findCustomerByIdInputPort.find("id-1")).thenReturn(customer);

        ResponseEntity<CustomerResponse> response = controller.findById("id-1");

        verify(findCustomerByIdInputPort).find("id-1");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Maria", response.getBody().getName());
        assertEquals("12345678901", response.getBody().getCpf());
        assertEquals(Boolean.TRUE, response.getBody().getIsValidCpf());
        assertEquals("Rua A", response.getBody().getAddress().getStreet());
    }
}
