package com.cliente.hexagonal.adapters.in.controller;

import com.cliente.hexagonal.adapters.in.controller.mapper.CustomerMapper;
import com.cliente.hexagonal.adapters.in.controller.request.CustomerRequest;
import com.cliente.hexagonal.adapters.in.controller.response.CustomerResponse;
import com.cliente.hexagonal.application.core.domain.Customer;
import com.cliente.hexagonal.application.ports.in.FindCustomerByIdInputPort;
import com.cliente.hexagonal.application.ports.in.InsertCustomerInputPort;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    @Autowired
    private InsertCustomerInputPort insertCustomerInputPort;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private  FindCustomerByIdInputPort findCustomerByIdInputPort;

    @PostMapping
    public ResponseEntity<Void> insert(@Valid @RequestBody CustomerRequest customerRequest){
        Customer customer = customerMapper.toCustomer(customerRequest);
        insertCustomerInputPort.insert(customer, customerRequest.getZipCode());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> findById(@PathVariable final String id) {
        Customer customer = findCustomerByIdInputPort.find(id);
        CustomerResponse customerResponse = customerMapper.toCustomerResponse(customer);
        return ResponseEntity.ok(customerResponse);
    }
}
