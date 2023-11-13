package com.example.demo.client;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.util.Optional;

import com.example.demo.dto.BookDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

@SpringBootTest
@ActiveProfiles("it")
@ExtendWith(SpringExtension.class)
public class BookClientTest {

    private final static Long BOOK_NUMBER = 12345L;

    private BookClient bookClient;

    private MockRestServiceServer mockServer;

    private ObjectMapper objectMapper;

    public BookClientTest() {
        final RestTemplate restTemplate = new RestTemplateBuilder()
            .uriTemplateHandler(new DefaultUriBuilderFactory())
            .build();
        this.bookClient = new BookClient(restTemplate);
        this.mockServer = MockRestServiceServer.bindTo(restTemplate).build();
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void getBook() throws JsonProcessingException {
        final BookDTO expectedDto = BookDTO.builder()
            .build();
        final String data = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(expectedDto);

        mockServer.expect(requestTo("/api/book/" + BOOK_NUMBER))
            .andRespond(withSuccess(data, MediaType.APPLICATION_JSON));

        final Optional<BookDTO> response = bookClient.getBook(BOOK_NUMBER);
        mockServer.verify();
        assertTrue(response.isPresent());
    }
}
