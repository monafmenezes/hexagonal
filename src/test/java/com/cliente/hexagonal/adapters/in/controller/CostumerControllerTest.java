package com.cliente.hexagonal.adapters.in.controller;

import com.cliente.hexagonal.adapters.in.controller.mapper.CustomerMapper;
import com.cliente.hexagonal.adapters.in.controller.request.CustomerRequest;
import com.cliente.hexagonal.application.core.domain.Customer;
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

class CostumerControllerTest {

    @Test
    void shouldMapRequestAndDelegateToInputPort() {
        InsertCustomerInputPort insertCustomerInputPort = mock(InsertCustomerInputPort.class);
        CostumerController controller = new CostumerController();
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
}
