package com.example.demo.client;

import java.util.Optional;

import com.example.demo.dto.BookDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class BookClient {

    private final RestTemplate restTemplate;

    public BookClient(@Qualifier("secondRestTemplate") final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Optional<BookDTO> getBook(final Long bookNumber) {
        try {
            final ResponseEntity<BookDTO> response = restTemplate.exchange(
                "/api/book/{bookNumber}",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                BookDTO.class,
                bookNumber
            );
            return Optional.ofNullable(response.getBody());
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            return Optional.empty();
        }
    }

}
