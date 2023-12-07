package com.example.demo.service;

import java.util.function.Function;

import org.springframework.context.annotation.Bean;

import com.example.demo.data.Request;
import com.example.demo.data.Response;

public class CustomerProcessor {

    @Bean
    public Function<Request, Response> process() {
        return this::processRequest;
    }

    public Response processRequest(final Request request) {
        return new Response();
    }

}