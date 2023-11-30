package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>, QuerydslPredicateExecutor<Customer> {

    Customer findByName(String name);

}
