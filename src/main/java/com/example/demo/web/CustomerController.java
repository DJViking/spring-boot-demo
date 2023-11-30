package com.example.demo.web;

import com.example.demo.dto.CustomerDTO;
import com.example.demo.domain.Customer;
import com.example.demo.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(final CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<CustomerDTO> getCustomerByNumber(final Long customerNumber) {
        return customerService.getCustomer(customerNumber)
            .map(this::toDto)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    private CustomerDTO toDto(final Customer customer) {
        return CustomerDTO.builder()
            .customerNumber(customer.getId())
            .name(customer.getName())
            .address(customer.getAddress())
            .build();
    }
}
