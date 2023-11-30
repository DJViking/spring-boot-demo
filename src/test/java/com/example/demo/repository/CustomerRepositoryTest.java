package com.example.demo.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.Customer;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@ActiveProfiles("it")
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "/testscript/TestDataSetup.sql")
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "/testscript/TestDataTeardown.sql")
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    @Transactional(readOnly = true)
    public void findById() {
        final Optional<Customer> customer = customerRepository.findById(1L);
        assertThat(customer).isNotNull();
        assertTrue(customer.isPresent());
        assertEquals(customer.get().getName(), "John Doe");
        System.out.println(customer.get());
    }

    @Test
    @Transactional(readOnly = true)
    public void findByName() {
        final Customer customer = customerRepository.findByName("John Doe");
        assertThat(customer).isNotNull();
        assertEquals(customer.getAddress(), "Bakersfield 2B");
        System.out.println(customer);
    }

}
