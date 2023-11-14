package com.example.demo.client;

import java.util.Optional;

import com.example.demo.dto.CustomerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;

@Service
public class CustomerClient {

    private final RestClient restClient;

    @Autowired
    public CustomerClient(@Qualifier("firstRestClient") final RestClient restClient) {
        this.restClient = restClient;
    }

    public Optional<CustomerDTO> getCustomer(final Long customerNumber) {
        try {
            final CustomerDTO customer = restClient.get()
                .uri("/api/customer/{customerNumber}", customerNumber)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(CustomerDTO.class);
            return Optional.ofNullable(customer);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            return Optional.empty();
        }
    }

}
