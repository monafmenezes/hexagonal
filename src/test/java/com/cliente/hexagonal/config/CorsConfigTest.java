package com.cliente.hexagonal.config;

import com.cliente.hexagonal.adapters.in.controller.CustomerController;
import com.cliente.hexagonal.adapters.in.controller.mapper.CustomerMapper;
import com.cliente.hexagonal.application.ports.in.DeleteCustomerByIdInputPort;
import com.cliente.hexagonal.application.ports.in.FindAllCustomersInputPort;
import com.cliente.hexagonal.application.ports.in.FindCustomerByIdInputPort;
import com.cliente.hexagonal.application.ports.in.InsertCustomerInputPort;
import com.cliente.hexagonal.application.ports.in.UpdateCustomerInputPort;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
@Import(CorsConfig.class)
class CorsConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean private InsertCustomerInputPort insertCustomerInputPort;
    @MockitoBean private FindCustomerByIdInputPort findCustomerByIdInputPort;
    @MockitoBean private FindAllCustomersInputPort findAllCustomersInputPort;
    @MockitoBean private UpdateCustomerInputPort updateCustomerInputPort;
    @MockitoBean private DeleteCustomerByIdInputPort deleteCustomerByIdInputPort;
    @MockitoBean private CustomerMapper customerMapper;

    @Test
    void shouldAllowCorsFromAllowedOrigin() throws Exception {
        mockMvc.perform(options("/api/v1/customers")
                        .header("Origin", "http://localhost:3000")
                        .header("Access-Control-Request-Method", "GET"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:3000"))
                .andExpect(header().exists("Access-Control-Allow-Methods"));
    }

    @Test
    void shouldRejectCorsFromUnknownOrigin() throws Exception {
        mockMvc.perform(options("/api/v1/customers")
                        .header("Origin", "http://malicious.com")
                        .header("Access-Control-Request-Method", "GET"))
                .andExpect(header().doesNotExist("Access-Control-Allow-Origin"));
    }
}