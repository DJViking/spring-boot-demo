package com.example.demo.client;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.util.Optional;

import com.example.demo.config.WebConfig;
import com.example.demo.dto.CustomerDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.MockRestServiceServer;

@ActiveProfiles("it")
@ExtendWith(SpringExtension.class)
@RestClientTest({ CustomerClient.class, WebConfig.class })
public class CustomerClientTest {

    private final static Long CUSTOMER_NUMBER = 12345L;

    @Autowired
    private CustomerClient customerClient;

    @Autowired
    private MockRestServiceServer mockServer;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${first.baseUrl}")
    private String baseUrl;

    @Test
    public void getCustomer() throws JsonProcessingException {
        final CustomerDTO expectedDto = CustomerDTO.builder()
            .customerNumber(CUSTOMER_NUMBER)
            .name("John Doe")
            .address("Bakersfield 2b")
            .build();
        final String data = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(expectedDto);

        mockServer.expect(requestTo(baseUrl + "/api/customer/" + CUSTOMER_NUMBER))
            .andRespond(withSuccess(data, MediaType.APPLICATION_JSON));

        final Optional<CustomerDTO> response = customerClient.getCustomer(CUSTOMER_NUMBER);
        mockServer.verify();
        assertTrue(response.isPresent());
    }

}
