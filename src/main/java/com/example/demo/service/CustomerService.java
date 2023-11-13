package com.example.demo.service;

import java.util.Optional;

import com.example.demo.model.Customer;
import com.example.demo.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(final CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Optional<Customer> getCustomer(final Long customerNumber) {
        return customerRepository.findById(customerNumber);
    }

}
