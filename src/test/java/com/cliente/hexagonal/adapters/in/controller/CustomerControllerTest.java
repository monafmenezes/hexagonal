package com.cliente.hexagonal.adapters.in.controller;

import com.cliente.hexagonal.adapters.in.controller.mapper.CustomerMapper;
import com.cliente.hexagonal.adapters.in.controller.request.CustomerRequest;
import com.cliente.hexagonal.application.core.domain.Address;
import com.cliente.hexagonal.application.core.domain.Customer;
import com.cliente.hexagonal.application.ports.in.DeleteCustomerByIdInputPort;
import com.cliente.hexagonal.application.ports.in.FindAllCustomersInputPort;
import com.cliente.hexagonal.application.ports.in.FindCustomerByIdInputPort;
import com.cliente.hexagonal.application.ports.in.InsertCustomerInputPort;
import com.cliente.hexagonal.application.ports.in.UpdateCustomerInputPort;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private InsertCustomerInputPort insertCustomerInputPort;

    @MockitoBean
    private FindCustomerByIdInputPort findCustomerByIdInputPort;

    @MockitoBean
    private UpdateCustomerInputPort updateCustomerInputPort;

    @MockitoBean
    private DeleteCustomerByIdInputPort deleteCustomerByIdInputPort;

    @MockitoBean
    private FindAllCustomersInputPort findAllCustomersInputPort;

    @MockitoBean
    private CustomerMapper customerMapper;

    @Test
    void shouldInsertCustomerWithValidRequest() throws Exception {
        CustomerRequest request = validRequest();

        when(customerMapper.toCustomer(any())).thenReturn(new Customer());

        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(insertCustomerInputPort).insert(any(), eq("60100-000"));
    }

    @Test
    void shouldReturn400WhenNameIsBlank() throws Exception {
        CustomerRequest request = validRequest();
        request.setName("");

        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("name")));
    }

    @Test
    void shouldReturn400WhenCpfHasWrongLength() throws Exception {
        CustomerRequest request = validRequest();
        request.setCpf("123");

        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("cpf")));
    }

    @Test
    void shouldReturn400WhenCpfHasNonNumericChars() throws Exception {
        CustomerRequest request = validRequest();
        request.setCpf("1234567890A");

        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("cpf")));
    }

    @Test
    void shouldReturn400WhenZipCodeIsInvalid() throws Exception {
        CustomerRequest request = validRequest();
        request.setZipCode("INVALID");

        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("zipCode")));
    }

    @Test
    void shouldFindCustomerById() throws Exception {
        Customer customer = new Customer("id-1", "Maria", "12345678901",
                new Address("Rua A", "Fortaleza", "CE"), true);

        com.cliente.hexagonal.adapters.in.controller.response.CustomerResponse response =
                new com.cliente.hexagonal.adapters.in.controller.response.CustomerResponse();
        response.setName("Maria");
        response.setCpf("12345678901");

        when(findCustomerByIdInputPort.find("id-1")).thenReturn(customer);
        when(customerMapper.toCustomerResponse(any())).thenReturn(response);

        mockMvc.perform(get("/api/v1/customers/id-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Maria"));

        verify(findCustomerByIdInputPort).find("id-1");
    }

    @Test
    void shouldReturnPagedCustomers() throws Exception {
        Customer customer = new Customer("id-1", "Maria", "12345678901",
                new Address("Rua A", "Fortaleza", "CE"), true);

        com.cliente.hexagonal.adapters.in.controller.response.CustomerResponse response =
                new com.cliente.hexagonal.adapters.in.controller.response.CustomerResponse();
        response.setName("Maria");

        when(findAllCustomersInputPort.findAll(any())).thenReturn(
                new PageImpl<>(List.of(customer), PageRequest.of(0, 10), 1));
        when(customerMapper.toCustomerResponse(any())).thenReturn(response);

        mockMvc.perform(get("/api/v1/customers")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Maria"))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1));
    }

    private CustomerRequest validRequest() {
        CustomerRequest request = new CustomerRequest();
        request.setName("Maria");
        request.setCpf("12345678901");
        request.setZipCode("60100-000");
        return request;
    }
}
